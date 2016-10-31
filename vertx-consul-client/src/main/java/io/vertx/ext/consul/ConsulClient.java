package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
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
   * Create a Consul client.
   *
   * @param vertx  the Vert.x instance
   * @param config the configuration
   * @return the client
   */
  static ConsulClient create(Vertx vertx, JsonObject config) {
    return new ConsulClientImpl(vertx, config);
  }

  /**
   * Returns the LAN network coordinates for all nodes in a given DC
   *
   * @param resultHandler will be provided with network coordinates of nodes in datacenter
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient coordinateNodes(Handler<AsyncResult<List<Coordinate>>> resultHandler);

  /**
   * Returns the WAN network coordinates for all Consul servers, organized by DCs
   *
   * @param resultHandler will be provided with network coordinates for all Consul servers
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient coordinateDatacenters(Handler<AsyncResult<List<DcCoordinates>>> resultHandler);

  @Fluent
  ConsulClient getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler);

  @Fluent
  ConsulClient getValueBlocking(String key, BlockingQueryOptions options, Handler<AsyncResult<KeyValue>> resultHandler);

  @Fluent
  ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValue>>> resultHandler);

  @Fluent
  ConsulClient getValuesBlocking(String keyPrefix, BlockingQueryOptions options, Handler<AsyncResult<List<KeyValue>>> resultHandler);

  @Fluent
  ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler);

  @Fluent
  ConsulClient putValueWithOptions(String key, String value, KeyValueOptions options, Handler<AsyncResult<Boolean>> resultHandler);

  /**
   * Create new Acl token
   *
   * @param token properties of the token
   * @param idHandler will be provided with ID of created token
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

  /**
   * Update Acl token
   *
   * @param token properties of the token to be updated
   * @param idHandler  will be provided with ID of updated
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

  /**
   * Clone Acl token
   *
   * @param id the ID of token to be cloned
   * @param idHandler will be provided with ID of cloned token
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler);

  /**
   * Get list of Acl token
   *
   * @param resultHandler will be provided with list of tokens
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler);

  /**
   * Get info of Acl token
   *
   * @param id the ID of token
   * @param tokenHandler will be provided with token
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler);

  /**
   * Destroy Acl token
   *
   * @param id the ID of token
   * @param resultHandler will be called on complete
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Fires a new user event
   *
   * @param name          name of event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient fireEvent(String name, Handler<AsyncResult<Event>> resultHandler);

  /**
   * Fires a new user event
   *
   * @param name          name of event
   * @param options       options used to create event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient fireEventWithOptions(String name, EventOptions options, Handler<AsyncResult<Event>> resultHandler);

  /**
   * Returns the most recent events known by the agent
   *
   * @param resultHandler will be provided with list of events
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler);

  /**
   * Adds a new service, with an optional health check, to the local agent.
   *
   * @param serviceOptions the options of new service
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see ServiceOptions
   */
  @Fluent
  ConsulClient registerService(ServiceOptions serviceOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Places a given service into "maintenance mode"
   *
   * @param maintenanceOptions the maintenance options
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see MaintenanceOptions
   */
  @Fluent
  ConsulClient maintenanceService(MaintenanceOptions maintenanceOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Remove a service from the local agent. The agent will take care of deregistering the service with the Catalog.
   * If there is an associated check, that is also deregistered.
   *
   * @param id the ID of service
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient deregisterService(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Returns the nodes providing a service
   *
   * @param service       name of service
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient catalogServiceNodes(String service, Handler<AsyncResult<List<Service>>> resultHandler);

  /**
   * Returns the nodes providing a service, filtered by tag
   *
   * @param service name of service
   * @param tag service tag
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient catalogServiceNodesWithTag(String service, String tag, Handler<AsyncResult<List<Service>>> resultHandler);

  /**
   * Return all the datacenters that are known by the Consul server
   *
   * @param resultHandler will be provided with list of datacenters
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler);

  /**
   * Returns the nodes registered in a datacenter
   *
   * @param resultHandler will be provided with list of nodes
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient catalogNodes(Handler<AsyncResult<List<Node>>> resultHandler);

  /**
   * Returns the services registered in a datacenter
   *
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient catalogServices(Handler<AsyncResult<List<Service>>> resultHandler);

  /**
   * Returns the node's registered services
   *
   * @param node          node name
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient catalogNodeServices(String node, Handler<AsyncResult<List<Service>>> resultHandler);

  @Fluent
  ConsulClient localServices(Handler<AsyncResult<List<Service>>> resultHandler);

  @Fluent
  ConsulClient localChecks(Handler<AsyncResult<List<Check>>> resultHandler);

  @Fluent
  ConsulClient registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ConsulClient updateCheckWithNote(String checkId, CheckStatus status, String note, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Get the Raft leader for the datacenter in which the agent is running.
   * It returns an address in format "<code>10.1.10.12:8300</code>"
   *
   * @param resultHandler will be provided with address of cluster leader
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler);

  /**
   * Retrieves the Raft peers for the datacenter in which the the agent is running.
   * It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
   *
   * @param resultHandler will be provided with list of peers
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler);

  /**
   * Initialize a new session
   *
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient createSession(Handler<AsyncResult<String>> idHandler);

  /**
   * Initialize a new session
   *
   * @param options   options used to create session
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler);

  /**
   * Returns the requested session information
   *
   * @param id            the ID of requested session
   * @param resultHandler will be provided with info of requested session
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler);

  /**
   * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
   *
   * @param id            the ID of session that should be renewed
   * @param resultHandler will be provided with info of renewed session
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler);

  /**
   * Returns the active sessions
   *
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient listSessions(Handler<AsyncResult<List<Session>>> resultHandler);

  /**
   * Returns the active sessions for a given node
   *
   * @param nodeId        the ID of node
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler);

  /**
   * Destroys the given session
   *
   * @param id            the ID of session
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient destroySession(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Close the client and release its resources
   */
  void close();
}
