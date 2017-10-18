package io.vertx.ext.consul;

import io.vertx.core.DeploymentOptions;
import org.junit.BeforeClass;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceTest extends ConsulClientTest {

  private static AtomicInteger cnt = new AtomicInteger(1);

  @BeforeClass
  public static void initClient() {
    ConsulTestBase.ctxFactory = vertx -> new ConsulContext(
      config -> {
        String addr = "vertx.consul." + cnt.incrementAndGet();
        DeploymentOptions options = new DeploymentOptions().setConfig(config.toJson().put("address", addr));
        Utils.<String>getAsync(h -> vertx.deployVerticle("service:io.vertx.consul-service", options, h));
        return ConsulService.createEventBusProxy(vertx, addr);
      },
      client -> {
      }
    );
  }

}
