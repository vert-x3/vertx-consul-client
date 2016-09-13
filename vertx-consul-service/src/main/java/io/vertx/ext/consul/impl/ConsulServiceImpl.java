package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.*;

import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceImpl implements ConsulService {

    private final ConsulClient consulClient;

    public ConsulServiceImpl(ConsulClient consulClient) {
        this.consulClient = consulClient;
    }

    @Override
    public ConsulService getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler) {
        consulClient.getValue(key, resultHandler);
        return this;
    }

    @Override
    public ConsulService deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.deleteValue(key, resultHandler);
        return this;
    }

    @Override
    public ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler) {
        consulClient.getValues(keyPrefix, resultHandler);
        return this;
    }

    @Override
    public ConsulService deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.deleteValues(keyPrefix, resultHandler);
        return this;
    }

    @Override
    public ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.putValue(key, value, resultHandler);
        return this;
    }

    @Override
    public ConsulService createAclToken(AclToken aclToken, Handler<AsyncResult<String>> idHandler) {
        consulClient.createAclToken(aclToken, idHandler);
        return this;
    }

    @Override
    public ConsulService infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) {
        consulClient.infoAclToken(id, tokenHandler);
        return this;
    }

    @Override
    public ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.destroyAclToken(id, resultHandler);
        return this;
    }

    @Override
    public ConsulService fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler) {
        consulClient.fireEvent(event, resultHandler);
        return this;
    }

    @Override
    public ConsulService listEvents(Handler<AsyncResult<List<Event>>> resultHandler) {
        consulClient.listEvents(resultHandler);
        return this;
    }

    @Override
    public ConsulService registerService(Service service, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.registerService(service, resultHandler);
        return this;
    }

    @Override
    public ConsulService infoService(String name, Handler<AsyncResult<List<Service>>> resultHandler) {
        consulClient.infoService(name, resultHandler);
        return this;
    }

    @Override
    public void close() {
        consulClient.close();
    }
}
