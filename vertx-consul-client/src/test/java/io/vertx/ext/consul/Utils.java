package io.vertx.ext.consul;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Utils {

  public static String readResource(String fileName) throws Exception {
    final URL resource = Utils.class.getClassLoader().getResource(fileName);
    if (resource == null) {
      throw new RuntimeException("resource not found");
    }
    return new String(Files.readAllBytes(Paths.get(resource.toURI())));
  }

  public static void runAsync(Consumer<Handler<AsyncResult<Void>>> runner) {
    CountDownLatch latch = new CountDownLatch(1);
    Future<Void> future = Future.future();
    runner.accept(h -> {
      if (h.succeeded()) {
        future.complete();
      } else {
        future.fail(h.cause());
      }
      latch.countDown();
    });
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (future.failed()) {
      throw new RuntimeException(future.cause());
    }
  }

  public static <T> T getAsync(Consumer<Handler<AsyncResult<T>>> runner) {
    CountDownLatch latch = new CountDownLatch(1);
    Future<T> future = Future.future();
    runner.accept(h -> {
      if (h.succeeded()) {
        future.complete(h.result());
      } else {
        future.fail(h.cause());
      }
      latch.countDown();
    });
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (future.succeeded()) {
      return future.result();
    } else {
      throw new RuntimeException(future.cause());
    }
  }

  public static void sleep(Vertx vertx, long millis) {
    CountDownLatch latch = new CountDownLatch(1);
    vertx.setTimer(millis, h -> latch.countDown());
    try {
      latch.await(2 * millis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static String osResolve() {
    String os = System.getProperty("os.name").toLowerCase();
    String binaryVersion = "linux";
    if (os.contains("mac")) {
      binaryVersion = "darwin";
    } else if (os.contains("windows")) {
      binaryVersion = "windows";
    }
    return binaryVersion;
  }

  public static boolean isWindows() {
    return "windows".equals(osResolve());
  }

}
