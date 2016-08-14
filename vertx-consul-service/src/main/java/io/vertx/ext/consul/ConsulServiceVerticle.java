package io.vertx.ext.consul;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.consul.impl.ConsulServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceVerticle extends AbstractVerticle {

    ConsulService service;

    @Override
    public void start() throws Exception {

        // Create the client object
        service = new ConsulServiceImpl(ConsulClient.create(vertx, config()));

        // And register it on the event bus against the configured address
        String address = config().getString("address");
        if (address == null) {
            throw new IllegalStateException("address field must be specified in config for client verticle");
        }
        ProxyHelper.registerService(ConsulService.class, vertx, service, address);
    }

    @Override
    public void stop() throws Exception {
        service.close();
    }
}
