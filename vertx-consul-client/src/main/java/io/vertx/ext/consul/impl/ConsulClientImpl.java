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
    public ConsulClient getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler) {
        request(HttpMethod.GET, "/v1/kv/" + key, resultHandler, buffer ->
                KeyValue.parse(buffer.toJsonArray().getJsonObject(0))).end();
        return this;
    }

    @Override
    public ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.DELETE, "/v1/kv/" + key, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValue>>> resultHandler) {
        request(HttpMethod.GET, "/v1/kv/" + keyPrefix, "recurse", resultHandler, buffer ->
                buffer.toJsonArray().stream()
                        .map(obj -> KeyValue.parse((JsonObject) obj))
                        .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.DELETE, "/v1/kv/" + keyPrefix, "recurse", resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) {
        return putValueWithOptions(key, value, null, resultHandler);
    }

    @Override
    public ConsulClient putValueWithOptions(String key, String value, KeyValueOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
        String query = null;
        if (options != null) {
            query = "flags=" + Long.toUnsignedString(options.getFlags());
            String acquireSession = options.getAcquireSession();
            if (acquireSession != null) {
                query += "&acquire=" + acquireSession;
            }
            String releaseSession = options.getReleaseSession();
            if (releaseSession != null) {
                query += "&release=" + releaseSession;
            }
            long cas = options.getCasIndex();
            if (cas >= 0) {
                query += "&cas=" + cas;
            }
        }
        request(HttpMethod.PUT, "/v1/kv/" + key, query, resultHandler, buffer -> Boolean.valueOf(buffer.toString())).end(value);
        return this;
    }

    @Override
    public ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
        request(HttpMethod.PUT, "/v1/acl/create", idHandler, buffer ->
                buffer.toJsonObject().getString("ID")).end(token.toJson().encode());
        return this;
    }

    @Override
    public ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
        request(HttpMethod.PUT, "/v1/acl/update", idHandler, buffer ->
                buffer.toJsonObject().getString("ID")).end(token.toJson().encode());
        return this;
    }

    @Override
    public ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) {
        request(HttpMethod.PUT, "/v1/acl/clone/" + id, idHandler, buffer ->
                buffer.toJsonObject().getString("ID")).end();
        return this;
    }

    @Override
    public ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler) {
        request(HttpMethod.GET, "/v1/acl/list", resultHandler, buffer ->
                buffer.toJsonArray().stream()
                        .map(obj -> new AclToken((JsonObject) obj))
                        .collect(Collectors.toList()))
                .end();
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
    public ConsulClient registerService(ServiceOptions service, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/agent/service/register", resultHandler).end(service.toJson().encode());
        return this;
    }

    @Override
    public ConsulClient maintenanceService(MaintenanceOptions opts, Handler<AsyncResult<Void>> resultHandler) {
        String query = "enable=" + opts.isEnable();
        if (opts.getReason() != null) {
            query += "&reason=" + opts.getReason();
        }
        request(HttpMethod.PUT, "/v1/agent/service/maintenance/" + opts.getId(), query, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient deregisterService(String id, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.GET, "/v1/agent/service/deregister/" + id, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient infoService(String name, Handler<AsyncResult<List<ServiceInfo>>> resultHandler) {
        request(HttpMethod.GET, "/v1/catalog/service/" + name, resultHandler, buffer -> buffer.toJsonArray().stream()
                .map(obj -> new ServiceInfo((JsonObject) obj))
                .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient localChecks(Handler<AsyncResult<List<CheckInfo>>> resultHandler) {
        request(HttpMethod.GET, "/v1/agent/checks", resultHandler, buffer -> buffer.toJsonObject().stream()
                .map(obj -> new CheckInfo((JsonObject) obj.getValue()))
                .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient localServices(Handler<AsyncResult<List<ServiceInfo>>> resultHandler) {
        request(HttpMethod.GET, "/v1/agent/services", resultHandler, buffer -> buffer.toJsonObject().stream()
                .map(obj -> ServiceInfo.parseAgentInfo((JsonObject) obj.getValue()))
                .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.GET, "/v1/agent/check/register", resultHandler).end(check.toJson().encode());
        return this;
    }

    @Override
    public ConsulClient deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.GET, "/v1/agent/check/deregister/" + id, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient passCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) {
        String query = check.getNote() == null ? null : "note=" + check.getNote();
        request(HttpMethod.GET, "/v1/agent/check/pass/" + check.getId(), query, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient warnCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) {
        String query = check.getNote() == null ? null : "note=" + check.getNote();
        request(HttpMethod.GET, "/v1/agent/check/warn/" + check.getId(), query, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient failCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) {
        String query = check.getNote() == null ? null : "note=" + check.getNote();
        request(HttpMethod.GET, "/v1/agent/check/fail/" + check.getId(), query, resultHandler).end();
        return this;
    }

    @Override
    public ConsulClient updateCheck(CheckInfo checkInfo, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/agent/check/update/" + checkInfo.getId(), resultHandler)
                .end(checkInfo.updateRequest().encode());
        return this;
    }

    @Override
    public ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler) {
        request(HttpMethod.GET, "/v1/status/leader", resultHandler, buffer -> {
            String leader = buffer.toString();
            return leader.substring(1, leader.length() - 2);
        }).end();
        return this;
    }

    @Override
    public ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler) {
        request(HttpMethod.GET, "/v1/status/peers", resultHandler, buffer -> buffer.toJsonArray().stream()
                .map(obj -> (String) obj)
                .collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient createSession(SessionOptions options, Handler<AsyncResult<String>> idHandler) {
        request(HttpMethod.PUT, "/v1/session/create", idHandler, buffer -> buffer.toJsonObject().getString("ID"))
                .end(options.toJson().encode());
        return this;
    }

    @Override
    public ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler) {
        request(HttpMethod.GET, "/v1/session/info/" + id, resultHandler, buffer -> new Session(buffer.toJsonArray().getJsonObject(0))).end();
        return this;
    }

    @Override
    public ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler) {
        request(HttpMethod.PUT, "/v1/session/renew/" + id, resultHandler, buffer -> new Session(buffer.toJsonArray().getJsonObject(0))).end();
        return this;
    }

    @Override
    public ConsulClient listSessions(Handler<AsyncResult<List<Session>>> resultHandler) {
        request(HttpMethod.GET, "/v1/session/list", resultHandler, buffer -> buffer.toJsonArray()
                .stream().map(obj -> new Session((JsonObject) obj)).collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler) {
        request(HttpMethod.GET, "/v1/session/node/" + nodeId, resultHandler, buffer -> buffer.toJsonArray()
                .stream().map(obj -> new Session((JsonObject) obj)).collect(Collectors.toList())).end();
        return this;
    }

    @Override
    public ConsulClient destroySession(String id, Handler<AsyncResult<Void>> resultHandler) {
        request(HttpMethod.PUT, "/v1/session/destroy/" + id, resultHandler).end();
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
        if (query == null) {
            query = "";
        }
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
