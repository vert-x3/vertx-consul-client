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
import io.vertx.ext.consul.*;

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
    private final String dc;

    public ConsulClientImpl(Vertx vertx, JsonObject config) {
        Objects.requireNonNull(vertx);
        Objects.requireNonNull(config);
        httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost(config.getString("host", "localhost"))
                .setDefaultPort(config.getInteger("port", 8500))
        );
        aclToken = config.getString("acl_token");
        dc = config.getString("dc");
    }

    @Override
    public ConsulClient getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler) {
        request(HttpMethod.GET, "/v1/kv/" + key, resultHandler, buffer ->
                KeyValuePair.parseConsulResponse(buffer.toJsonArray().getJsonObject(0))).end();
        return this;
    }

    @Override
    public ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.DELETE, "/v1/kv/" + key, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler) {
        request(HttpMethod.GET, "/v1/kv/" + keyPrefix, "recurse", resultHandler, buffer ->
                buffer.toJsonArray().stream()
                        .map(obj -> KeyValuePair.parseConsulResponse((JsonObject) obj))
                        .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.DELETE, "/v1/kv/" + keyPrefix, "recurse", resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/kv/" + key, resultHandler, buffer -> null).end(value);
        return this;
    }

    @Override
    public ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
        request(HttpMethod.PUT, "/v1/acl/create", idHandler, buffer ->
                buffer.toJsonObject().getString("ID")).end(token.toJson().encode());
        return this;
    }

    @Override
    public ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) {
        request(HttpMethod.GET, "/v1/acl/info/" + id, tokenHandler, buffer -> {
            JsonObject jsonObject = buffer.toJsonArray().getJsonObject(0);
            return new AclToken(jsonObject);
        }).end();
        return this;
    }

    @Override
    public ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/acl/destroy/" + id, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler) {
        request(HttpMethod.PUT, "/v1/event/fire/" + event.getName(), resultHandler, buffer -> {
            JsonObject jsonObject = buffer.toJsonObject();
            return Event.parseConsulResponse(jsonObject);
        }).end(event.getPayload() == null ? "" : event.getPayload());
        return this;
    }

    @Override
    public ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler) {
        request(HttpMethod.GET, "/v1/event/list", resultHandler, buffer -> {
            JsonArray jsonArray = buffer.toJsonArray();
            return jsonArray.stream().map(obj -> Event.parseConsulResponse((JsonObject) obj)).collect(Collectors.toList());
        }).end();
        return this;
    }

    @Override
    public ConsulClient registerService(Service service, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/agent/service/register", resultHandler).end(service.registerRequest().encode());
        return this;
    }

    @Override
    public ConsulClient infoService(String name, Handler<AsyncResult<List<Service>>> resultHandler) {
        request(HttpMethod.GET, "/v1/catalog/service/" + name, resultHandler, buffer -> buffer.toJsonArray().stream()
                .map(obj -> new Service((JsonObject) obj))
                .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient localServices(Handler<AsyncResult<List<Service>>> resultHandler) {
        request(HttpMethod.GET, "/v1/agent/services", resultHandler, buffer -> buffer.toJsonObject().stream()
                .map(obj -> Service.parseAgentInfo((JsonObject) obj.getValue()))
                .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public void close() {
        httpClient.close();
    }

    private <T> HttpClientRequest request(HttpMethod method, String path,
                                          Handler<AsyncResult<T>> resultHandler) {
        return request(method, path, "", resultHandler, buffer -> null);
    }

    private <T> HttpClientRequest request(HttpMethod method, String path, String query,
                                          Handler<AsyncResult<T>> resultHandler) {
        return request(method, path, query, resultHandler, buffer -> null);
    }

    private <T> HttpClientRequest request(HttpMethod method, String path,
                                          Handler<AsyncResult<T>> resultHandler, Function<Buffer, T> mapper) {
        return request(method, path, "", resultHandler, mapper);
    }

    private <T> HttpClientRequest request(HttpMethod method, String path, String query,
                             Handler<AsyncResult<T>> resultHandler, Function<Buffer, T> mapper) {
        if (dc != null) {
            if (!query.isEmpty()) {
                query += "&";
            }
            query += "dc=" + dc;
        }
        HttpClientRequest rq = httpClient.request(method, path + "?" + query, h -> {
            if (h.statusCode() == 200) {
                h.bodyHandler(bh -> resultHandler.handle(Future.succeededFuture(mapper.apply(bh))));
            } else {
                resultHandler.handle(Future.failedFuture(h.statusMessage()));
            }
        });
        if (aclToken != null) {
            rq.putHeader("X-Consul-Token", aclToken);
        }
        return rq;
    }

}
