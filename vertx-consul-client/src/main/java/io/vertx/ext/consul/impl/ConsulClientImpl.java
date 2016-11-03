package io.vertx.ext.consul.impl;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientImpl implements ConsulClient {

  private static final String TOKEN_HEADER = "X-Consul-Token";

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
  public ConsulClient agentInfo(Handler<AsyncResult<JsonObject>> resultHandler) {
    request(HttpMethod.GET, "/v1/agent/self", resultHandler, (buffer, headers) -> buffer.toJsonObject()).end();
    return this;
  }

  @Override
  public ConsulClient coordinateNodes(Handler<AsyncResult<List<Coordinate>>> resultHandler) {
    request(HttpMethod.GET, "/v1/coordinate/nodes", resultHandler, (buffer, headers) ->
      buffer.toJsonArray().stream().map(obj -> new Coordinate((JsonObject) obj)).collect(Collectors.toList())
    ).end();
    return this;
  }

  @Override
  public ConsulClient coordinateDatacenters(Handler<AsyncResult<List<DcCoordinates>>> resultHandler) {
    request(HttpMethod.GET, "/v1/coordinate/datacenters", resultHandler, (buffer, headers) ->
      buffer.toJsonArray().stream().map(obj -> new DcCoordinates((JsonObject) obj)).collect(Collectors.toList())
    ).end();
    return this;
  }

  @Override
  public ConsulClient getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler) {
    return getValueBlocking(key, null, resultHandler);
  }

  @Override
  public ConsulClient getValueBlocking(String key, BlockingQueryOptions options, Handler<AsyncResult<KeyValue>> resultHandler) {
    request(HttpMethod.GET, "/v1/kv/" + key, blockingQuery("", options), resultHandler, (buffer, headers) ->
      KeyValue.parse(buffer.toJsonArray().getJsonObject(0))).end();
    return this;
  }

  private static String blockingQuery(String query, BlockingQueryOptions options) {
    if (options != null) {
      if (options.getIndex() > 0) {
        query += "&index=" + options.getIndex();
      }
      if (options.getWait() != null) {
        query += "&wait=" + options.getWait();
      }
      if (query.startsWith("&")) {
        query = query.substring(1);
      }
    }
    return query;
  }

  @Override
  public ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
    request(HttpMethod.DELETE, "/v1/kv/" + key, resultHandler).end();
    return this;
  }

  @Override
  public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValue>>> resultHandler) {
    return getValuesBlocking(keyPrefix, null, resultHandler);
  }

  @Override
  public ConsulClient getValuesBlocking(String keyPrefix, BlockingQueryOptions options, Handler<AsyncResult<List<KeyValue>>> resultHandler) {
    request(HttpMethod.GET, "/v1/kv/" + keyPrefix, blockingQuery("recurse", options), resultHandler, (buffer, headers) ->
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
    request(HttpMethod.PUT, "/v1/kv/" + key, query, resultHandler, (buffer, headers) -> Boolean.valueOf(buffer.toString())).end(value);
    return this;
  }

  @Override
  public ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
    request(HttpMethod.PUT, "/v1/acl/create", idHandler, (buffer, headers) ->
      buffer.toJsonObject().getString("ID")).end(token.toJson().encode());
    return this;
  }

  @Override
  public ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
    request(HttpMethod.PUT, "/v1/acl/update", idHandler, (buffer, headers) ->
      buffer.toJsonObject().getString("ID")).end(token.toJson().encode());
    return this;
  }

  @Override
  public ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) {
    request(HttpMethod.PUT, "/v1/acl/clone/" + id, idHandler, (buffer, headers) ->
      buffer.toJsonObject().getString("ID")).end();
    return this;
  }

  @Override
  public ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler) {
    request(HttpMethod.GET, "/v1/acl/list", resultHandler, (buffer, headers) ->
      buffer.toJsonArray().stream()
        .map(obj -> new AclToken((JsonObject) obj))
        .collect(Collectors.toList()))
      .end();
    return this;
  }

  @Override
  public ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) {
    request(HttpMethod.GET, "/v1/acl/info/" + id, tokenHandler, (buffer, headers) -> {
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
  public ConsulClient fireEvent(String name, Handler<AsyncResult<Event>> resultHandler) {
    fireEventWithOptions(name, null, resultHandler);
    return this;
  }

  @Override
  public ConsulClient fireEventWithOptions(String name, EventOptions options, Handler<AsyncResult<Event>> resultHandler) {
    String query = "";
    if (options != null) {
      if (options.getNode() != null) {
        query += "&node=" + options.getNode();
      }
      if (options.getService() != null) {
        query += "&service=" + options.getService();
      }
      if (options.getTag() != null) {
        query += "&tag=" + options.getTag();
      }
      if (!query.isEmpty()) {
        query = query.substring(1);
      }
    }
    request(HttpMethod.PUT, "/v1/event/fire/" + name, query, resultHandler, (buffer, headers) -> {
      JsonObject jsonObject = buffer.toJsonObject();
      return new Event(fixEventJson(jsonObject));
    }).end(options == null || options.getPayload() == null ? "" : options.getPayload());
    return this;
  }

  @Override
  public ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler) {
    request(HttpMethod.GET, "/v1/event/list", resultHandler, (buffer, headers) -> {
      JsonArray jsonArray = buffer.toJsonArray();
      return jsonArray.stream().map(obj -> new Event(fixEventJson(((JsonObject) obj)))).collect(Collectors.toList());
    }).end();
    return this;
  }

  private static JsonObject fixEventJson(JsonObject jsonObject) {
    String payload = jsonObject.getString("Payload");
    if (payload != null) {
      jsonObject.put("Payload", new String(Base64.getDecoder().decode(payload)));
    }
    return jsonObject;
  }

  @Override
  public ConsulClient registerService(ServiceOptions serviceOptions, Handler<AsyncResult<Void>> resultHandler) {
    request(HttpMethod.PUT, "/v1/agent/service/register", resultHandler).end(serviceOptions.toJson().encode());
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
  public ConsulClient catalogServiceNodes(String service, Handler<AsyncResult<List<Service>>> resultHandler) {
    return catalogServiceNodesWithTag(service, null, resultHandler);
  }

  @Override
  public ConsulClient catalogServiceNodesWithTag(String service, String tag, Handler<AsyncResult<List<Service>>> resultHandler) {
    String query = tag == null ? null : "tag=" + tag;
    request(HttpMethod.GET, "/v1/catalog/service/" + service, query, resultHandler, (buffer, headers) -> buffer.toJsonArray().stream()
      .map(obj -> new Service((JsonObject) obj))
      .collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler) {
    request(HttpMethod.GET, "/v1/catalog/datacenters", resultHandler, (buffer, headers) -> buffer.toJsonArray().getList()).end();
    return this;
  }

  @Override
  public ConsulClient catalogNodes(Handler<AsyncResult<List<Node>>> resultHandler) {
    request(HttpMethod.GET, "/v1/catalog/nodes", resultHandler, (buffer, headers) -> buffer.toJsonArray().stream()
      .map(obj -> new Node((JsonObject) obj))
      .collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient catalogServices(Handler<AsyncResult<List<Service>>> resultHandler) {
    request(HttpMethod.GET, "/v1/catalog/services", resultHandler, (buffer, headers) -> buffer.toJsonObject().stream()
      .map(ServiceParser::parseCatalogInfo).collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient localChecks(Handler<AsyncResult<List<Check>>> resultHandler) {
    request(HttpMethod.GET, "/v1/agent/checks", resultHandler, (buffer, headers) -> buffer.toJsonObject().stream()
      .map(obj -> new Check((JsonObject) obj.getValue()))
      .collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient localServices(Handler<AsyncResult<List<Service>>> resultHandler) {
    request(HttpMethod.GET, "/v1/agent/services", resultHandler, (buffer, headers) -> buffer.toJsonObject().stream()
      .map(obj -> ServiceParser.parseAgentInfo((JsonObject) obj.getValue()))
      .collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient catalogNodeServices(String node, Handler<AsyncResult<List<Service>>> resultHandler) {
    request(HttpMethod.GET, "/v1/catalog/node/" + node, resultHandler, (buffer, headers) -> {
      JsonObject jsonObject = buffer.toJsonObject();
      JsonObject nodeInfo = jsonObject.getJsonObject("Node");
      String nodeName = nodeInfo.getString("Node");
      String nodeAddress = nodeInfo.getString("Address");
      return jsonObject.getJsonObject("Services").stream()
        .map(obj -> ServiceParser.parseNodeInfo(nodeName, nodeAddress, (JsonObject) obj.getValue()))
        .collect(Collectors.toList());
    }).end();
    return this;
  }

  @Override
  public ConsulClient registerCheck(CheckOptions checkOptions, Handler<AsyncResult<Void>> resultHandler) {
    request(HttpMethod.GET, "/v1/agent/check/register", resultHandler).end(checkOptions.toJson().encode());
    return this;
  }

  @Override
  public ConsulClient deregisterCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    request(HttpMethod.GET, "/v1/agent/check/deregister/" + checkId, resultHandler).end();
    return this;
  }

  @Override
  public ConsulClient passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    return passCheckWithNote(checkId, null, resultHandler);
  }

  @Override
  public ConsulClient passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    String query = note == null ? null : "note=" + note;
    request(HttpMethod.GET, "/v1/agent/check/pass/" + checkId, query, resultHandler).end();
    return this;
  }

  @Override
  public ConsulClient warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    return warnCheckWithNote(checkId, null, resultHandler);
  }

  @Override
  public ConsulClient warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    String query = note == null ? null : "note=" + note;
    request(HttpMethod.GET, "/v1/agent/check/warn/" + checkId, query, resultHandler).end();
    return this;
  }

  @Override
  public ConsulClient failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    return failCheckWithNote(checkId, null, resultHandler);
  }

  @Override
  public ConsulClient failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    String query = note == null ? null : "note=" + note;
    request(HttpMethod.GET, "/v1/agent/check/fail/" + checkId, query, resultHandler).end();
    return this;
  }

  @Override
  public ConsulClient updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler) {
    return updateCheckWithNote(checkId, status, null, resultHandler);
  }

  @Override
  public ConsulClient updateCheckWithNote(String checkId, CheckStatus status, String note, Handler<AsyncResult<Void>> resultHandler) {
    JsonObject put = new JsonObject().put("Status", status.key);
    if (note != null) {
      put.put("Output", note);
    }
    request(HttpMethod.PUT, "/v1/agent/check/update/" + checkId, resultHandler)
      .end(put.encode());
    return this;
  }

  @Override
  public ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler) {
    request(HttpMethod.GET, "/v1/status/leader", resultHandler, (buffer, headers) -> {
      String leader = buffer.toString();
      return leader.substring(1, leader.length() - 2);
    }).end();
    return this;
  }

  @Override
  public ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler) {
    request(HttpMethod.GET, "/v1/status/peers", resultHandler, (buffer, headers) -> buffer.toJsonArray().stream()
      .map(obj -> (String) obj)
      .collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient createSession(Handler<AsyncResult<String>> idHandler) {
    createSessionWithOptions(null, idHandler);
    return this;
  }

  @Override
  public ConsulClient createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler) {
    HttpClientRequest req = request(HttpMethod.PUT, "/v1/session/create", idHandler, (buffer, headers) -> buffer.toJsonObject().getString("ID"));
    if (options != null) {
      req.end(options.toJson().encode());
    } else {
      req.end();
    }
    return this;
  }

  @Override
  public ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler) {
    request(HttpMethod.GET, "/v1/session/info/" + id, resultHandler, (buffer, headers) -> {
      JsonArray sessions = buffer.toJsonArray();
      if (sessions == null) {
        throw new InternalError();
      } else if (sessions.size() == 0) {
        throw new RuntimeException("Unknown session ID: " + id);
      } else {
        return new Session(sessions.getJsonObject(0));
      }
    }).end();
    return this;
  }

  @Override
  public ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler) {
    request(HttpMethod.PUT, "/v1/session/renew/" + id, resultHandler, (buffer, headers) -> new Session(buffer.toJsonArray().getJsonObject(0))).end();
    return this;
  }

  @Override
  public ConsulClient listSessions(Handler<AsyncResult<List<Session>>> resultHandler) {
    request(HttpMethod.GET, "/v1/session/list", resultHandler, (buffer, headers) -> buffer.toJsonArray()
      .stream().map(obj -> new Session((JsonObject) obj)).collect(Collectors.toList())).end();
    return this;
  }

  @Override
  public ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler) {
    request(HttpMethod.GET, "/v1/session/node/" + nodeId, resultHandler, (buffer, headers) -> buffer.toJsonArray()
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
    return request(method, path, "", resultHandler, (buffer, headers) -> null);
  }

  private <T> HttpClientRequest request(HttpMethod method, String path, String query,
                                        Handler<AsyncResult<T>> resultHandler) {
    return request(method, path, query, resultHandler, (buffer, headers) -> null);
  }

  private <T> HttpClientRequest request(HttpMethod method, String path,
                                        Handler<AsyncResult<T>> resultHandler, BiFunction<Buffer, MultiMap, T> mapper) {
    return request(method, path, "", resultHandler, mapper);
  }

  private <T> HttpClientRequest request(HttpMethod method, String path, String query,
                                        Handler<AsyncResult<T>> resultHandler, BiFunction<Buffer, MultiMap, T> mapper) {
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
        h.bodyHandler(bh -> {
          try {
            resultHandler.handle(Future.succeededFuture(mapper.apply(bh, h.headers())));
          } catch (Throwable throwable) {
            resultHandler.handle(Future.failedFuture(throwable));
          }
        });
      } else {
        resultHandler.handle(Future.failedFuture(h.statusMessage()));
      }
    });
    if (aclToken != null) {
      rq.putHeader(TOKEN_HEADER, aclToken);
    }
    return rq;
  }

}
