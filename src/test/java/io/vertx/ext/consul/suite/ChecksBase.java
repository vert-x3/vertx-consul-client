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

import grpc.health.v1.HealthCheck;
import grpc.health.v1.HealthGrpc;
import io.grpc.stub.StreamObserver;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.impl.future.PromiseImpl;
import io.vertx.ext.consul.*;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.*;
import static io.vertx.test.core.TestUtils.randomAlphaString;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public abstract class ChecksBase extends ConsulTestBase {

  abstract String createCheck(CheckOptions opts);

  abstract void createCheck(TestContext tc, CheckOptions opts, Handler<String> idHandler);

  @Test
  public void ttlCheckLifecycle(TestContext tc) {
    CheckOptions opts = new CheckOptions()
      .setTtl("2s")
      .setName(randomAlphaString(10));
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

    sleep(vertx, 3000);

    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void httpCheckLifecycle() {
    HttpHealthReporter reporter = new HttpHealthReporterWithHeaderCheck(vertx);

    HashMap<String, List<String>> headers = new HashMap<>();
    headers.put(
      HttpHealthReporterWithHeaderCheck.TEST_HEADER_NAME,
      Collections.singletonList(HttpHealthReporterWithHeaderCheck.TEST_HEADER_VALUE)
    );

    CheckOptions opts = new CheckOptions()
      .setHttp("http://localhost:" + reporter.port())
      .setInterval("2s")
      .setHeaders(headers)
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 3000);
    Check check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());

    reporter.setStatus(CheckStatus.WARNING);
    sleep(vertx, 3000);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.WARNING, check.getStatus());

    reporter.setStatus(CheckStatus.CRITICAL);
    sleep(vertx, 3000);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    reporter.close();

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void grpcCheckLifecycle(TestContext tc) {
    if (consul.container.getVersion().compareTo("1.0.3") < 0) {
      System.out.println("skip " + consul.container.getVersion() + " version");
      return;
    }
    GrpcHealthReporter reporter = new GrpcHealthReporter(vertx);
    Async async = tc.async();

    CheckOptions opts = new CheckOptions()
      .setGrpc("localhost:" + reporter.port() + "/testee")
      .setInterval("2s")
      .setName("checkName");
    String checkId = createCheck(opts);

    reporter.start(tc.asyncAssertSuccess(v1 -> {
      vertx.setTimer(3000, t1 -> {
        getCheckInfo(tc, checkId, c1 -> {
          tc.assertEquals(CheckStatus.PASSING, c1.getStatus());
          reporter.setStatus(HealthCheck.HealthCheckResponse.ServingStatus.NOT_SERVING);

          vertx.setTimer(3000, t2 -> {
            getCheckInfo(tc, checkId, c2 -> {
              tc.assertEquals(CheckStatus.CRITICAL, c2.getStatus());

              reporter.close(tc.asyncAssertSuccess(v2 -> {
                writeClient.deregisterCheck(checkId, tc.asyncAssertSuccess(v -> async.complete()));
              }));
            });
          });

        });
      });
    }));
  }

  @Test
  public void tcpCheckLifecycle() {
    HttpHealthReporter reporter = new HttpHealthReporterImpl(vertx);

    CheckOptions opts = new CheckOptions()
      .setTcp("localhost:" + reporter.port())
      .setInterval("2s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 3000);
    Check check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());

    reporter.close();
    sleep(vertx, 3000);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  @Test
  public void scriptCheckLifecycle() {
    ScriptHealthReporter reporter = new ScriptHealthReporter();

    CheckOptions opts = new CheckOptions()
      .setScriptArgs(reporter.scriptArgs())
      .setInterval("2s")
      .setName("checkName");
    String checkId = createCheck(opts);

    sleep(vertx, 3000);
    Check check = getCheckInfo(checkId);
    assertEquals(CheckStatus.PASSING, check.getStatus());

    reporter.setStatus(CheckStatus.WARNING);
    sleep(vertx, 3000);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.WARNING, check.getStatus());

    reporter.setStatus(CheckStatus.CRITICAL);
    sleep(vertx, 3000);
    check = getCheckInfo(checkId);
    assertEquals(CheckStatus.CRITICAL, check.getStatus());

    runAsync(h -> writeClient.deregisterCheck(checkId, h));
  }

  Check getCheckInfo(String id) {
    List<Check> checks = getAsync(h -> writeClient.localChecks(h));
    return checks.stream()
      .filter(check -> check.getId().equals(id))
      .findFirst()
      .get();
  }

  void getCheckInfo(TestContext tc, String id, Handler<Check> resultHandler) {
    writeClient.localChecks(tc.asyncAssertSuccess(list -> {
      resultHandler.handle(list.stream()
        .filter(check -> check.getId().equals(id))
        .findFirst()
        .orElseThrow(NoSuchElementException::new));
    }));
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

    List<String> scriptArgs() {
      List<String> scriptArgs = new ArrayList<>();
      scriptArgs.add(scriptPath());
      return scriptArgs;
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

  private static abstract class HttpHealthReporter {

    private final HttpServer server;
    private final int port;
    protected CheckStatus status = CheckStatus.PASSING;

    HttpHealthReporter(Vertx vertx) {
      this.port = Utils.getFreePort();
      CountDownLatch latch = new CountDownLatch(1);
      this.server = vertx.createHttpServer().requestHandler(this::handle).listen(port, h -> latch.countDown());
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    abstract void handle(HttpServerRequest request);

    int port() {
      return port;
    }

    void close() {
      server.close();
    }

    protected int statusCode(CheckStatus status) {
      switch (status) {
        case PASSING:
          return 200;
        case WARNING:
          return 429;
        default:
          return 500;
      }
    }

    void setStatus(CheckStatus status) {
      this.status = status;
    }

  }

  private static class HttpHealthReporterImpl extends HttpHealthReporter {

    HttpHealthReporterImpl(Vertx vertx) {
      super(vertx);
    }

    @Override
    void handle(HttpServerRequest request) {
      request.response()
        .setStatusCode(statusCode(status))
        .end(status.name());
    }

  }

  private static class HttpHealthReporterWithHeaderCheck extends HttpHealthReporter {

    final static String TEST_HEADER_NAME = "test";
    final static String TEST_HEADER_VALUE = "foo";

    HttpHealthReporterWithHeaderCheck(Vertx vertx) {
      super(vertx);
    }

    @Override
    void handle(HttpServerRequest request) {
      String headerValue = request.getHeader(TEST_HEADER_NAME);
      Assert.assertEquals(TEST_HEADER_VALUE, headerValue);
      request.response()
        .setStatusCode(statusCode(status))
        .end(status.name());
    }

  }

  private static class GrpcHealthReporter {

    private final VertxServer server;
    private final int port;

    private HealthCheck.HealthCheckResponse.ServingStatus status = HealthCheck.HealthCheckResponse.ServingStatus.SERVING;

    GrpcHealthReporter(Vertx vertx) {
      this.port = Utils.getFreePort();
      HealthGrpc.HealthImplBase service = new HealthGrpc.HealthImplBase() {
        @Override
        public void check(
          HealthCheck.HealthCheckRequest request,
          StreamObserver<HealthCheck.HealthCheckResponse> response
        ) {
          response.onNext(HealthCheck.HealthCheckResponse.newBuilder()
            .setStatus(status)
            .build());
          response.onCompleted();
        }
      };
      server = VertxServerBuilder
        .forPort(vertx, port)
        .addService(service)
        .build();
    }

    int port() {
      return port;
    }

    void setStatus(HealthCheck.HealthCheckResponse.ServingStatus status) {
      this.status = status;
    }

    void start(Handler<AsyncResult<Void>> h) {
      server.start(h);
    }

    void close(Handler<AsyncResult<Void>> h) {
      server.shutdown(h);
    }

  }
}
