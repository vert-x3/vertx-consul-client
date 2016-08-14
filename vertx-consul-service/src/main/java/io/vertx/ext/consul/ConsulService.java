package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
@VertxGen
public interface ConsulService extends ConsulClient {

    @Override
    @Fluent
    ConsulService getValue(String key, Handler<AsyncResult<String>> resultHandler);

    @Override
    @Fluent
    ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler);

    @Override
    void close();
}
