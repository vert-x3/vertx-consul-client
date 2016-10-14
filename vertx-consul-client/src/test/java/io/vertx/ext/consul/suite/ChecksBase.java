package io.vertx.ext.consul.suite;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;
import static io.vertx.ext.consul.Utils.sleep;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public abstract class ChecksBase extends ConsulTestBase {

  abstract String createCheck(CheckOptions opts);

  @Test
  public void ttlCheckLifecycle() {
    CheckOptions opts = CheckOptions.ttl("1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    CheckInfo checkInfo;

    runAsync(h -> writeClient.warnCheck(new CheckOptions().setId(checkId).setNote("warn"), h));
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.warning, checkInfo.getStatus());
    assertEquals("warn", checkInfo.getOutput());

    runAsync(h -> writeClient.failCheck(new CheckOptions().setId(checkId).setNote("fail"), h));
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());
    assertEquals("fail", checkInfo.getOutput());

    runAsync(h -> writeClient.passCheck(new CheckOptions().setId(checkId).setNote("pass"), h));
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());
    assertEquals("pass", checkInfo.getOutput());

    sleep(vertx, 1500);

    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void httpCheckLifecycle() {
    HttpHealthReporter reporter = new HttpHealthReporter(vertx);

    CheckOptions opts = CheckOptions.http("http://localhost:" + reporter.port(), "1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 1500);
    CheckInfo checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());

    reporter.setStatus(CheckInfo.Status.warning);
    sleep(vertx, 1500);
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.warning, checkInfo.getStatus());

    reporter.setStatus(CheckInfo.Status.critical);
    sleep(vertx, 1500);
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

    reporter.close();

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void tcpCheckLifecycle() {
    HttpHealthReporter reporter = new HttpHealthReporter(vertx);

    CheckOptions opts = CheckOptions.tcp("localhost:" + reporter.port(), "1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 1500);
    CheckInfo checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());

    reporter.close();
    sleep(vertx, 1500);
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void scriptCheckLifecycle() {
    ScriptHealthReporter reporter = new ScriptHealthReporter();

    CheckOptions opts = CheckOptions.script(reporter.scriptPath(), "1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 1500);
    CheckInfo checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.passing, checkInfo.getStatus());

    reporter.setStatus(CheckInfo.Status.warning);
    sleep(vertx, 1500);
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.warning, checkInfo.getStatus());

    reporter.setStatus(CheckInfo.Status.critical);
    sleep(vertx, 1500);
    checkInfo = getCheckInfo(checkId);
    assertEquals(CheckInfo.Status.critical, checkInfo.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  private CheckInfo getCheckInfo(String id) {
    List<CheckInfo> checks = getAsync(h -> writeClient.localChecks(h));
    return checks.stream()
      .filter(check -> check.getId().equals(id))
      .findFirst()
      .get();
  }

  private static class ScriptHealthReporter {

    private File healthStatusFile;
    private File scriptFile;

    ScriptHealthReporter() {
      try {
        Path scriptDir = Files.createTempDirectory("vertx-consul-script-dir-");
        healthStatusFile = new File(scriptDir.toFile(), "status");
        String scriptName = "health_script." + (Utils.isWindows() ? "bat" : "sh");
        String scriptContent = Utils.readResource(scriptName)
          .replace("%STATUS_FILE%", healthStatusFile.getAbsolutePath());
        scriptFile = new File(scriptDir.toFile(), scriptName);
        PrintStream out = new PrintStream(scriptFile);
        out.print(scriptContent);
        out.close();
        scriptFile.setExecutable(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
      setStatus(CheckInfo.Status.passing);
    }

    String scriptPath() {
      return scriptFile.getAbsolutePath();
    }

    void setStatus(CheckInfo.Status status) {
      int statusCode;
      switch (status) {
        case passing:
          statusCode = 0;
          break;
        case warning:
          statusCode = 1;
          break;
        default:
          statusCode = 42;
          break;
      }
      try {
        PrintStream out = new PrintStream(healthStatusFile);
        out.print(statusCode);
        out.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private static class HttpHealthReporter {

    private final HttpServer server;
    private final int port;

    private CheckInfo.Status status = CheckInfo.Status.passing;

    HttpHealthReporter(Vertx vertx) {
      this.port = getFreePort();
      CountDownLatch latch = new CountDownLatch(1);
      this.server = vertx.createHttpServer().requestHandler(h -> h.response()
        .setStatusCode(statusCode(status))
        .end(status.name())).listen(port, h -> latch.countDown());
      try {
        latch.await(1, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    int port() {
      return port;
    }

    void setStatus(CheckInfo.Status status) {
      this.status = status;
    }

    void close() {
      server.close();
    }

    private int statusCode(CheckInfo.Status status) {
      switch (status) {
        case passing:
          return 200;
        case warning:
          return 429;
        default:
          return 500;
      }
    }

    private static int getFreePort() {
      int port = -1;
      try {
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return port;
    }
  }
}
