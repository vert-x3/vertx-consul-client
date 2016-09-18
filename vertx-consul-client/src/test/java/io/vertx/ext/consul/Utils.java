package io.vertx.ext.consul;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

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

    public static void runSync(Consumer<Handler<AsyncResult<Void>>> runner) throws Throwable {
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
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (future.failed()) {
            throw future.cause();
        }
    }

    public static <T> T getSync(Consumer<Handler<AsyncResult<T>>> runner) throws Throwable {
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
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (future.succeeded()) {
            return future.result();
        } else {
            throw future.cause();
        }
    }

    public static <T> Handler<AsyncResult<T>> handleResult(Handler<T> resultHandler) {
        return h -> {
            if (h.succeeded()) {
                resultHandler.handle(h.result());
            } else {
                throw new RuntimeException(h.cause());
            }
        };
    }
}
