package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.impl.ConsulClientImpl;

import java.util.List;

/**
 * A Vert.x service used to interact with Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@VertxGen
public interface ConsulClient {

    static ConsulClient create(Vertx vertx, JsonObject config) {
        return new ConsulClientImpl(vertx, config);
    }

    @Fluent
    ConsulClient getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler);

    @Fluent
    ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler);

    @Fluent
    ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler);

    /**
     * Close the client and release its resources
     */
    void close();
}
