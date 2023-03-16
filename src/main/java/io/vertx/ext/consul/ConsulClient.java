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
package io.vertx.ext.consul;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.impl.ConsulClientImpl;

import java.util.List;

/**
 * A Vert.x service used to interact with Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@VertxGen
public interface ConsulClient {

  /**
   * Create a Consul client with default options.
   *
   * @param vertx the Vert.x instance
   * @return the client
   */
  static ConsulClient create(Vertx vertx) {
    return new ConsulClientImpl(vertx, new ConsulClientOptions());
  }

  /**
   * Create a Consul client.
   *
   * @param vertx   the Vert.x instance
   * @param options the options
   * @return the client
   */
  static ConsulClient create(Vertx vertx, ConsulClientOptions options) {
    return new ConsulClientImpl(vertx, options);
  }

  /**
   * Returns the configuration and member information of the local agent
   *
   * @return a future provided with the configuration and member information of the local agent
   * @see <a href="https://www.consul.io/api/agent.html#read-configuration">/v1/agent/self</a> endpoint
   */
  Future<JsonObject> agentInfo();

  /**
   * Returns the LAN network coordinates for all nodes in a given DC
   *
   * @return a future provided with network coordinates of nodes in datacenter
   * @see <a href="https://www.consul.io/api/coordinate.html#read-lan-coordinates">/v1/coordinate/nodes</a> endpoint
   */
  Future<CoordinateList> coordinateNodes();

  /**
   * Returns the LAN network coordinates for all nodes in a given DC
   * This is blocking query unlike {@link ConsulClient#coordinateNodes()}
   *
   * @param options       the blocking options
   * @return a future provided with network coordinates of nodes in datacenter
   * @see <a href="https://www.consul.io/api/coordinate.html#read-lan-coordinates">/v1/coordinate/nodes</a> endpoint
   */
  Future<CoordinateList> coordinateNodesWithOptions(BlockingQueryOptions options);

  /**
   * Returns the WAN network coordinates for all Consul servers, organized by DCs
   *
   * @return a future provided with network coordinates for all Consul servers
   * @see <a href="https://www.consul.io/api/coordinate.html#read-wan-coordinates">/v1/coordinate/datacenters</a> endpoint
   */
  Future<List<DcCoordinates>> coordinateDatacenters();

  /**
   * Returns the list of keys that corresponding to the specified key prefix.
   *
   * @param keyPrefix     the prefix
   * @return a future provided with keys list
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  Future<List<String>> getKeys(String keyPrefix);

  /**
   * Returns the list of keys that corresponding to the specified key prefix.
   *
   * @param keyPrefix     the prefix
   * @param options       the blocking options
   * @return a future provided with keys list
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  Future<List<String>> getKeysWithOptions(String keyPrefix, BlockingQueryOptions options);

  /**
   * Returns key/value pair that corresponding to the specified key.
   * An empty {@link KeyValue} object will be returned if no such key is found.
   *
   * @param key           the key
   * @return a future provided with key/value pair
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  Future<KeyValue> getValue(String key);

  /**
   * Returns key/value pair that corresponding to the specified key.
   * An empty {@link KeyValue} object will be returned if no such key is found.
   * This is blocking query unlike {@link ConsulClient#getValue(String)}
   *
   * @param key           the key
   * @param options       the blocking options
   * @return a future provided with key/value pair
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  Future<KeyValue> getValueWithOptions(String key, BlockingQueryOptions options);

  /**
   * Remove the key/value pair that corresponding to the specified key
   *
   * @param key           the key
   * @return a future notified on complete
   * @see <a href="https://www.consul.io/api/kv.html#delete-key">/v1/kv/:key</a> endpoint
   */
  Future<Void> deleteValue(String key);

  /**
   * Returns the list of key/value pairs that corresponding to the specified key prefix.
   * An empty {@link KeyValueList} object will be returned if no such key prefix is found.
   *
   * @param keyPrefix     the prefix
   * @return a future provided with list of key/value pairs
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  Future<KeyValueList> getValues(String keyPrefix);

  /**
   * Returns the list of key/value pairs that corresponding to the specified key prefix.
   * An empty {@link KeyValueList} object will be returned if no such key prefix is found.
   * This is blocking query unlike {@link ConsulClient#getValues(String)}
   *
   * @param keyPrefix     the prefix
   * @param options       the blocking options
   * @return a future provided with list of key/value pairs
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  Future<KeyValueList> getValuesWithOptions(String keyPrefix, BlockingQueryOptions options);

  /**
   * Removes all the key/value pair that corresponding to the specified key prefix
   *
   * @param keyPrefix     the prefix
   * @return a future notified on complete
   * @see <a href="https://www.consul.io/api/kv.html#delete-key">/v1/kv/:key</a> endpoint
   */
  Future<Void> deleteValues(String keyPrefix);

  /**
   * Adds specified key/value pair
   *
   * @param key           the key
   * @param value         the value
   * @return a future provided with success of operation
   * @see <a href="https://www.consul.io/api/kv.html#create-update-key">/v1/kv/:key</a> endpoint
   */
  Future<Boolean> putValue(String key, String value);

  /**
   * @param key           the key
   * @param value         the value
   * @param options       options used to push pair
   * @return a future provided with success of operation
   * @see <a href="https://www.consul.io/api/kv.html#create-update-key">/v1/kv/:key</a> endpoint
   */
  Future<Boolean> putValueWithOptions(String key, String value, KeyValueOptions options);

  /**
   * Manages multiple operations inside a single, atomic transaction.
   *
   * @param request       transaction request
   * @return a future provided with result of transaction
   * @see <a href="https://www.consul.io/api/txn.html">/v1/txn</a> endpoint
   */
  Future<TxnResponse> transaction(TxnRequest request);

  /**
   * Create new Acl token
   *
   * @param token     properties of the token
   * @return a future provided with ID of created token
   * @see <a href="https://www.consul.io/api/acl.html#create-acl-token">/v1/acl/create</a> endpoint
   */
  Future<String> createAclToken(AclToken token);

  /**
   * Update Acl token
   *
   * @param token     properties of the token to be updated
   * @return a future provided with ID of updated
   * @see <a href="https://www.consul.io/api/acl.html#update-acl-token">/v1/acl/update</a> endpoint
   */
  Future<String> updateAclToken(AclToken token);

  /**
   * Clone Acl token
   *
   * @param id        the ID of token to be cloned
   * @return a future provided with ID of cloned token
   * @see <a href="https://www.consul.io/api/acl.html#clone-acl-token">/v1/acl/clone/:uuid</a> endpoint
   */
  Future<String> cloneAclToken(String id);

  /**
   * Get list of Acl token
   *
   * @return a future provided with list of tokens
   * @see <a href="https://www.consul.io/api/acl.html#list-acls">/v1/acl/list</a> endpoint
   */
  Future<List<AclToken>> listAclTokens();

  /**
   * Get info of Acl token
   *
   * @param id           the ID of token
   * @return a future provided with token
   * @see <a href="https://www.consul.io/api/acl.html#read-acl-token">/v1/acl/info/:uuid</a> endpoint
   */
  Future<AclToken> infoAclToken(String id);

  /**
   * Destroy Acl token
   *
   * @param id            the ID of token
   * @return a future notified on complete
   * @see <a href="https://www.consul.io/api/acl.html#delete-acl-token">/v1/acl/destroy/:uuid</a> endpoint
   */
  Future<Void> destroyAclToken(String id);

  /**
   * Fires a new user event
   *
   * @param name          name of event
   * @return a future provided with properties of event
   * @see <a href="https://www.consul.io/api/event.html#fire-event">/v1/event/fire/:name</a> endpoint
   */
  Future<Event> fireEvent(String name);

  /**
   * Fires a new user event
   *
   * @param name          name of event
   * @param options       options used to create event
   * @return a future provided with properties of event
   * @see <a href="https://www.consul.io/api/event.html#fire-event">/v1/event/fire/:name</a> endpoint
   */
  Future<Event> fireEventWithOptions(String name, EventOptions options);

  /**
   * Returns the most recent events known by the agent
   *
   * @return a future provided with list of events
   * @see <a href="https://www.consul.io/api/event.html#list-events">/v1/event/list</a> endpoint
   */
  Future<EventList> listEvents();

  /**
   * Returns the most recent events known by the agent.
   * This is blocking query unlike {@link ConsulClient#listEvents()}. However, the semantics of this endpoint
   * are slightly different. Most blocking queries provide a monotonic index and block until a newer index is available.
   * This can be supported as a consequence of the total ordering of the consensus protocol. With gossip,
   * there is no ordering, and instead {@code X-Consul-Index} maps to the newest event that matches the query.
   * <p>
   * In practice, this means the index is only useful when used against a single agent and has no meaning globally.
   * Because Consul defines the index as being opaque, clients should not be expecting a natural ordering either.
   *
   * @return a future provided with list of events
   * @param options       the blocking options
   * @see <a href="https://www.consul.io/api/event.html#list-events">/v1/event/list</a> endpoint
   */
  Future<EventList> listEventsWithOptions(EventListOptions options);

  /**
   * Adds a new service, with an optional health check, to the local agent.
   *
   * @param serviceOptions the options of new service
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/service.html#register-service">/v1/agent/service/register</a> endpoint
   * @see ServiceOptions
   */
  Future<Void> registerService(ServiceOptions serviceOptions);

  /**
   * Places a given service into "maintenance mode"
   *
   * @param maintenanceOptions the maintenance options
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/service.html#enable-maintenance-mode">/v1/agent/service/maintenance/:service_id</a> endpoint
   * @see MaintenanceOptions
   */
  Future<Void> maintenanceService(MaintenanceOptions maintenanceOptions);

  /**
   * Remove a service from the local agent. The agent will take care of deregistering the service with the Catalog.
   * If there is an associated check, that is also deregistered.
   *
   * @param id            the ID of service
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/service.html#deregister-service">/v1/agent/service/deregister/:service_id</a> endpoint
   */
  Future<Void> deregisterService(String id);

  /**
   * Returns the nodes providing a service
   *
   * @param service       name of service
   * @return a future provided with list of nodes providing given service
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes-for-service">/v1/catalog/service/:service</a> endpoint
   */
  Future<ServiceList> catalogServiceNodes(String service);

  /**
   * Returns the nodes providing a service
   *
   * @param service       name of service
   * @param options       options used to request services
   * @return a future provided with list of nodes providing given service
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes-for-service">/v1/catalog/service/:service</a> endpoint
   */
  Future<ServiceList> catalogServiceNodesWithOptions(String service, ServiceQueryOptions options);

  /**
   * Return all the datacenters that are known by the Consul server
   *
   * @return a future provided with list of datacenters
   * @see <a href="https://www.consul.io/api/catalog.html#list-datacenters">/v1/catalog/datacenters</a> endpoint
   */
  Future<List<String>> catalogDatacenters();

  /**
   * Returns the nodes registered in a datacenter
   *
   * @return a future provided with list of nodes
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes">/v1/catalog/nodes</a> endpoint
   */
  Future<NodeList> catalogNodes();

  /**
   * Returns the nodes registered in a datacenter
   *
   * @return a future provided with list of nodes
   * @param options       options used to request nodes
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes">/v1/catalog/nodes</a> endpoint
   */
  Future<NodeList> catalogNodesWithOptions(NodeQueryOptions options);

  /**
   * Returns the checks associated with the service
   *
   * @param service       the service name
   * @return a future provided with list of checks
   * @see <a href="https://www.consul.io/api/health.html#list-checks-for-service">/v1/health/checks/:service</a> endpoint
   */
  Future<CheckList> healthChecks(String service);

  /**
   * Returns the checks associated with the service
   *
   * @param service       the service name
   * @param options       options used to request checks
   * @return a future provided with list of checks
   * @see <a href="https://www.consul.io/api/health.html#list-checks-for-service">/v1/health/checks/:service</a> endpoint
   */
  Future<CheckList> healthChecksWithOptions(String service, CheckQueryOptions options);

  /**
   * Returns the checks in the specified status
   *
   * @param healthState   the health state
   * @return a future provided with list of checks
   * @see <a href="https://www.consul.io/api/health.html#list-checks-in-state">/v1/health/state/:state</a> endpoint
   */
  Future<CheckList> healthState(HealthState healthState);

  /**
   * Returns the checks in the specified status
   *
   * @param healthState   the health state
   * @param options       options used to request checks
   * @return a future provided with list of checks
   * @see <a href="https://www.consul.io/api/health.html#list-checks-in-state">/v1/health/state/:state</a> endpoint
   */
  Future<CheckList> healthStateWithOptions(HealthState healthState, CheckQueryOptions options);

  /**
   * Returns the nodes providing the service. This endpoint is very similar to the {@link ConsulClient#catalogServiceNodes} endpoint;
   * however, this endpoint automatically returns the status of the associated health check as well as any system level health checks.
   *
   * @param service       the service name
   * @param passing       if true, filter results to only nodes with all checks in the passing state
   * @return a future provided with list of services
   * @see <a href="https://www.consul.io/api/health.html#list-nodes-for-service">/v1/health/service/:service</a> endpoint
   */
  Future<ServiceEntryList> healthServiceNodes(String service, boolean passing);

  /**
   * Returns the nodes providing the service. This endpoint is very similar to the {@link ConsulClient#catalogServiceNodesWithOptions} endpoint;
   * however, this endpoint automatically returns the status of the associated health check as well as any system level health checks.
   *
   * @param service       the service name
   * @param passing       if true, filter results to only nodes with all checks in the passing state
   * @param options       options used to request services
   * @return a future provided with list of services
   * @see <a href="https://www.consul.io/api/health.html#list-nodes-for-service">/v1/health/service/:service</a> endpoint
   */
  Future<ServiceEntryList> healthServiceNodesWithOptions(String service, boolean passing, ServiceQueryOptions options);

  /**
   * Returns the services registered in a datacenter
   *
   * @return a future provided with list of services
   * @see <a href="https://www.consul.io/api/catalog.html#list-services">/v1/catalog/services</a> endpoint
   */
  Future<ServiceList> catalogServices();

  /**
   * Returns the services registered in a datacenter
   * This is blocking query unlike {@link ConsulClient#catalogServices()}
   *
   * @return a future provided with list of services
   * @param options       the blocking options
   * @see <a href="https://www.consul.io/api/catalog.html#list-services">/v1/catalog/services</a> endpoint
   */
  Future<ServiceList> catalogServicesWithOptions(BlockingQueryOptions options);

  /**
   * Returns the node's registered services
   *
   * @param node          node name
   * @return a future provided with list of services
   * @see <a href="https://www.consul.io/api/catalog.html#list-services-for-node">/v1/catalog/node/:node</a> endpoint
   */
  Future<ServiceList> catalogNodeServices(String node);

  /**
   * Returns the node's registered services
   * This is blocking query unlike {@link ConsulClient#catalogNodeServices(String)}
   *
   * @param node          node name
   * @param options       the blocking options
   * @return a future provided with list of services
   * @see <a href="https://www.consul.io/api/catalog.html#list-services-for-node">/v1/catalog/node/:node</a> endpoint
   */
  Future<ServiceList> catalogNodeServicesWithOptions(String node, BlockingQueryOptions options);

  /**
   * Returns list of services registered with the local agent.
   *
   * @return a future provided with list of services
   * @see <a href="https://www.consul.io/api/agent/service.html#list-services">/v1/agent/services</a> endpoint
   */
  Future<List<Service>> localServices();

  /**
   * Return all the checks that are registered with the local agent.
   *
   * @return a future provided with list of checks
   * @see <a href="https://www.consul.io/api/agent/check.html#list-checks">/v1/agent/checks</a> endpoint
   */
  Future<List<Check>> localChecks();

  /**
   * Add a new check to the local agent. The agent is responsible for managing the status of the check
   * and keeping the Catalog in sync.
   *
   * @param checkOptions  options used to register new check
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#register-check">/v1/agent/check/register</a> endpoint
   */
  Future<Void> registerCheck(CheckOptions checkOptions);

  /**
   * Remove a check from the local agent. The agent will take care of deregistering the check from the Catalog.
   *
   * @param checkId       the ID of check
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#deregister-check">/v1/agent/check/deregister/:check_id</a> endpoint
   */
  Future<Void> deregisterCheck(String checkId);

  /**
   * Set status of the check to "passing". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-pass">/v1/agent/check/pass/:check_id</a> endpoint
   * @see CheckStatus
   */
  Future<Void> passCheck(String checkId);

  /**
   * Set status of the check to "passing". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-pass">/v1/agent/check/pass/:check_id</a> endpoint
   * @see CheckStatus
   */
  Future<Void> passCheckWithNote(String checkId, String note);

  /**
   * Set status of the check to "warning". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-warn">/v1/agent/check/warn/:check_id</a> endpoint
   * @see CheckStatus
   */
  Future<Void> warnCheck(String checkId);

  /**
   * Set status of the check to "warning". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-warn">/v1/agent/check/warn/:check_id</a> endpoint
   * @see CheckStatus
   */
  Future<Void> warnCheckWithNote(String checkId, String note);

  /**
   * Set status of the check to "critical". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-fail">/v1/agent/check/fail/:check_id</a> endpoint
   * @see CheckStatus
   */
  Future<Void> failCheck(String checkId);

  /**
   * Set status of the check to "critical". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-fail">/v1/agent/check/fail/:check_id</a> endpoint
   * @see CheckStatus
   */
  Future<Void> failCheckWithNote(String checkId, String note);

  /**
   * Set status of the check to given status. Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param status        new status of check
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-update">/v1/agent/check/update/:check_id</a> endpoint
   */
  Future<Void> updateCheck(String checkId, CheckStatus status);

  /**
   * Set status of the check to given status. Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param status        new status of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-update">/v1/agent/check/update/:check_id</a> endpoint
   */
  Future<Void> updateCheckWithNote(String checkId, CheckStatus status, String note);

  /**
   * Get the Raft leader for the datacenter in which the agent is running.
   * It returns an address in format "<code>10.1.10.12:8300</code>"
   *
   * @return a future provided with address of cluster leader
   * @see <a href="https://www.consul.io/api/status.html#get-raft-leader">/v1/status/leader</a> endpoint
   */
  Future<String> leaderStatus();

  /**
   * Retrieves the Raft peers for the datacenter in which the the agent is running.
   * It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
   *
   * @return a future provided with list of peers
   * @see <a href="https://www.consul.io/api/status.html#list-raft-peers">/v1/status/peers</a> endpoint
   */
  Future<List<String>> peersStatus();

  /**
   * Initialize a new session
   *
   * @return a future provided with ID of new session
   * @see <a href="https://www.consul.io/api/session.html#create-session">/v1/session/create</a> endpoint
   */
  Future<String> createSession();

  /**
   * Initialize a new session
   *
   * @param options   options used to create session
   * @return a future provided with ID of new session
   * @see <a href="https://www.consul.io/api/session.html#create-session">/v1/session/create</a> endpoint
   */
  Future<String> createSessionWithOptions(SessionOptions options);

  /**
   * Returns the requested session information
   *
   * @param id            the ID of requested session
   * @return a future provided with info of requested session
   * @see <a href="https://www.consul.io/api/session.html#read-session">/v1/session/info/:uuid</a> endpoint
   */
  Future<Session> infoSession(String id);

  /**
   * Returns the requested session information
   * This is blocking query unlike {@link ConsulClient#infoSession(String)}
   *
   * @param id            the ID of requested session
   * @param options       the blocking options
   * @return a future provided with info of requested session
   * @see <a href="https://www.consul.io/api/session.html#read-session">/v1/session/info/:uuid</a> endpoint
   */
  Future<Session> infoSessionWithOptions(String id, BlockingQueryOptions options);

  /**
   * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
   *
   * @param id            the ID of session that should be renewed
   * @return a future provided with info of renewed session
   * @see <a href="https://www.consul.io/api/session.html#renew-session">/v1/session/renew/:uuid</a> endpoint
   */
  Future<Session> renewSession(String id);

  /**
   * Returns the active sessions
   *
   * @return a future provided with list of sessions
   * @see <a href="https://www.consul.io/api/session.html#list-sessions">/v1/session/list</a> endpoint
   */
  Future<SessionList> listSessions();

  /**
   * Returns the active sessions
   * This is blocking query unlike {@link ConsulClient#listSessions()}
   *
   * @param options       the blocking options
   * @return a future provided with list of sessions
   * @see <a href="https://www.consul.io/api/session.html#list-sessions">/v1/session/list</a> endpoint
   */
  Future<SessionList> listSessionsWithOptions(BlockingQueryOptions options);

  /**
   * Returns the active sessions for a given node
   *
   * @param nodeId        the ID of node
   * @return a future provided with list of sessions
   * @see <a href="https://www.consul.io/api/session.html#list-sessions-for-node">/v1/session/node/:node</a> endpoint
   */
  Future<SessionList> listNodeSessions(String nodeId);

  /**
   * Returns the active sessions for a given node
   * This is blocking query unlike {@link ConsulClient#listNodeSessions(String)}
   *
   * @param nodeId        the ID of node
   * @param options       the blocking options
   * @return a future provided with list of sessions
   * @see <a href="https://www.consul.io/api/session.html#list-sessions-for-node">/v1/session/node/:node</a> endpoint
   */
  Future<SessionList> listNodeSessionsWithOptions(String nodeId, BlockingQueryOptions options);

  /**
   * Destroys the given session
   *
   * @param id            the ID of session
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/session.html#delete-session">/v1/session/destroy/:uuid</a> endpoint
   */
  Future<Void> destroySession(String id);

  /**
   * @param definition    definition of the prepare query
   * @return a future provided with id of created prepare query
   * @see <a href="https://www.consul.io/api/query.html#create-prepared-query">/v1/query</a> endpoint
   */
  Future<String> createPreparedQuery(PreparedQueryDefinition definition);

  /**
   * Returns an existing prepared query
   *
   * @param id            the id of the query to read
   * @return a future provided with definition of the prepare query
   * @see <a href="https://www.consul.io/api/query.html#read-prepared-query-1">/v1/query/:uuid</a> endpoint
   */
  Future<PreparedQueryDefinition> getPreparedQuery(String id);

  /**
   * Returns a list of all prepared queries.
   *
   * @return a future provided with list of definitions of the all prepare queries
   * @see <a href="https://www.consul.io/api/query.html#read-prepared-query">/v1/query</a> endpoint
   */
  Future<List<PreparedQueryDefinition>> getAllPreparedQueries();

  /**
   * @param definition    definition of the prepare query
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/query.html#update-prepared-query">/v1/query/:uuid</a> endpoint
   */
  Future<Void> updatePreparedQuery(PreparedQueryDefinition definition);

  /**
   * Deletes an existing prepared query
   *
   * @param id            the id of the query to delete
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api/query.html#delete-prepared-query">/v1/query/:uuid</a> endpoint
   */
  Future<Void> deletePreparedQuery(String id);

  /**
   * Executes an existing prepared query.
   *
   * @param query         the ID of the query to execute. This can also be the name of an existing prepared query,
   *                      or a name that matches a prefix name for a prepared query template.
   * @return a future provided with response
   * @see <a href="https://www.consul.io/api/query.html#execute-prepared-query">/v1/query/:uuid/execute</a> endpoint
   */
  Future<PreparedQueryExecuteResponse> executePreparedQuery(String query);

  /**
   * Executes an existing prepared query.
   *
   * @param query         the ID of the query to execute. This can also be the name of an existing prepared query,
   *                      or a name that matches a prefix name for a prepared query template.
   * @param options       the options used to execute prepared query
   * @return a future provided with response
   * @see <a href="https://www.consul.io/api/query.html#execute-prepared-query">/v1/query/:uuid/execute</a> endpoint
   */
  Future<PreparedQueryExecuteResponse> executePreparedQueryWithOptions(String query, PreparedQueryExecuteOptions options);

  /**
   * Register node with external service
   * @param nodeOptions the options of new node
   * @param serviceOptions the options of new service
   * @return a future provided with response
   * @see <a href="https://www.consul.io/api-docs/catalog#register-entity">/v1/catalog/register</a> endpoint
   */
  Future<Void> registerCatalogService(Node nodeOptions, ServiceOptions serviceOptions);

  /**
   * Deregister entities from the node or deregister the node itself.
   *
   * @param nodeId            the ID of node
   * @param serviceId         the ID of the service to de-registered; if it is null, the node itself will be de-registered (as well as the entities that belongs to that node)
   * @return a future notified when complete
   * @see <a href="https://www.consul.io/api-docs/catalog#deregister-entity">/v1/catalog/deregister</a> endpoint
   */
  Future<Void> deregisterCatalogService(String nodeId, String serviceId);

  /**
   * Close the client and release its resources
   */
  void close();
}
