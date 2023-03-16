/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.*;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.impl.Utils.listOf;
import static io.vertx.ext.consul.impl.Utils.urlEncode;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulClientImpl implements ConsulClient {

  private static final String TOKEN_HEADER = "X-Consul-Token";
  private static final String INDEX_HEADER = "X-Consul-Index";

  private static final List<Integer> DEFAULT_VALID_CODES = Collections.singletonList(HttpResponseStatus.OK.code());
  private static final List<Integer> TXN_VALID_CODES = Arrays.asList(HttpResponseStatus.OK.code(), HttpResponseStatus.CONFLICT.code());
  private static final List<Integer> KV_VALID_CODES = Arrays.asList(HttpResponseStatus.OK.code(), HttpResponseStatus.NOT_FOUND.code());

  private final WebClient webClient;
  private final Context ctx;
  private final String aclToken;
  private final String dc;
  private final long timeoutMs;

  public ConsulClientImpl(Vertx vertx, ConsulClientOptions options) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(options);
    webClient = WebClient.create(vertx, options);
    ctx = vertx.getOrCreateContext();
    aclToken = options.getAclToken();
    dc = options.getDc();
    timeoutMs = options.getTimeout();
  }

  @Override
  public Future<JsonObject> agentInfo() {
    Promise<JsonObject> promise = Promise.promise();
    requestObject(HttpMethod.GET, "/v1/agent/self", null, null, promise, (obj, headers) -> obj);
    return promise.future();
  }

  @Override
  public Future<CoordinateList> coordinateNodes() {
    return coordinateNodesWithOptions(null);
  }

  @Override
  public Future<CoordinateList> coordinateNodesWithOptions(BlockingQueryOptions options) {
    Promise<CoordinateList> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/coordinate/nodes", new Query().put(options), null, promise, (arr, headers) -> {
      List<Coordinate> list = arr.stream().map(obj -> CoordinateParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new CoordinateList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<List<DcCoordinates>> coordinateDatacenters() {
    Promise<List<DcCoordinates>> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/coordinate/datacenters", null, null, promise, (arr, headers) ->
      arr.stream().map(obj -> CoordinateParser.parseDc((JsonObject) obj)).collect(Collectors.toList())
    );
    return promise.future();
  }

  @Override
  public Future<List<String>> getKeys(String keyPrefix) {
    return getKeysWithOptions(keyPrefix, null);
  }

  @Override
  public Future<List<String>> getKeysWithOptions(String keyPrefix, BlockingQueryOptions options) {
    Promise<List<String>> promise = Promise.promise();
    Query query = Query.of("recurse", true).put("keys", true).put(options);
    request(KV_VALID_CODES, HttpMethod.GET, "/v1/kv/" + urlEncode(keyPrefix), query, null, promise, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new ArrayList<>();
      } else {
        return resp.bodyAsJsonArray().stream().map(Object::toString).collect(Collectors.toList());
      }
    });
    return promise.future();
  }

  @Override
  public Future<KeyValue> getValue(String key) {
    return getValueWithOptions(key, null);
  }

  @Override
  public Future<KeyValue> getValueWithOptions(String key, BlockingQueryOptions options) {
    Promise<KeyValue> promise = Promise.promise();
    request(KV_VALID_CODES, HttpMethod.GET, "/v1/kv/" + urlEncode(key), new Query().put(options), null, promise, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new KeyValue();
      } else {
        return KVParser.parse(resp.bodyAsJsonArray().getJsonObject(0));
      }
    });
    return promise.future();
  }

  @Override
  public Future<Void> deleteValue(String key) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.DELETE, "/v1/kv/" + urlEncode(key), null, null, promise);
    return promise.future();
  }

  @Override
  public Future<KeyValueList> getValues(String keyPrefix) {
    return getValuesWithOptions(keyPrefix, null);
  }

  @Override
  public Future<KeyValueList> getValuesWithOptions(String keyPrefix, BlockingQueryOptions options) {
    Promise<KeyValueList> promise = Promise.promise();
    Query query = Query.of("recurse", true).put(options);
    request(KV_VALID_CODES, HttpMethod.GET, "/v1/kv/" + urlEncode(keyPrefix), query, null, promise, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new KeyValueList();
      } else {
        List<KeyValue> list = resp.bodyAsJsonArray().stream().map(obj -> KVParser.parse((JsonObject) obj)).collect(Collectors.toList());
        return new KeyValueList().setList(list).setIndex(Long.parseLong(resp.headers().get(INDEX_HEADER)));
      }
    });
    return promise.future();
  }

  @Override
  public Future<Void> deleteValues(String keyPrefix) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.DELETE, "/v1/kv/" + urlEncode(keyPrefix), Query.of("recurse", true), null, promise);
    return promise.future();
  }

  @Override
  public Future<Boolean> putValue(String key, String value) {
    return putValueWithOptions(key, value, null);
  }

  @Override
  public Future<Boolean> putValueWithOptions(String key, String value, KeyValueOptions options) {
    Promise<Boolean> promise = Promise.promise();
    Query query = new Query();
    if (options != null) {
      query.put("flags", Long.toUnsignedString(options.getFlags()))
        .put("acquire", options.getAcquireSession())
        .put("release", options.getReleaseSession());
      long cas = options.getCasIndex();
      if (cas >= 0) {
        query.put("cas", cas);
      }
    }
    requestString(HttpMethod.PUT, "/v1/kv/" + urlEncode(key), query, value, promise,
      (bool, headers) -> Boolean.valueOf(bool));
    return promise.future();
  }

  @Override
  public Future<TxnResponse> transaction(TxnRequest request) {
    Promise<TxnResponse> promise = Promise.promise();
    String boby = request.toJson().getJsonArray("operations").encode();
    request(TXN_VALID_CODES, HttpMethod.PUT, "/v1/txn", null, boby, promise, resp -> TxnResponseParser.parse(resp.bodyAsJsonObject()));
    return promise.future();
  }

  @Override
  public Future<String> createAclToken(AclToken token) {
    Promise<String> promise = Promise.promise();
    requestObject(HttpMethod.PUT, "/v1/acl/create", null, token.toJson().encode(), promise, (obj, headers) ->
      obj.getString("ID"));
    return promise.future();
  }

  @Override
  public Future<String> updateAclToken(AclToken token) {
    Promise<String> promise = Promise.promise();
    requestObject(HttpMethod.PUT, "/v1/acl/update", null, token.toJson().encode(), promise, (obj, headers) ->
      obj.getString("ID"));
    return promise.future();
  }

  @Override
  public Future<String> cloneAclToken(String id) {
    Promise<String> promise = Promise.promise();
    requestObject(HttpMethod.PUT, "/v1/acl/clone/" + urlEncode(id), null, null, promise, (obj, headers) ->
      obj.getString("ID"));
    return promise.future();
  }

  @Override
  public Future<List<AclToken>> listAclTokens() {
    Promise<List<AclToken>> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/acl/list", null, null, promise, (arr, headers) ->
      arr.stream()
        .map(obj -> new AclToken((JsonObject) obj))
        .collect(Collectors.toList()));
    return promise.future();
  }

  @Override
  public Future<AclToken> infoAclToken(String id) {
    Promise<AclToken> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/acl/info/" + urlEncode(id), null, null, promise, (arr, headers) -> {
      JsonObject jsonObject = arr.getJsonObject(0);
      return new AclToken(jsonObject);
    });
    return promise.future();
  }

  @Override
  public Future<Void> destroyAclToken(String id) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/acl/destroy/" + urlEncode(id), null, null, promise);
    return promise.future();
  }

  @Override
  public Future<Event> fireEvent(String name) {
    return fireEventWithOptions(name, null);
  }

  @Override
  public Future<Event> fireEventWithOptions(String name, EventOptions options) {
    Promise<Event> promise = Promise.promise();
    Query query = new Query();
    if (options != null) {
      query.put("node", options.getNode()).put("service", options.getService()).put("tag", options.getTag());
    }
    String body = options == null || options.getPayload() == null ? "" : options.getPayload();
    requestObject(HttpMethod.PUT, "/v1/event/fire/" + urlEncode(name), query, body, promise, (jsonObject, headers) -> EventParser.parse(jsonObject));
    return promise.future();
  }

  @Override
  public Future<EventList> listEvents() {
    return listEventsWithOptions(null);
  }

  @Override
  public Future<EventList> listEventsWithOptions(EventListOptions options) {
    Promise<EventList> promise = Promise.promise();
    Query query = options == null ? null : Query.of(options.getBlockingOptions()).put("name", options.getName());
    requestArray(HttpMethod.GET, "/v1/event/list", query, null, promise, (jsonArray, headers) -> {
      List<Event> list = jsonArray.stream().map(obj -> EventParser.parse(((JsonObject) obj))).collect(Collectors.toList());
      return new EventList().setList(list).setIndex(Long.parseUnsignedLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<Void> registerService(ServiceOptions serviceOptions) {
    Promise<Void> promise = Promise.promise();
    JsonObject jsonOpts = new JsonObject()
      .put("ID", serviceOptions.getId())
      .put("Name", serviceOptions.getName())
      .put("Tags", serviceOptions.getTags())
      .put("Address", serviceOptions.getAddress())
      .put("Port", serviceOptions.getPort());
    if (serviceOptions.getCheckOptions() != null) {
      jsonOpts.put("Check", checkOpts(serviceOptions.getCheckOptions(), "CheckID", false));
    }
    if (serviceOptions.getCheckListOptions() != null) {
      jsonOpts.put("Checks", checkListOpts(serviceOptions.getCheckListOptions(), "CheckID", false));
    }
    if (serviceOptions.getMeta() != null && !serviceOptions.getMeta().isEmpty()) {
      jsonOpts.put("Meta", serviceOptions.getMeta());
    }
    requestVoid(HttpMethod.PUT, "/v1/agent/service/register", null, jsonOpts.encode(), promise);
    return promise.future();
  }

  @Override
  public Future<Void> maintenanceService(MaintenanceOptions opts) {
    Promise<Void> promise = Promise.promise();
    Query query = Query.of("enable", opts.isEnable()).put("reason", opts.getReason());
    requestVoid(HttpMethod.PUT, "/v1/agent/service/maintenance/" + urlEncode(opts.getId()), query, null, promise);
    return promise.future();
  }

  @Override
  public Future<Void> deregisterService(String id) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/agent/service/deregister/" + urlEncode(id), null, null, promise);
    return promise.future();
  }

  @Override
  public Future<ServiceList> catalogServiceNodes(String service) {
    return catalogServiceNodesWithOptions(service, null);
  }

  @Override
  public Future<ServiceList> catalogServiceNodesWithOptions(String service, ServiceQueryOptions options) {
    Promise<ServiceList> promise = Promise.promise();
    Query query = options == null ? null : Query.of("tag", options.getTag()).put("near", options.getNear()).put(options.getBlockingOptions());
    requestArray(HttpMethod.GET, "/v1/catalog/service/" + urlEncode(service), query, null, promise, (arr, headers) -> {
      List<Service> list = arr.stream().map(obj -> new Service((JsonObject) obj)).collect(Collectors.toList());
      return new ServiceList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<List<String>> catalogDatacenters() {
    Promise<List<String>> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/catalog/datacenters", null, null, promise, (arr, headers) -> listOf(arr));
    return promise.future();
  }

  @Override
  public Future<NodeList> catalogNodes() {
    return catalogNodesWithOptions(null);
  }

  @Override
  public Future<NodeList> catalogNodesWithOptions(NodeQueryOptions options) {
    Promise<NodeList> promise = Promise.promise();
    Query query = options == null ? null : Query.of("near", options.getNear()).put(options.getBlockingOptions());
    requestArray(HttpMethod.GET, "/v1/catalog/nodes", query, null, promise, (arr, headers) -> {
      List<Node> list = arr.stream().map(obj -> NodeParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new NodeList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<CheckList> healthChecks(String service) {
    return healthChecksWithOptions(service, null);
  }

  @Override
  public Future<CheckList> healthChecksWithOptions(String service, CheckQueryOptions options) {
    Promise<CheckList> promise = Promise.promise();
    Query query = options == null ? null : Query.of("near", options.getNear()).put(options.getBlockingOptions());
    requestArray(HttpMethod.GET, "/v1/health/checks/" + urlEncode(service), query, null, promise, (arr, headers) -> {
      List<Check> list = arr.stream().map(obj -> CheckParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new CheckList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<CheckList> healthState(HealthState healthState) {
    return healthStateWithOptions(healthState, null);
  }

  @Override
  public Future<CheckList> healthStateWithOptions(HealthState healthState, CheckQueryOptions options) {
    Promise<CheckList> promise = Promise.promise();
    Query query = options == null ? null : Query.of("near", options.getNear()).put(options.getBlockingOptions());
    requestArray(HttpMethod.GET, "/v1/health/state/" + healthState.key, query, null, promise, (arr, headers) -> {
      List<Check> list = arr.stream().map(obj -> CheckParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new CheckList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<ServiceEntryList> healthServiceNodes(String service, boolean passing) {
    return healthServiceNodesWithOptions(service ,passing, null);
  }

  @Override
  public Future<ServiceEntryList> healthServiceNodesWithOptions(String service, boolean passing, ServiceQueryOptions options) {
    Promise<ServiceEntryList> promise = Promise.promise();
    Query query = new Query().put("passing", passing ? 1 : null);
    if (options != null) {
      query.put(options.getBlockingOptions()).put("near", options.getNear()).put("tag", options.getTag());
    }
    requestArray(HttpMethod.GET, "/v1/health/service/" + urlEncode(service), query, null, promise, (arr, headers) -> {
      List<ServiceEntry> list = arr.stream().map(obj -> ServiceEntryParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new ServiceEntryList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<ServiceList> catalogServices() {
    return catalogServicesWithOptions(null);
  }

  @Override
  public Future<ServiceList> catalogServicesWithOptions(BlockingQueryOptions options) {
    Promise<ServiceList> promise = Promise.promise();
    requestObject(HttpMethod.GET, "/v1/catalog/services", Query.of(options), null, promise, (json, headers) -> {
      List<Service> list = json.stream().map(ServiceParser::parseCatalogInfo).collect(Collectors.toList());
      return new ServiceList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<List<Check>> localChecks() {
    Promise<List<Check>> promise = Promise.promise();
    requestObject(HttpMethod.GET, "/v1/agent/checks", null, null, promise, (json, headers) -> json.stream()
      .map(obj -> CheckParser.parse((JsonObject) obj.getValue()))
      .collect(Collectors.toList()));
    return promise.future();
  }

  @Override
  public Future<List<Service>> localServices() {
    Promise<List<Service>> promise = Promise.promise();
    requestObject(HttpMethod.GET, "/v1/agent/services", null, null, promise, (json, headers) -> json.stream()
      .map(obj -> ServiceParser.parseAgentInfo((JsonObject) obj.getValue()))
      .collect(Collectors.toList()));
    return promise.future();
  }

  @Override
  public Future<ServiceList> catalogNodeServices(String node) {
    return catalogNodeServicesWithOptions(node, null);
  }

  @Override
  public Future<ServiceList> catalogNodeServicesWithOptions(String node, BlockingQueryOptions options) {
    Promise<ServiceList> promise = Promise.promise();
    requestObject(HttpMethod.GET, "/v1/catalog/node/" + urlEncode(node), Query.of(options), null, promise, (json, headers) -> {
      JsonObject nodeInfo = json.getJsonObject("Node");
      String nodeName = nodeInfo.getString("Node");
      String nodeAddress = nodeInfo.getString("Address");
      List<Service> list = json.getJsonObject("Services").stream()
        .map(obj -> ServiceParser.parseNodeInfo(nodeName, nodeAddress, (JsonObject) obj.getValue()))
        .collect(Collectors.toList());
      return new ServiceList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<Void> registerCheck(CheckOptions checkOptions) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/agent/check/register", null, checkOpts(checkOptions, "ID", true).encode(), promise);
    return promise.future();
  }

  private static JsonObject checkOpts(CheckOptions checkOptions, String checkIdKey, boolean extended) {
    JsonObject json = new JsonObject()
      .put(checkIdKey, checkOptions.getId())
      .put("Name", checkOptions.getName())
      .put("Notes", checkOptions.getNotes())
      .put("ScriptArgs", checkOptions.getScriptArgs())
      .put("HTTP", checkOptions.getHttp())
      .put("Header", checkOptions.getHeaders())
      .put("TLSSkipVerify", checkOptions.isTlsSkipVerify())
      .put("GRPC", checkOptions.getGrpc())
      .put("Interval", checkOptions.getInterval())
      .put("TTL", checkOptions.getTtl())
      .put("TCP", checkOptions.getTcp());
    if (checkOptions.getGrpc() != null) {
      json.put("GRPCUseTLS", checkOptions.isGrpcTls());
    }
    if (checkOptions.getDeregisterAfter() != null) {
      json.put("DeregisterCriticalServiceAfter", checkOptions.getDeregisterAfter());
    }
    if (checkOptions.getStatus() != null) {
      json.put("Status", checkOptions.getStatus().key);
    }
    if (extended && checkOptions.getServiceId() != null) {
      json.put("ServiceID", checkOptions.getServiceId());
    }
    return json;
  }

  private static JsonArray checkListOpts(List<CheckOptions> listChecks, String checkIdKey, boolean extended) {
    JsonArray jsonArray = new JsonArray();
    listChecks.stream().map(c -> checkOpts(c, checkIdKey,extended)).forEach(jsonArray::add);
    return jsonArray;
  }

  @Override
  public Future<Void> deregisterCheck(String checkId) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/agent/check/deregister/" + urlEncode(checkId), null, null, promise);
    return promise.future();
  }

  @Override
  public Future<Void> passCheck(String checkId) {
    return passCheckWithNote(checkId, null);
  }

  @Override
  public Future<Void> passCheckWithNote(String checkId, String note) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/agent/check/pass/" + urlEncode(checkId), Query.of("note", note), null, promise);
    return promise.future();
  }

  @Override
  public Future<Void> warnCheck(String checkId) {
    return warnCheckWithNote(checkId, null);
  }

  @Override
  public Future<Void> warnCheckWithNote(String checkId, String note) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/agent/check/warn/" + urlEncode(checkId), Query.of("note", note), null, promise);
    return promise.future();
  }

  @Override
  public Future<Void> failCheck(String checkId) {
    return failCheckWithNote(checkId, null);
  }

  @Override
  public Future<Void> failCheckWithNote(String checkId, String note) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/agent/check/fail/" + urlEncode(checkId), Query.of("note", note), null, promise);
    return promise.future();
  }

  @Override
  public Future<Void> updateCheck(String checkId, CheckStatus status) {
    return updateCheckWithNote(checkId, status, null);
  }

  @Override
  public Future<Void> updateCheckWithNote(String checkId, CheckStatus status, String note) {
    Promise<Void> promise = Promise.promise();
    JsonObject put = new JsonObject().put("Status", status.key);
    if (note != null) {
      put.put("Output", note);
    }
    requestVoid(HttpMethod.PUT, "/v1/agent/check/update/" + urlEncode(checkId), null, put.encode(), promise);
    return promise.future();
  }

  @Override
  public Future<String> leaderStatus() {
    Promise<String> promise = Promise.promise();
    requestString(HttpMethod.GET, "/v1/status/leader", null, null, promise, (leader, headers) ->
      leader.substring(1, leader.length() - 2));
    return promise.future();
  }

  @Override
  public Future<List<String>> peersStatus() {
    Promise<List<String>> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/status/peers", null, null, promise, (arr, headers) -> arr.stream()
      .map(obj -> (String) obj)
      .collect(Collectors.toList()));
    return promise.future();
  }

  @Override
  public Future<String> createSession() {
    return createSessionWithOptions(null);
  }

  @Override
  public Future<String> createSessionWithOptions(SessionOptions options) {
    Promise<String> promise = Promise.promise();
    String body = options == null ? null : options.toJson().encode();
    requestObject(HttpMethod.PUT, "/v1/session/create", null, body, promise, (obj, headers) -> obj.getString("ID"));
    return promise.future();
  }

  @Override
  public Future<Session> infoSession(String id) {
    return infoSessionWithOptions(id, null);
  }

  @Override
  public Future<Session> infoSessionWithOptions(String id, BlockingQueryOptions options) {
    Promise<Session> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/session/info/" + urlEncode(id), Query.of(options), null, promise, (sessions, headers) -> {
      if (sessions.size() == 0) {
        throw new RuntimeException("Unknown session ID: " + id);
      } else {
        return SessionParser.parse(sessions.getJsonObject(0), Long.parseLong(headers.get(INDEX_HEADER)));
      }
    });
    return promise.future();
  }

  @Override
  public Future<Session> renewSession(String id) {
    Promise<Session> promise = Promise.promise();
    requestArray(HttpMethod.PUT, "/v1/session/renew/" + urlEncode(id), null, null, promise, (arr, headers) ->
      SessionParser.parse(arr.getJsonObject(0)));
    return promise.future();
  }

  @Override
  public Future<SessionList> listSessions() {
    return listSessionsWithOptions(null);
  }

  @Override
  public Future<SessionList> listSessionsWithOptions(BlockingQueryOptions options) {
    Promise<SessionList> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/session/list", Query.of(options), null, promise, (arr, headers) -> {
      List<Session> list = arr.stream().map(obj -> SessionParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new SessionList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<SessionList> listNodeSessions(String nodeId) {
    return listNodeSessionsWithOptions(nodeId, null);
  }

  @Override
  public Future<SessionList> listNodeSessionsWithOptions(String nodeId, BlockingQueryOptions options) {
    Promise<SessionList> promise = Promise.promise();
    requestArray(HttpMethod.GET, "/v1/session/node/" + urlEncode(nodeId), Query.of(options), null, promise, (arr, headers) -> {
      List<Session> list = arr.stream().map(obj -> SessionParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new SessionList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
    return promise.future();
  }

  @Override
  public Future<Void> destroySession(String id) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/session/destroy/" + urlEncode(id), null, null, promise);
    return promise.future();
  }

  @Override
  public Future<String> createPreparedQuery(PreparedQueryDefinition definition) {
    Promise<String> promise = Promise.promise();
    requestObject(HttpMethod.POST, "/v1/query", null, definition.toJson().encode(), promise, (obj, headers) -> obj.getString("ID"));
    return promise.future();
  }

  @Override
  public Future<PreparedQueryDefinition> getPreparedQuery(String id) {
    Promise<PreparedQueryDefinition> promise = Promise.promise();
    getPreparedQueryList(id, h -> promise.handle(h.map(list -> list.get(0))));
    return promise.future();
  }

  @Override
  public Future<List<PreparedQueryDefinition>> getAllPreparedQueries() {
    Promise<List<PreparedQueryDefinition>> promise = Promise.promise();
    getPreparedQueryList(null, promise);
    return promise.future();
  }

  @Override
  public Future<Void> updatePreparedQuery(PreparedQueryDefinition definition) {
    Promise<Void> promise = Promise.promise();
    String path = "/v1/query/" + urlEncode(definition.getId());
    requestVoid(HttpMethod.PUT, path, null, definition.toJson().encode(), promise);
    return promise.future();
  }

  private void getPreparedQueryList(String id, Handler<AsyncResult<List<PreparedQueryDefinition>>> resultHandler) {
    String path = "/v1/query" + (id == null ? "" : "/" + urlEncode(id));
    requestArray(HttpMethod.GET, path, null, null, resultHandler, (arr, headers) -> arr.stream()
      .map(obj -> new PreparedQueryDefinition((JsonObject) obj)).collect(Collectors.toList()));
  }

  @Override
  public Future<Void> deletePreparedQuery(String id) {
    Promise<Void> promise = Promise.promise();
    requestVoid(HttpMethod.DELETE, "/v1/query/" + urlEncode(id), null, null, promise);
    return promise.future();
  }

  @Override
  public Future<PreparedQueryExecuteResponse> executePreparedQuery(String query) {
    return executePreparedQueryWithOptions(query, null);
  }

  @Override
  public Future<PreparedQueryExecuteResponse> executePreparedQueryWithOptions(String query, PreparedQueryExecuteOptions options) {
    Promise<PreparedQueryExecuteResponse> promise = Promise.promise();
    String path = "/v1/query/" + urlEncode(query) + "/execute";
    Query q = new Query();
    if (options != null) {
      q.put("near", options.getNear()).put("limit", options.getLimit());
    }
    requestObject(HttpMethod.GET, path, q, null, promise, (obj, headers) -> {
      return new PreparedQueryExecuteResponse()
        .setService(obj.getString("Service"))
        .setDc(obj.getString("Datacenter"))
        .setFailovers(obj.getInteger("Failovers"))
        .setDnsTtl(obj.getJsonObject("DNS").getString("TTL"))
        .setNodes(obj.getJsonArray("Nodes").stream()
          .map(o -> ServiceEntryParser.parse((JsonObject) o))
          .collect(Collectors.toList()));
    });
    return promise.future();
  }

  @Override
  public Future<Void> registerCatalogService(Node nodeOptions, ServiceOptions serviceOptions) {
    JsonObject nodeJsonOpts = new JsonObject()
      .put("Node", nodeOptions.getName())
      .put("Address", nodeOptions.getAddress());
    if(notEmptyString(nodeOptions.getId())) {
      nodeJsonOpts.put("ID", nodeOptions.getId());
    }

    Map<String, String> taggedAddresses = new HashMap<>();
    String lanAddress = nodeOptions.getLanAddress();
    if (lanAddress != null && !lanAddress.isEmpty()) {
      taggedAddresses.put("lan", lanAddress);
    }

    String wanAddress = nodeOptions.getWanAddress();
    if (wanAddress != null && !wanAddress.isEmpty()) {
      taggedAddresses.put("wan", wanAddress);
    }

    if (!taggedAddresses.isEmpty()) {
      nodeJsonOpts.put("TaggedAddresses", taggedAddresses);
    }

    if(notEmptyString(nodeOptions.getDatacenter())) {
      nodeJsonOpts.put("Datacenter", nodeOptions.getDatacenter());
    }
    if(nodeOptions.getNodeMeta() != null && !nodeOptions.getNodeMeta().isEmpty())
      nodeJsonOpts.put("NodeMeta", nodeOptions.getNodeMeta());

    if(serviceOptions != null) {
      JsonObject serviceJsonOpts = new JsonObject()
        .put("ID", serviceOptions.getId())
        .put("Service", serviceOptions.getName())
        .put("Tags", serviceOptions.getTags())
        .put("Address", serviceOptions.getAddress())
        .put("Port", serviceOptions.getPort())
        .put("Meta", serviceOptions.getMeta());

      nodeJsonOpts.put("Service", serviceJsonOpts);
    }

    Map<String, Object> map = nodeJsonOpts.getMap();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() == null) {
        map.remove(entry.getKey());
      }
    }

    Promise<Void> p = Promise.promise();
    requestVoid(HttpMethod.PUT, "/v1/catalog/register", null, nodeJsonOpts.encode(), p);
    return p.future();
  }

  private boolean notEmptyString(String str) {
    return str != null && !str.isEmpty();
  }

  @Override
  public Future<Void> deregisterCatalogService(String nodeId, String serviceId) {
    Promise<Void> p = Promise.promise();
    JsonObject jsonOpts = new JsonObject()
      .put("Node", nodeId)
      .put("ServiceID", serviceId);
    requestVoid(HttpMethod.PUT, "/v1/catalog/deregister", null, jsonOpts.encode(), p);
    return p.future();
  }

  @Override
  public void close() {
    webClient.close();
  }

  private <T> void requestArray(HttpMethod method, String path, Query query, String body,
                                Handler<AsyncResult<T>> resultHandler,
                                BiFunction<JsonArray, MultiMap, T> mapper) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> mapper.apply(resp.bodyAsJsonArray(), resp.headers()));
  }

  private <T> void requestObject(HttpMethod method, String path, Query query, String body,
                                 Handler<AsyncResult<T>> resultHandler,
                                 BiFunction<JsonObject, MultiMap, T> mapper) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> mapper.apply(resp.bodyAsJsonObject(), resp.headers()));
  }

  private <T> void requestString(HttpMethod method, String path, Query query, String body,
                                 Handler<AsyncResult<T>> resultHandler,
                                 BiFunction<String, MultiMap, T> mapper) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> mapper.apply(resp.bodyAsString().trim(), resp.headers()));
  }

  private <T> void requestVoid(HttpMethod method, String path, Query query, String body,
                               Handler<AsyncResult<T>> resultHandler) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> null);
  }

  private <T> void request(List<Integer> validCodes, HttpMethod method, String path, Query query, String body,
                           Handler<AsyncResult<T>> resultHandler,
                           Function<HttpResponse<Buffer>, T> mapper) {
    if (Vertx.currentContext() == ctx) {
      reqOnContext(validCodes, method, path, query, body, resultHandler, mapper);
    } else {
      ctx.runOnContext(v -> reqOnContext(validCodes, method, path, query, body, resultHandler, mapper));
    }
  }

  private <T> void reqOnContext(List<Integer> validCodes, HttpMethod method, String path, Query query, String body,
                                Handler<AsyncResult<T>> resultHandler,
                                Function<HttpResponse<Buffer>, T> mapper) {
    if (query == null) {
      query = new Query();
    }
    if (dc != null) {
      query.put("dc", dc);
    }
    HttpRequest<Buffer> rq = webClient.request(method, path);
    query.entrySet().forEach(e -> rq.addQueryParam(e.getKey(), e.getValue()));
    if (aclToken != null) {
      rq.putHeader(TOKEN_HEADER, aclToken);
    }
    if (timeoutMs > 0) {
      rq.timeout(timeoutMs);
    }
    try {
      rq.sendBuffer(body == null ? Buffer.buffer() : Buffer.buffer(body))
        .map(resp -> {
          if (validCodes.contains(resp.statusCode())) {
            return mapper.apply(resp);
          } else {
            throw new VertxException(String.format("Status message: '%s'. Body: '%s' ", resp.statusMessage(), resp.bodyAsString()), true);
          }
        }).onComplete(resultHandler);
    } catch (final IllegalStateException e) {
      resultHandler.handle(Future.failedFuture(e));
    }
  }
}
