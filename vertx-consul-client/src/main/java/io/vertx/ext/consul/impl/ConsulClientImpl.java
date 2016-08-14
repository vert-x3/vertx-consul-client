package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulClient;

import java.util.Base64;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientImpl implements ConsulClient {

    private final HttpClient httpClient;

    public ConsulClientImpl(Vertx vertx, JsonObject config) {
        httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost(config.getString("host", "localhost"))
                .setDefaultPort(config.getInteger("port", 8500))
        );
    }

    @Override
    public ConsulClient getValue(String key, Handler<AsyncResult<String>> resultHandler) {
        httpClient.get("/v1/kv/" + key, h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> {
                    JsonArray arr = bh.toJsonArray();
                    resultHandler.handle(Future.succeededFuture(new String(Base64.getDecoder().decode(arr.getJsonObject(0).getString("Value")))));
                });
            } else {
                resultHandler.handle(Future.failedFuture("bad status code"));
            }
        }).end();
        return this;
    }

    @Override
    public ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        httpClient.put("/v1/kv/" + key, h -> {
            if (h.statusCode() == 200) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture("bad status code"));
            }
        }).end(value);
        return this;
    }

    @Override
    public void close() {
        httpClient.close();
    }
}
