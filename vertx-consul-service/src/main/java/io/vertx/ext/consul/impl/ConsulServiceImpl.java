package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulService;
import io.vertx.ext.consul.KeyValuePair;

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
    public ConsulService getValues(String keyPrefix, Handler<AsyncResult<JsonArray>> resultHandler) {
        consulClient.getValues(keyPrefix, resultHandler);
        return this;
    }

    @Override
    public ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        consulClient.putValue(key, value, resultHandler);
        return this;
    }

    @Override
    public void close() {
        consulClient.close();
    }
}
