package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulService;
import io.vertx.ext.consul.KeyValuePair;

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
    public ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler) {
        consulClient.getValues(keyPrefix, resultHandler);
        return this;
    }

    @Override
    public ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.putValue(key, value, resultHandler);
        return this;
    }

    @Override
    public ConsulService createAclToken(Handler<AsyncResult<String>> idHandler) {
        consulClient.createAclToken(idHandler);
        return this;
    }

    @Override
    public ConsulService infoAclToken(String id, Handler<AsyncResult<JsonObject>> tokenHandler) {
        consulClient.infoAclToken(id, tokenHandler);
        return this;
    }

    @Override
    public ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.destroyAclToken(id, resultHandler);
        return this;
    }

    @Override
    public void close() {
        consulClient.close();
    }
}
