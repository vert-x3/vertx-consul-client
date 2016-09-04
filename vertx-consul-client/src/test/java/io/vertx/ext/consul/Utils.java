package io.vertx.ext.consul;

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
}
