package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.*;

import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulServiceImpl implements ConsulService {

  private final ConsulClient consulClient;

  public ConsulServiceImpl(ConsulClient consulClient) {
    this.consulClient = consulClient;
  }

  @Override
  public ConsulService agentInfo(Handler<AsyncResult<JsonObject>> resultHandler) {
    consulClient.agentInfo(resultHandler);
    return this;
  }

  @Override
  public ConsulService coordinateNodes(Handler<AsyncResult<CoordinateList>> resultHandler) {
    consulClient.coordinateNodes(resultHandler);
    return this;
  }

  @Override
  public ConsulService coordinateNodesWithOptions(BlockingQueryOptions options, Handler<AsyncResult<CoordinateList>> resultHandler) {
    consulClient.coordinateNodesWithOptions(options, resultHandler);
    return this;
  }

  @Override
  public ConsulService coordinateDatacenters(Handler<AsyncResult<List<DcCoordinates>>> resultHandler) {
    consulClient.coordinateDatacenters(resultHandler);
    return this;
  }

  @Override
  public ConsulService getKeys(String keyPrefix, Handler<AsyncResult<List<String>>> resultHandler) {
    consulClient.getKeys(keyPrefix, resultHandler);
    return this;
  }

  @Override
  public ConsulService getKeysWithOptions(String keyPrefix, BlockingQueryOptions options, Handler<AsyncResult<List<String>>> resultHandler) {
    consulClient.getKeysWithOptions(keyPrefix, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler) {
    consulClient.getValue(key, resultHandler);
    return this;
  }

  @Override
  public ConsulService getValueWithOptions(String key, BlockingQueryOptions options, Handler<AsyncResult<KeyValue>> resultHandler) {
    consulClient.getValueWithOptions(key, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.deleteValue(key, resultHandler);
    return this;
  }

  @Override
  public ConsulService getValues(String keyPrefix, Handler<AsyncResult<KeyValueList>> resultHandler) {
    consulClient.getValues(keyPrefix, resultHandler);
    return this;
  }

  @Override
  public ConsulService getValuesWithOptions(String keyPrefix, BlockingQueryOptions options, Handler<AsyncResult<KeyValueList>> resultHandler) {
    consulClient.getValuesWithOptions(keyPrefix, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.deleteValues(keyPrefix, resultHandler);
    return this;
  }

  @Override
  public ConsulService putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) {
    consulClient.putValue(key, value, resultHandler);
    return this;
  }

  @Override
  public ConsulService putValueWithOptions(String key, String value, KeyValueOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
    consulClient.putValueWithOptions(key, value, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService transaction(TxnRequest request, Handler<AsyncResult<TxnResponse>> resultHandler) {
    consulClient.transaction(request, resultHandler);
    return this;
  }

  @Override
  public ConsulService createAclToken(AclToken aclToken, Handler<AsyncResult<String>> idHandler) {
    consulClient.createAclToken(aclToken, idHandler);
    return this;
  }

  @Override
  public ConsulService updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) {
    consulClient.updateAclToken(token, idHandler);
    return this;
  }

  @Override
  public ConsulService cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) {
    consulClient.cloneAclToken(id, idHandler);
    return this;
  }

  @Override
  public ConsulService listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler) {
    consulClient.listAclTokens(resultHandler);
    return this;
  }

  @Override
  public ConsulService infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) {
    consulClient.infoAclToken(id, tokenHandler);
    return this;
  }

  @Override
  public ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.destroyAclToken(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService fireEvent(String name, Handler<AsyncResult<Event>> resultHandler) {
    consulClient.fireEvent(name, resultHandler);
    return this;
  }

  @Override
  public ConsulService fireEventWithOptions(String name, EventOptions options, Handler<AsyncResult<Event>> resultHandler) {
    consulClient.fireEventWithOptions(name, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService listEvents(Handler<AsyncResult<EventList>> resultHandler) {
    consulClient.listEvents(resultHandler);
    return this;
  }

  @Override
  public ConsulService listEventsWithOptions(EventListOptions options, Handler<AsyncResult<EventList>> resultHandler) {
    consulClient.listEventsWithOptions(options, resultHandler);
    return this;
  }

  @Override
  public ConsulService registerService(ServiceOptions serviceOptions, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.registerService(serviceOptions, resultHandler);
    return this;
  }

  @Override
  public ConsulService maintenanceService(MaintenanceOptions maintenanceOptions, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.maintenanceService(maintenanceOptions, resultHandler);
    return this;
  }

  @Override
  public ConsulService deregisterService(String id, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.deregisterService(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService healthChecks(String service, Handler<AsyncResult<CheckList>> resultHandler) {
    consulClient.healthChecks(service, resultHandler);
    return this;
  }

  @Override
  public ConsulService healthChecksWithOptions(String service, CheckQueryOptions options, Handler<AsyncResult<CheckList>> resultHandler) {
    consulClient.healthChecksWithOptions(service, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService healthState(CheckStatus checkStatus, Handler<AsyncResult<CheckList>> resultHandler) {
    consulClient.healthState(checkStatus, resultHandler);
    return this;
  }

  @Override
  public ConsulService healthStateWithOptions(CheckStatus checkStatus, CheckQueryOptions options, Handler<AsyncResult<CheckList>> resultHandler) {
    consulClient.healthStateWithOptions(checkStatus, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService healthServiceNodes(String service, boolean passing, Handler<AsyncResult<ServiceEntryList>> resultHandler) {
    consulClient.healthServiceNodes(service, passing, resultHandler);
    return this;
  }

  @Override
  public ConsulService healthServiceNodesWithOptions(String service, boolean passing, ServiceQueryOptions options, Handler<AsyncResult<ServiceEntryList>> resultHandler) {
    consulClient.healthServiceNodesWithOptions(service, passing, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogServiceNodes(String service, Handler<AsyncResult<ServiceList>> resultHandler) {
    consulClient.catalogServiceNodes(service, resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogServiceNodesWithOptions(String service, ServiceQueryOptions options, Handler<AsyncResult<ServiceList>> resultHandler) {
    consulClient.catalogServiceNodesWithOptions(service, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler) {
    consulClient.catalogDatacenters(resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogNodes(Handler<AsyncResult<NodeList>> resultHandler) {
    consulClient.catalogNodes(resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogNodesWithOptions(NodeQueryOptions options, Handler<AsyncResult<NodeList>> resultHandler) {
    consulClient.catalogNodesWithOptions(options, resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogServices(Handler<AsyncResult<ServiceList>> resultHandler) {
    consulClient.catalogServices(resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogServicesWithOptions(BlockingQueryOptions options, Handler<AsyncResult<ServiceList>> resultHandler) {
    consulClient.catalogServicesWithOptions(options, resultHandler);
    return this;
  }

  @Override
  public ConsulService localChecks(Handler<AsyncResult<List<Check>>> resultHandler) {
    consulClient.localChecks(resultHandler);
    return this;
  }

  @Override
  public ConsulService registerCheck(CheckOptions checkOptions, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.registerCheck(checkOptions, resultHandler);
    return this;
  }

  @Override
  public ConsulService deregisterCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.deregisterCheck(checkId, resultHandler);
    return this;
  }

  @Override
  public ConsulService passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.passCheck(checkId, resultHandler);
    return this;
  }

  @Override
  public ConsulService passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.passCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  @Override
  public ConsulService warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.warnCheck(checkId, resultHandler);
    return this;
  }

  @Override
  public ConsulService warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.warnCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  @Override
  public ConsulService failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.failCheck(checkId, resultHandler);
    return this;
  }

  @Override
  public ConsulService failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.failCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  @Override
  public ConsulService updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.updateCheck(checkId, status, resultHandler);
    return this;
  }

  @Override
  public ConsulService updateCheckWithNote(String checkId, CheckStatus status, String note, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.updateCheckWithNote(checkId, status, note, resultHandler);
    return this;
  }

  @Override
  public ConsulService leaderStatus(Handler<AsyncResult<String>> resultHandler) {
    consulClient.leaderStatus(resultHandler);
    return this;
  }

  @Override
  public ConsulService peersStatus(Handler<AsyncResult<List<String>>> resultHandler) {
    consulClient.peersStatus(resultHandler);
    return this;
  }

  @Override
  public ConsulService createSession(Handler<AsyncResult<String>> idHandler) {
    consulClient.createSession(idHandler);
    return this;
  }

  @Override
  public ConsulService createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler) {
    consulClient.createSessionWithOptions(options, idHandler);
    return this;
  }

  @Override
  public ConsulService infoSession(String id, Handler<AsyncResult<Session>> resultHandler) {
    consulClient.infoSession(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService infoSessionWithOptions(String id, BlockingQueryOptions options, Handler<AsyncResult<Session>> resultHandler) {
    consulClient.infoSessionWithOptions(id, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService renewSession(String id, Handler<AsyncResult<Session>> resultHandler) {
    consulClient.renewSession(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService listSessions(Handler<AsyncResult<SessionList>> resultHandler) {
    consulClient.listSessions(resultHandler);
    return this;
  }

  @Override
  public ConsulService listSessionsWithOptions(BlockingQueryOptions options, Handler<AsyncResult<SessionList>> resultHandler) {
    consulClient.listSessionsWithOptions(options, resultHandler);
    return this;
  }

  @Override
  public ConsulService listNodeSessions(String nodeId, Handler<AsyncResult<SessionList>> resultHandler) {
    consulClient.listNodeSessions(nodeId, resultHandler);
    return this;
  }

  @Override
  public ConsulService listNodeSessionsWithOptions(String nodeId, BlockingQueryOptions options, Handler<AsyncResult<SessionList>> resultHandler) {
    consulClient.listNodeSessionsWithOptions(nodeId, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService destroySession(String id, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.destroySession(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService createPreparedQuery(PreparedQueryDefinition definition, Handler<AsyncResult<String>> resultHandler) {
    consulClient.createPreparedQuery(definition, resultHandler);
    return this;
  }

  @Override
  public ConsulService getPreparedQuery(String id, Handler<AsyncResult<PreparedQueryDefinition>> resultHandler) {
    consulClient.getPreparedQuery(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService getAllPreparedQueries(Handler<AsyncResult<List<PreparedQueryDefinition>>> resultHandler) {
    consulClient.getAllPreparedQueries(resultHandler);
    return this;
  }

  @Override
  public ConsulService updatePreparedQuery(PreparedQueryDefinition definition, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.updatePreparedQuery(definition, resultHandler);
    return this;
  }

  @Override
  public ConsulService deletePreparedQuery(String id, Handler<AsyncResult<Void>> resultHandler) {
    consulClient.deletePreparedQuery(id, resultHandler);
    return this;
  }

  @Override
  public ConsulService executePreparedQuery(String query, Handler<AsyncResult<PreparedQueryExecuteResponse>> resultHandler) {
    consulClient.executePreparedQuery(query, resultHandler);
    return this;
  }

  @Override
  public ConsulService executePreparedQueryWithOptions(String query, PreparedQueryExecuteOptions options, Handler<AsyncResult<PreparedQueryExecuteResponse>> resultHandler) {
    consulClient.executePreparedQueryWithOptions(query, options, resultHandler);
    return this;
  }

  @Override
  public ConsulService localServices(Handler<AsyncResult<List<Service>>> resultHandler) {
    consulClient.localServices(resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogNodeServices(String node, Handler<AsyncResult<ServiceList>> resultHandler) {
    consulClient.catalogNodeServices(node, resultHandler);
    return this;
  }

  @Override
  public ConsulService catalogNodeServicesWithOptions(String node, BlockingQueryOptions options, Handler<AsyncResult<ServiceList>> resultHandler) {
    consulClient.catalogNodeServicesWithOptions(node, options, resultHandler);
    return this;
  }

  @Override
  public void close() {
    consulClient.close();
  }
}
