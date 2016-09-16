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
    ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler);

    @Fluent
    ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

    @Fluent
    ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler);

    @Fluent
    ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler);

    @Fluent
    ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler);

    @Fluent
    ConsulClient registerService(ServiceOptions service, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient infoService(String name, Handler<AsyncResult<List<ServiceInfo>>> resultHandler);

    @Fluent
    ConsulClient localServices(Handler<AsyncResult<List<ServiceInfo>>> resultHandler);

    @Fluent
    ConsulClient localChecks(Handler<AsyncResult<List<CheckInfo>>> resultHandler);

    @Fluent
    ConsulClient registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient passCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient warnCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient failCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient updateCheck(CheckInfo checkInfo, Handler<AsyncResult<Void>> resultHandler);

    /**
     * Close the client and release its resources
     */
    void close();
}
