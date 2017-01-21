package io.vertx.ext.consul;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.impl.ConsulServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceVerticle extends AbstractVerticle {

  private ConsulService service;
  private MessageConsumer<JsonObject> messageConsumer;

  @Override
  public void start() throws Exception {
    service = new ConsulServiceImpl(ConsulClient.create(vertx, new ConsulClientOptions(config())));
    String address = config().getString("address");
    if (address == null) {
      throw new IllegalStateException("address field must be specified in config for client verticle");
    }
    messageConsumer = ProxyHelper.registerService(ConsulService.class, vertx, service, address);
  }

  @Override
  public void stop() throws Exception {
    if (messageConsumer != null) {
      ProxyHelper.unregisterService(messageConsumer);
    }
    service.close();
  }
}
