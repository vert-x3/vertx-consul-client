package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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
    ConsulService deleteValue(String key, Handler<AsyncResult<Void>> resultHandler);

    @Override
    @Fluent
    ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler);

    @Override
    @Fluent
    ConsulService deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler);

    @Override
    @Fluent
    ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler);

    @Override
    @Fluent
    ConsulService createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

    @Override
    @Fluent
    ConsulService infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler);

    @Override
    @Fluent
    ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler);

    @Fluent
    ConsulService listEvents(Handler<AsyncResult<List<Event>>> resultHandler);

    @Fluent
    ConsulService registerService(ServiceOptions service, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService infoService(String name, Handler<AsyncResult<List<ServiceInfo>>> resultHandler);

    @Fluent
    ConsulService localServices(Handler<AsyncResult<List<ServiceInfo>>> resultHandler);

    @Fluent
    ConsulService localChecks(Handler<AsyncResult<List<CheckInfo>>> resultHandler);

    @Fluent
    ConsulService registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService passCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService warnCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService failCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulService updateCheck(CheckInfo checkInfo, Handler<AsyncResult<Void>> resultHandler);

    @Override
    void close();
}
