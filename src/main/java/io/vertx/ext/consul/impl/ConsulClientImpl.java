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
import io.vertx.ext.consul.policy.AclPolicy;
import io.vertx.ext.consul.token.CloneAclTokenOptions;
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
  private final String aclToken;
  private final String dc;
  private final long timeoutMs;

  public ConsulClientImpl(Vertx vertx, ConsulClientOptions options) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(options);
    webClient = WebClient.create(vertx, options);
    aclToken = options.getAclToken();
    dc = options.getDc();
    timeoutMs = options.getTimeout();
  }

  @Override
  public Future<JsonObject> agentInfo() {
    return requestObject(HttpMethod.GET, "/v1/agent/self", null, null, (obj, headers) -> obj);
  }

  @Override
  public Future<CoordinateList> coordinateNodes() {
    return coordinateNodesWithOptions(null);
  }

  @Override
  public Future<CoordinateList> coordinateNodesWithOptions(BlockingQueryOptions options) {
    return requestArray(HttpMethod.GET, "/v1/coordinate/nodes", new Query().put(options), null, (arr, headers) -> {
      List<Coordinate> list = arr.stream().map(obj -> CoordinateParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new CoordinateList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<List<DcCoordinates>> coordinateDatacenters() {
    return requestArray(HttpMethod.GET, "/v1/coordinate/datacenters", null, null, (arr, headers) ->
      arr.stream().map(obj -> CoordinateParser.parseDc((JsonObject) obj)).collect(Collectors.toList()));
  }

  @Override
  public Future<List<String>> getKeys(String keyPrefix) {
    return getKeysWithOptions(keyPrefix, null);
  }

  @Override
  public Future<List<String>> getKeysWithOptions(String keyPrefix, BlockingQueryOptions options) {
    Query query = Query.of("recurse", true).put("keys", true).put(options);
    return request(KV_VALID_CODES, HttpMethod.GET, "/v1/kv/" + urlEncode(keyPrefix), query, null, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new ArrayList<>();
      } else {
        return resp.bodyAsJsonArray().stream().map(Object::toString).collect(Collectors.toList());
      }
    });
  }

  @Override
  public Future<KeyValue> getValue(String key) {
    return getValueWithOptions(key, null);
  }

  @Override
  public Future<KeyValue> getValueWithOptions(String key, BlockingQueryOptions options) {
    return request(KV_VALID_CODES, HttpMethod.GET, "/v1/kv/" + urlEncode(key), new Query().put(options), null, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new KeyValue();
      } else {
        return KVParser.parse(resp.bodyAsJsonArray().getJsonObject(0));
      }
    });
  }

  @Override
  public Future<Void> deleteValue(String key) {
    return requestVoid(HttpMethod.DELETE, "/v1/kv/" + urlEncode(key), null, null);
  }

  @Override
  public Future<KeyValueList> getValues(String keyPrefix) {
    return getValuesWithOptions(keyPrefix, null);
  }

  @Override
  public Future<KeyValueList> getValuesWithOptions(String keyPrefix, BlockingQueryOptions options) {
    Query query = Query.of("recurse", true).put(options);
    return request(KV_VALID_CODES, HttpMethod.GET, "/v1/kv/" + urlEncode(keyPrefix), query, null, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new KeyValueList();
      } else {
        List<KeyValue> list = resp.bodyAsJsonArray().stream().map(obj -> KVParser.parse((JsonObject) obj)).collect(Collectors.toList());
        return new KeyValueList().setList(list).setIndex(Long.parseLong(resp.headers().get(INDEX_HEADER)));
      }
    });
  }

  @Override
  public Future<Void> deleteValues(String keyPrefix) {
    return requestVoid(HttpMethod.DELETE, "/v1/kv/" + urlEncode(keyPrefix), Query.of("recurse", true), null);
  }

  @Override
  public Future<Boolean> putValue(String key, String value) {
    return putValueWithOptions(key, value, null);
  }

  @Override
  public Future<Boolean> putValueWithOptions(String key, String value, KeyValueOptions options) {
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
    return requestString(HttpMethod.PUT, "/v1/kv/" + urlEncode(key), query, value, (bool, headers) -> Boolean.valueOf(bool));
  }

  @Override
  public Future<TxnResponse> transaction(TxnRequest request) {
    String boby = request.toJson().getJsonArray("operations").encode();
    return request(TXN_VALID_CODES, HttpMethod.PUT, "/v1/txn", null, boby, resp -> TxnResponseParser.parse(resp.bodyAsJsonObject()));
  }

  @Override
  public Future<String> createAclPolicy(AclPolicy policy) {
    if (policy.getRules() == null) {
      return Future.failedFuture(new RuntimeException("Missing required request parameter: 'rules'"));
    }
    if (policy.getName() == null) {
      return Future.failedFuture(new RuntimeException("Missing required request parameter: 'name'"));
    }
    return requestObject(HttpMethod.PUT, "/v1/acl/policy", null, policy.toJson().encode(), (obj, headers) ->
      obj.getString("ID")
    );
  }

  @Override
  public Future<AclPolicy> readPolicy(String id) {
    return requestObject(HttpMethod.GET, "/v1/acl/policy/" + urlEncode(id), null, null, (obj, headers) ->
      new AclPolicy(obj)
    );
  }

  @Override
  public Future<AclPolicy> readPolicyByName(String name) {
    return requestObject(HttpMethod.GET, "/v1/acl/policy/name/" + urlEncode(name), null, null, (obj, headers) ->
      new AclPolicy(obj)
    );
  }

  @Override
  public Future<AclPolicy> updatePolicy(String id, AclPolicy policy) {
    return requestObject(HttpMethod.PUT, "/v1/acl/policy/" + urlEncode(id), null, policy.toJson().encode(), (obj, headers) ->
      new AclPolicy(obj)
    );
  }

  @Override
  public Future<Boolean> deletePolicy(String id) {
    return requestString(HttpMethod.DELETE, "/v1/acl/policy/" + urlEncode(id), null, null, (str, headers) ->
      Boolean.parseBoolean(str)
    );
  }

  @Override
  public Future<List<AclPolicy>> getAclPolicies() {
    return requestArray(HttpMethod.GET, "/v1/acl/policies", null, null, (array, header) ->
      array.stream()
        .map(obj -> new AclPolicy((JsonObject) obj))
        .collect(Collectors.toList())
    );
  }

  public Future<io.vertx.ext.consul.token.AclToken> createAclToken(io.vertx.ext.consul.token.AclToken token) {
    return requestObject(HttpMethod.PUT, "/v1/acl/token", null, token.toJson().encode(), (obj, headers) ->
      new io.vertx.ext.consul.token.AclToken(obj)
    );
  }

  @Override
  public Future<io.vertx.ext.consul.token.AclToken> updateAclToken(String accessorId, io.vertx.ext.consul.token.AclToken token) {
    return requestObject(HttpMethod.PUT, "/v1/acl/token/" + urlEncode(accessorId), null, token.toJson().encode(),
      (obj, headers) -> new io.vertx.ext.consul.token.AclToken(obj)
    );
  }

  @Override
  public Future<io.vertx.ext.consul.token.AclToken> cloneAclToken(String accessorId, CloneAclTokenOptions cloneAclTokenOptions) {
    return requestObject(HttpMethod.PUT, "/v1/acl/token/" + urlEncode(accessorId) + "/clone", null,
      cloneAclTokenOptions.toJson().encode(),
      (obj, headers) -> new io.vertx.ext.consul.token.AclToken(obj)
    );
  }

  @Override
  public Future<List<io.vertx.ext.consul.token.AclToken>> getAclTokens() {
    return requestArray(HttpMethod.GET, "/v1/acl/tokens", null, null, (arr, headers) ->
      arr.stream()
        .map(obj -> new io.vertx.ext.consul.token.AclToken((JsonObject) obj))
        .collect(Collectors.toList()));
  }

  @Override
  public Future<io.vertx.ext.consul.token.AclToken> readAclToken(String accessorId) {
    return requestObject(HttpMethod.GET, "/v1/acl/token/" + urlEncode(accessorId), null, null,
      (obj, headers) -> new io.vertx.ext.consul.token.AclToken(obj)
    );
  }

  @Override
  public Future<Boolean> deleteAclToken(String accessorId) {
    return requestString(HttpMethod.DELETE, "/v1/acl/token/" + urlEncode(accessorId), null, null,
      (str, headers) -> Boolean.parseBoolean(str)
    );
  }

  @Override
  public Future<String> createAclToken(AclToken token) {
    return requestObject(HttpMethod.PUT, "/v1/acl/create", null, token.toJson().encode(), (obj, headers) ->
      obj.getString("ID"));
  }

  @Override
  public Future<String> updateAclToken(AclToken token) {
    return requestObject(HttpMethod.PUT, "/v1/acl/update", null, token.toJson().encode(), (obj, headers) ->
      obj.getString("ID"));
  }

  @Override
  public Future<String> cloneAclToken(String id) {
    return requestObject(HttpMethod.PUT, "/v1/acl/clone/" + urlEncode(id), null, null, (obj, headers) ->
      obj.getString("ID"));
  }

  @Override
  public Future<List<AclToken>> listAclTokens() {
    return requestArray(HttpMethod.GET, "/v1/acl/list", null, null, (arr, headers) ->
      arr.stream()
        .map(obj -> new AclToken((JsonObject) obj))
        .collect(Collectors.toList()));
  }

  @Override
  public Future<AclToken> infoAclToken(String id) {
    return requestArray(HttpMethod.GET, "/v1/acl/info/" + urlEncode(id), null, null, (arr, headers) -> {
      JsonObject jsonObject = arr.getJsonObject(0);
      return new AclToken(jsonObject);
    });
  }

  @Override
  public Future<Void> destroyAclToken(String id) {
    return requestVoid(HttpMethod.PUT, "/v1/acl/destroy/" + urlEncode(id), null, null);
  }

  @Override
  public Future<Event> fireEvent(String name) {
    return fireEventWithOptions(name, null);
  }

  @Override
  public Future<Event> fireEventWithOptions(String name, EventOptions options) {
    Query query = new Query();
    if (options != null) {
      query.put("node", options.getNode()).put("service", options.getService()).put("tag", options.getTag());
    }
    String body = options == null || options.getPayload() == null ? "" : options.getPayload();
    return requestObject(
      HttpMethod.PUT,
      "/v1/event/fire/" + urlEncode(name),
      query,
      body,
      (jsonObject, headers) -> EventParser.parse(jsonObject)
    );
  }

  @Override
  public Future<EventList> listEvents() {
    return listEventsWithOptions(null);
  }

  @Override
  public Future<EventList> listEventsWithOptions(EventListOptions options) {
    Query query = options == null ? null : Query.of(options.getBlockingOptions()).put("name", options.getName());
    return requestArray(HttpMethod.GET, "/v1/event/list", query, null, (jsonArray, headers) -> {
      List<Event> list = jsonArray
        .stream()
        .map(obj -> EventParser.parse(((JsonObject) obj)))
        .collect(Collectors.toList());
      return new EventList().setList(list).setIndex(Long.parseUnsignedLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<Void> registerService(ServiceOptions serviceOptions) {
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
    if (serviceOptions.getConnectOptions() != null) {
      jsonOpts.put("Connect", serviceOptions.getConnectOptions().toJson());
    }
    return requestVoid(HttpMethod.PUT, "/v1/agent/service/register", null, jsonOpts.encode());
  }

  @Override
  public Future<Void> maintenanceService(MaintenanceOptions opts) {
    Query query = Query.of("enable", opts.isEnable()).put("reason", opts.getReason());
    return requestVoid(HttpMethod.PUT, "/v1/agent/service/maintenance/" + urlEncode(opts.getId()), query, null);
  }

  @Override
  public Future<Void> deregisterService(String id) {
    return requestVoid(HttpMethod.PUT, "/v1/agent/service/deregister/" + urlEncode(id), null, null);
  }

  @Override
  public Future<ServiceList> catalogServiceNodes(String service) {
    return catalogServiceNodesWithOptions(service, null);
  }

  @Override
  public Future<ServiceList> catalogServiceNodesWithOptions(String service, ServiceQueryOptions options) {
    Query query = options == null ? null : Query
      .of("tag", options.getTag())
      .put("near", options.getNear())
      .put(options.getBlockingOptions());
    return requestArray(HttpMethod.GET, "/v1/catalog/service/" + urlEncode(service), query, null, (arr, headers) -> {
      List<Service> list = arr.stream().map(obj -> new Service((JsonObject) obj)).collect(Collectors.toList());
      return new ServiceList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<List<String>> catalogDatacenters() {
    return requestArray(HttpMethod.GET, "/v1/catalog/datacenters", null, null, (arr, headers) -> listOf(arr));
  }

  @Override
  public Future<NodeList> catalogNodes() {
    return catalogNodesWithOptions(null);
  }

  @Override
  public Future<NodeList> catalogNodesWithOptions(NodeQueryOptions options) {
    Query query = options == null ? null : Query.of("near", options.getNear()).put(options.getBlockingOptions());
    return requestArray(HttpMethod.GET, "/v1/catalog/nodes", query, null, (arr, headers) -> {
      List<Node> list = arr.stream().map(obj -> NodeParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new NodeList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<CheckList> healthChecks(String service) {
    return healthChecksWithOptions(service, null);
  }

  @Override
  public Future<CheckList> healthChecksWithOptions(String service, CheckQueryOptions options) {
    Query query = options == null ? null : Query.of("near", options.getNear()).put(options.getBlockingOptions());
    return requestArray(HttpMethod.GET, "/v1/health/checks/" + urlEncode(service), query, null, (arr, headers) -> {
      List<Check> list = arr.stream().map(obj -> CheckParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new CheckList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<CheckList> healthState(HealthState healthState) {
    return healthStateWithOptions(healthState, null);
  }

  @Override
  public Future<CheckList> healthStateWithOptions(HealthState healthState, CheckQueryOptions options) {
    Query query = options == null ? null : Query.of("near", options.getNear()).put(options.getBlockingOptions());
    return requestArray(HttpMethod.GET, "/v1/health/state/" + healthState.key, query, null, (arr, headers) -> {
      List<Check> list = arr.stream().map(obj -> CheckParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new CheckList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<ServiceEntryList> healthServiceNodes(String service, boolean passing) {
    return healthServiceNodesWithOptions(service, passing, null);
  }

  @Override
  public Future<ServiceEntryList> healthServiceNodesWithOptions(
    String service,
    boolean passing,
    ServiceQueryOptions options
  ) {
    Query query = new Query().put("passing", passing ? 1 : null);
    if (options != null) {
      query.put(options.getBlockingOptions()).put("near", options.getNear()).put("tag", options.getTag());
    }
    return requestArray(HttpMethod.GET, "/v1/health/service/" + urlEncode(service), query, null, (arr, headers) -> {
      List<ServiceEntry> list = arr
        .stream()
        .map(obj -> ServiceEntryParser.parse((JsonObject) obj))
        .collect(Collectors.toList());
      return new ServiceEntryList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<CheckList> healthNodesWithOptions(String node, CheckQueryOptions options) {
    Query query = new Query().put("dc", options.getDc());
    if (options.getBlockingOptions() != null) {
      query.put(options.getBlockingOptions());
    }
    if (options.getDc() != null && !options.getDc().isEmpty()) {
      query.put("dc", options.getDc());
    }
    return requestArray(HttpMethod.GET, "/v1/health/node/" + urlEncode(node), query,
      options.toJson().encode(),
      (arr, headers) -> {
        List<Check> list = arr.stream().map(obj -> CheckParser.parse((JsonObject) obj)).collect(Collectors.toList());
        return new CheckList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
      }
    );
  }

  @Override
  public Future<ServiceList> catalogServices() {
    return catalogServicesWithOptions(null);
  }

  @Override
  public Future<ServiceList> catalogServicesWithOptions(BlockingQueryOptions options) {
    return requestObject(HttpMethod.GET, "/v1/catalog/services", Query.of(options), null, (json, headers) -> {
      List<Service> list = json.stream().map(ServiceParser::parseCatalogInfo).collect(Collectors.toList());
      return new ServiceList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<List<Check>> localChecks() {
    return requestObject(HttpMethod.GET, "/v1/agent/checks", null, null, (json, headers) -> json.stream()
      .map(obj -> CheckParser.parse((JsonObject) obj.getValue()))
      .collect(Collectors.toList()));
  }

  @Override
  public Future<List<Service>> localServices() {
    return requestObject(HttpMethod.GET, "/v1/agent/services", null, null, (json, headers) -> json.stream()
      .map(obj -> ServiceParser.parseAgentInfo((JsonObject) obj.getValue()))
      .collect(Collectors.toList()));
  }

  @Override
  public Future<ServiceList> catalogNodeServices(String node) {
    return catalogNodeServicesWithOptions(node, null);
  }

  @Override
  public Future<ServiceList> catalogNodeServicesWithOptions(String node, BlockingQueryOptions options) {
    return requestObject(
      HttpMethod.GET,
      "/v1/catalog/node/" + urlEncode(node),
      Query.of(options),
      null,
      (json, headers) -> {
        JsonObject nodeInfo = json.getJsonObject("Node");
        String nodeName = nodeInfo.getString("Node");
        String nodeAddress = nodeInfo.getString("Address");
        List<Service> list = json.getJsonObject("Services").stream()
          .map(obj -> ServiceParser.parseNodeInfo(nodeName, nodeAddress, (JsonObject) obj.getValue()))
          .collect(Collectors.toList());
        return new ServiceList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
      }
    );
  }

  @Override
  public Future<Void> registerCheck(CheckOptions checkOptions) {
    return requestVoid(HttpMethod.PUT, "/v1/agent/check/register", null, checkOpts(checkOptions, "ID", true).encode());
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
    listChecks.stream().map(c -> checkOpts(c, checkIdKey, extended)).forEach(jsonArray::add);
    return jsonArray;
  }

  @Override
  public Future<Void> deregisterCheck(String checkId) {
    return requestVoid(HttpMethod.PUT, "/v1/agent/check/deregister/" + urlEncode(checkId), null, null);
  }

  @Override
  public Future<Void> passCheck(String checkId) {
    return passCheckWithNote(checkId, null);
  }

  @Override
  public Future<Void> passCheckWithNote(String checkId, String note) {
    return requestVoid(HttpMethod.PUT, "/v1/agent/check/pass/" + urlEncode(checkId), Query.of("note", note), null);
  }

  @Override
  public Future<Void> warnCheck(String checkId) {
    return warnCheckWithNote(checkId, null);
  }

  @Override
  public Future<Void> warnCheckWithNote(String checkId, String note) {
    return requestVoid(HttpMethod.PUT, "/v1/agent/check/warn/" + urlEncode(checkId), Query.of("note", note), null);
  }

  @Override
  public Future<Void> failCheck(String checkId) {
    return failCheckWithNote(checkId, null);
  }

  @Override
  public Future<Void> failCheckWithNote(String checkId, String note) {
    return requestVoid(HttpMethod.PUT, "/v1/agent/check/fail/" + urlEncode(checkId), Query.of("note", note), null);
  }

  @Override
  public Future<Void> updateCheck(String checkId, CheckStatus status) {
    return updateCheckWithNote(checkId, status, null);
  }

  @Override
  public Future<Void> updateCheckWithNote(String checkId, CheckStatus status, String note) {
    JsonObject put = new JsonObject().put("Status", status.key);
    if (note != null) {
      put.put("Output", note);
    }
    return requestVoid(HttpMethod.PUT, "/v1/agent/check/update/" + urlEncode(checkId), null, put.encode());
  }

  @Override
  public Future<String> leaderStatus() {
    return requestString(HttpMethod.GET, "/v1/status/leader", null, null, (leader, headers) ->
      leader.substring(1, leader.length() - 2));
  }

  @Override
  public Future<List<String>> peersStatus() {
    return requestArray(HttpMethod.GET, "/v1/status/peers", null, null, (arr, headers) -> arr.stream()
      .map(obj -> (String) obj)
      .collect(Collectors.toList()));
  }

  @Override
  public Future<String> createSession() {
    return createSessionWithOptions(null);
  }

  @Override
  public Future<String> createSessionWithOptions(SessionOptions options) {
    String body = options == null ? null : options.toJson().encode();
    return requestObject(HttpMethod.PUT, "/v1/session/create", null, body, (obj, headers) -> obj.getString("ID"));
  }

  @Override
  public Future<Session> infoSession(String id) {
    return infoSessionWithOptions(id, null);
  }

  @Override
  public Future<Session> infoSessionWithOptions(String id, BlockingQueryOptions options) {
    return requestArray(
      HttpMethod.GET,
      "/v1/session/info/" + urlEncode(id),
      Query.of(options),
      null,
      (sessions, headers) -> {
        if (sessions.size() == 0) {
          throw new RuntimeException("Unknown session ID: " + id);
        } else {
          return SessionParser.parse(sessions.getJsonObject(0), Long.parseLong(headers.get(INDEX_HEADER)));
        }
      }
    );
  }

  @Override
  public Future<Session> renewSession(String id) {
    return requestArray(HttpMethod.PUT, "/v1/session/renew/" + urlEncode(id), null, null, (arr, headers) ->
      SessionParser.parse(arr.getJsonObject(0)));
  }

  @Override
  public Future<SessionList> listSessions() {
    return listSessionsWithOptions(null);
  }

  @Override
  public Future<SessionList> listSessionsWithOptions(BlockingQueryOptions options) {
    return requestArray(HttpMethod.GET, "/v1/session/list", Query.of(options), null, (arr, headers) -> {
      List<Session> list = arr.stream().map(obj -> SessionParser.parse((JsonObject) obj)).collect(Collectors.toList());
      return new SessionList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
    });
  }

  @Override
  public Future<SessionList> listNodeSessions(String nodeId) {
    return listNodeSessionsWithOptions(nodeId, null);
  }

  @Override
  public Future<SessionList> listNodeSessionsWithOptions(String nodeId, BlockingQueryOptions options) {
    return requestArray(
      HttpMethod.GET,
      "/v1/session/node/" + urlEncode(nodeId),
      Query.of(options),
      null,
      (arr, headers) -> {
        List<Session> list = arr
          .stream()
          .map(obj -> SessionParser.parse((JsonObject) obj))
          .collect(Collectors.toList());
        return new SessionList().setList(list).setIndex(Long.parseLong(headers.get(INDEX_HEADER)));
      }
    );
  }

  @Override
  public Future<Void> destroySession(String id) {
    return requestVoid(HttpMethod.PUT, "/v1/session/destroy/" + urlEncode(id), null, null);
  }

  @Override
  public Future<String> createPreparedQuery(PreparedQueryDefinition definition) {
    return requestObject(
      HttpMethod.POST,
      "/v1/query",
      null,
      definition.toJson().encode(),
      (obj, headers) -> obj.getString("ID")
    );
  }

  @Override
  public Future<PreparedQueryDefinition> getPreparedQuery(String id) {
    return getPreparedQueryList(id).map(list -> list.get(0));
  }

  @Override
  public Future<List<PreparedQueryDefinition>> getAllPreparedQueries() {
    return getPreparedQueryList(null);
  }

  @Override
  public Future<Void> updatePreparedQuery(PreparedQueryDefinition definition) {
    String path = "/v1/query/" + urlEncode(definition.getId());
    return requestVoid(HttpMethod.PUT, path, null, definition.toJson().encode());
  }

  private Future<List<PreparedQueryDefinition>> getPreparedQueryList(String id) {
    String path = "/v1/query" + (id == null ? "" : "/" + urlEncode(id));
    return requestArray(HttpMethod.GET, path, null, null, (arr, headers) -> arr.stream()
      .map(obj -> new PreparedQueryDefinition((JsonObject) obj)).collect(Collectors.toList()));
  }

  @Override
  public Future<Void> deletePreparedQuery(String id) {
    return requestVoid(HttpMethod.DELETE, "/v1/query/" + urlEncode(id), null, null);
  }

  @Override
  public Future<PreparedQueryExecuteResponse> executePreparedQuery(String query) {
    return executePreparedQueryWithOptions(query, null);
  }

  @Override
  public Future<PreparedQueryExecuteResponse> executePreparedQueryWithOptions(
    String query,
    PreparedQueryExecuteOptions options
  ) {
    String path = "/v1/query/" + urlEncode(query) + "/execute";
    Query q = new Query();
    if (options != null) {
      q.put("near", options.getNear()).put("limit", options.getLimit());
    }
    return requestObject(HttpMethod.GET, path, q, null, (obj, headers) -> {
      return new PreparedQueryExecuteResponse()
        .setService(obj.getString("Service"))
        .setDc(obj.getString("Datacenter"))
        .setFailovers(obj.getInteger("Failovers"))
        .setDnsTtl(obj.getJsonObject("DNS").getString("TTL"))
        .setNodes(obj.getJsonArray("Nodes").stream()
          .map(o -> ServiceEntryParser.parse((JsonObject) o))
          .collect(Collectors.toList()));
    });
  }

  @Override
  public Future<Void> registerCatalogService(Node nodeOptions, ServiceOptions serviceOptions) {
    JsonObject nodeJsonOpts = new JsonObject()
      .put("Node", nodeOptions.getName())
      .put("Address", nodeOptions.getAddress());
    if (notEmptyString(nodeOptions.getId())) {
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

    if (notEmptyString(nodeOptions.getDatacenter())) {
      nodeJsonOpts.put("Datacenter", nodeOptions.getDatacenter());
    }
    if (nodeOptions.getNodeMeta() != null && !nodeOptions.getNodeMeta().isEmpty())
      nodeJsonOpts.put("NodeMeta", nodeOptions.getNodeMeta());

    if (serviceOptions != null) {
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
        nodeJsonOpts.remove(entry.getKey());
      }
    }

    return requestVoid(HttpMethod.PUT, "/v1/catalog/register", null, nodeJsonOpts.encode());
  }

  private boolean notEmptyString(String str) {
    return str != null && !str.isEmpty();
  }

  @Override
  public Future<Void> deregisterCatalogService(String nodeId, String serviceId) {
    JsonObject jsonOpts = new JsonObject()
      .put("Node", nodeId)
      .put("ServiceID", serviceId);
    return requestVoid(HttpMethod.PUT, "/v1/catalog/deregister", null, jsonOpts.encode());
  }

  @Override
  public void close() {
    webClient.close();
  }

  private <T> Future<T> requestArray(
    HttpMethod method, String path, Query query, String body,
    BiFunction<JsonArray, MultiMap, T> mapper
  ) {
    return request(
      DEFAULT_VALID_CODES,
      method,
      path,
      query,
      body,
      resp -> mapper.apply(resp.bodyAsJsonArray(), resp.headers())
    );
  }

  private <T> Future<T> requestObject(
    HttpMethod method, String path, Query query, String body,
    BiFunction<JsonObject, MultiMap, T> mapper
  ) {
    return request(
      DEFAULT_VALID_CODES,
      method,
      path,
      query,
      body,
      resp -> mapper.apply(resp.bodyAsJsonObject(), resp.headers())
    );
  }

  private <T> Future<T> requestString(
    HttpMethod method, String path, Query query, String body,
    BiFunction<String, MultiMap, T> mapper
  ) {
    return request(
      DEFAULT_VALID_CODES,
      method,
      path,
      query,
      body,
      resp -> mapper.apply(resp.bodyAsString().trim(), resp.headers())
    );
  }

  private <T> Future<T> requestVoid(HttpMethod method, String path, Query query, String body) {
    return request(DEFAULT_VALID_CODES, method, path, query, body, resp -> null);
  }

  private <T> Future<T> request(
    List<Integer> validCodes, HttpMethod method, String path, Query query, String body,
    Function<HttpResponse<Buffer>, T> mapper
  ) {
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
    return rq.sendBuffer(body == null ? Buffer.buffer() : Buffer.buffer(body))
      .map(resp -> {
        if (validCodes.contains(resp.statusCode())) {
          return mapper.apply(resp);
        } else {
          throw new VertxException(String.format(
            "Status message: '%s'. Body: '%s' ",
            resp.statusMessage(),
            resp.bodyAsString()
          ), true);
        }
      });
  }
}
