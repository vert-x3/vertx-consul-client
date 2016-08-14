package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.ConsulClient;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientImpl implements ConsulClient {

    @Override
    public ConsulClient getValue(String key, Handler<AsyncResult<String>> resultHandler) {
        return null;
    }

    @Override
    public ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        return null;
    }

    @Override
    public void close() {

    }
}
