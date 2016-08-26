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
import io.vertx.ext.consul.KeyValuePair;

import java.util.Base64;
import java.util.Objects;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientImpl implements ConsulClient {

    private final HttpClient httpClient;

    public ConsulClientImpl(Vertx vertx, JsonObject config) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(config);
        httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost(config.getString("host", "localhost"))
                .setDefaultPort(config.getInteger("port", 8500))
        );
    }

    @Override
    public ConsulClient getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler) {
        httpClient.get("/v1/kv/" + key, h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> {
                    JsonArray arr = bh.toJsonArray();
                    resultHandler.handle(Future.succeededFuture(parseValue(arr.getJsonObject(0))));
                });
            } else {
                resultHandler.handle(Future.failedFuture("bad status code"));
            }
        }).end();
        return this;
    }

    @Override
    public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<JsonArray>> resultHandler) {
        httpClient.get("/v1/kv/" + keyPrefix + "?recurse", h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> {
                    JsonArray arr = bh.toJsonArray().stream()
                            .map(obj -> parseValue((JsonObject) obj))
                            .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                    resultHandler.handle(Future.succeededFuture(arr));
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

    private static KeyValuePair parseValue(JsonObject object) {
        String key = object.getString("Key");
        String value = new String(Base64.getDecoder().decode(object.getString("Value")));
        return new KeyValuePair(key, value);
    }
}
