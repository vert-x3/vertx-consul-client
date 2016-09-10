package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.Event;
import io.vertx.ext.consul.KeyValuePair;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientImpl implements ConsulClient {

    private final HttpClient httpClient;
    private final String aclToken;

    public ConsulClientImpl(Vertx vertx, JsonObject config) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(config);
        httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost(config.getString("consul_host", "localhost"))
                .setDefaultPort(config.getInteger("consul_port", 8500))
        );
        aclToken = config.getString("acl_token");
    }

    @Override
    public ConsulClient getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler) {
        request(HttpMethod.GET, "/v1/kv/" + key, resultHandler, buffer ->
                parseValue(buffer.toJsonArray().getJsonObject(0)));
        return this;
    }

    @Override
    public ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.DELETE, "/v1/kv/" + key, resultHandler);
        return this;
    }

    @Override
    public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler) {
        request(HttpMethod.GET, "/v1/kv/" + keyPrefix + "?recurse", resultHandler, buffer ->
                buffer.toJsonArray().stream()
                        .map(obj -> parseValue((JsonObject) obj))
                        .collect(Collectors.toList()));
        return this;
    }

    @Override
    public ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.DELETE, "/v1/kv/" + keyPrefix + "?recurse", resultHandler);
        return this;
    }

    @Override
    public ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/kv/" + key, value, resultHandler, buffer -> null);
        return this;
    }

    @Override
    public ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
        request(HttpMethod.PUT, "/v1/acl/create", token.toJson().encode(), idHandler, buffer ->
                buffer.toJsonObject().getString("ID"));
        return this;
    }

    @Override
    public ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) {
        request(HttpMethod.GET, "/v1/acl/info/" + id, tokenHandler, buffer -> {
            JsonObject jsonObject = buffer.toJsonArray().getJsonObject(0);
            return new AclToken(jsonObject);
        });
        return this;
    }

    @Override
    public ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/acl/destroy/" + id, resultHandler);
        return this;
    }

    @Override
    public ConsulClient fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler) {
        request(HttpMethod.PUT, "/v1/event/fire/" + event.getName(), event.getPayload(), resultHandler, buffer -> {
            JsonObject jsonObject = buffer.toJsonObject();
            return new Event(jsonObject);
        });
        return this;
    }

    @Override
    public ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler) {
        request(HttpMethod.GET, "/v1/event/list", resultHandler, buffer -> {
            JsonArray jsonArray = buffer.toJsonArray();
            return jsonArray.stream().map(obj -> new Event((JsonObject) obj)).collect(Collectors.toList());
        });
        return this;
    }

    @Override
    public void close() {
        httpClient.close();
    }

    private <T> void request(HttpMethod method, String path,
                             Handler<AsyncResult<T>> resultHandler, Function<Buffer, T> mapper) {
        request(method, path, null, resultHandler, mapper);
    }

    private <T> void request(HttpMethod method, String path,
                             Handler<AsyncResult<T>> resultHandler) {
        request(method, path, null, resultHandler, buffer -> null);
    }

    private <T> void request(HttpMethod method, String path, String body,
                             Handler<AsyncResult<T>> resultHandler, Function<Buffer, T> mapper) {
        HttpClientRequest rq = httpClient.request(method, path, h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> resultHandler.handle(Future.succeededFuture(mapper.apply(bh))));
            } else {
                resultHandler.handle(Future.failedFuture(h.statusMessage()));
            }
        });
        if (aclToken != null) {
            rq.putHeader("X-Consul-Token", aclToken);
        }
        if (body == null) {
            rq.end();
        } else {
            rq.end(body);
        }
    }

    private static KeyValuePair parseValue(JsonObject object) {
        String key = object.getString("Key");
        String value = new String(Base64.getDecoder().decode(object.getString("Value")));
        return new KeyValuePair(key, value);
    }
}
