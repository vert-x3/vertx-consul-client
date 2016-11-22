/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul.suite;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.consul.*;
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
    CheckOptions opts = new CheckOptions()
      .setTtl("1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    Check check;

    runAsync(h -> writeClient.warnCheckWithNote(checkId, "warn", h));
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.WARNING, check.getStatus());
    assertEquals("warn", check.getOutput());

    runAsync(h -> writeClient.failCheckWithNote(checkId, "fail", h));
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());
    assertEquals("fail", check.getOutput());

    runAsync(h -> writeClient.passCheckWithNote(checkId, "pass", h));
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());
    assertEquals("pass", check.getOutput());

    sleep(vertx, 1500);

    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void httpCheckLifecycle() {
    HttpHealthReporter reporter = new HttpHealthReporter(vertx);

    CheckOptions opts = new CheckOptions()
      .setHttp("http://localhost:" + reporter.port())
      .setInterval("1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 1500);
    Check check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());

    reporter.setStatus(CheckStatus.WARNING);
    sleep(vertx, 1500);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.WARNING, check.getStatus());

    reporter.setStatus(CheckStatus.CRITICAL);
    sleep(vertx, 1500);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    reporter.close();

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void tcpCheckLifecycle() {
    HttpHealthReporter reporter = new HttpHealthReporter(vertx);

    CheckOptions opts = new CheckOptions()
      .setTcp("localhost:" + reporter.port())
      .setInterval("1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 1500);
    Check check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());

    reporter.close();
    sleep(vertx, 1500);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void scriptCheckLifecycle() {
    ScriptHealthReporter reporter = new ScriptHealthReporter();

    CheckOptions opts = new CheckOptions()
      .setScript(reporter.scriptPath())
      .setInterval("1s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 1500);
    Check check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());

    reporter.setStatus(CheckStatus.WARNING);
    sleep(vertx, 1500);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.WARNING, check.getStatus());

    reporter.setStatus(CheckStatus.CRITICAL);
    sleep(vertx, 1500);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  private Check getCheckInfo(String id) {
    List<Check> checks = getAsync(h -> writeClient.localChecks(h));
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
      setStatus(CheckStatus.PASSING);
    }

    String scriptPath() {
      return scriptFile.getAbsolutePath();
    }

    void setStatus(CheckStatus status) {
      int statusCode;
      switch (status) {
        case PASSING:
          statusCode = 0;
          break;
        case WARNING:
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

    private CheckStatus status = CheckStatus.PASSING;

    HttpHealthReporter(Vertx vertx) {
      this.port = getFreePort();
      CountDownLatch latch = new CountDownLatch(1);
      this.server = vertx.createHttpServer().requestHandler(h -> h.response()
        .setStatusCode(statusCode(status))
        .end(status.name())).listen(port, h -> latch.countDown());
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    int port() {
      return port;
    }

    void setStatus(CheckStatus status) {
      this.status = status;
    }

    void close() {
      server.close();
    }

    private int statusCode(CheckStatus status) {
      switch (status) {
        case PASSING:
          return 200;
        case WARNING:
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
