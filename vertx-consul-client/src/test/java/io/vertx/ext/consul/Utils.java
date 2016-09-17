package io.vertx.ext.consul;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
