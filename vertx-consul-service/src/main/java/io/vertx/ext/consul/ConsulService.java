package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.List;

@ProxyGen
@VertxGen
public interface ConsulService extends ConsulClient {

    /**
     * Create a proxy to a service that is deployed somewhere on the event bus
     *
     * @param vertx  the Vert.x instance
     * @param address  the address the service is listening on on the event bus
     * @return the service
     */
    static ConsulService createEventBusProxy(Vertx vertx, String address) {
        return ProxyHelper.createProxy(ConsulService.class, vertx, address);
    }

    @Override
    @Fluent
    ConsulService getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler);

    @Override
    @Fluent
    ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler);

    @Override
    @Fluent
    ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler);

    @Override
    @Fluent
    ConsulService createAclToken(String name, String rules, Handler<AsyncResult<String>> idHandler);

    @Override
    @Fluent
    ConsulService infoAclToken(String id, Handler<AsyncResult<JsonObject>> tokenHandler);

    @Override
    @Fluent
    ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler);

    @Override
    void close();
}
