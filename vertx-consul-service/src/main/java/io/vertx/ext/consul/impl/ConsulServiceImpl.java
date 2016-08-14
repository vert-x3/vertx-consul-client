package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulService;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceImpl implements ConsulService {

    private final ConsulClient consulClient;

    public ConsulServiceImpl(ConsulClient consulClient) {
        this.consulClient = consulClient;
    }

    @Override
    public ConsulService getValue(String key, Handler<AsyncResult<String>> resultHandler) {
        consulClient.getValue(key, resultHandler);
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
