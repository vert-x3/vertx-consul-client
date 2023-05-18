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

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.impl.ConsulClientImpl;
import io.vertx.ext.consul.policy.AclPolicy;
import io.vertx.ext.consul.token.CloneAclTokenOptions;

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
   * @param resultHandler will be provided with the configuration and member information of the local agent
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent.html#read-configuration">/v1/agent/self</a> endpoint
   */
  @Fluent
  ConsulClient agentInfo(Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   * Like {@link #agentInfo(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<JsonObject> agentInfo();

  /**
   * Returns the LAN network coordinates for all nodes in a given DC
   *
   * @param resultHandler will be provided with network coordinates of nodes in datacenter
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/coordinate.html#read-lan-coordinates">/v1/coordinate/nodes</a> endpoint
   */
  @Fluent
  ConsulClient coordinateNodes(Handler<AsyncResult<CoordinateList>> resultHandler);

  /**
   * Like {@link #coordinateNodes(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<CoordinateList> coordinateNodes();

  /**
   * Returns the LAN network coordinates for all nodes in a given DC
   * This is blocking query unlike {@link ConsulClient#coordinateNodes(Handler)}
   *
   * @param options       the blocking options
   * @param resultHandler will be provided with network coordinates of nodes in datacenter
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/coordinate.html#read-lan-coordinates">/v1/coordinate/nodes</a> endpoint
   */
  @Fluent
  ConsulClient coordinateNodesWithOptions(
    BlockingQueryOptions options,
    Handler<AsyncResult<CoordinateList>> resultHandler
  );

  /**
   * Like {@link #coordinateNodesWithOptions(BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<CoordinateList> coordinateNodesWithOptions(BlockingQueryOptions options);

  /**
   * Returns the WAN network coordinates for all Consul servers, organized by DCs
   *
   * @param resultHandler will be provided with network coordinates for all Consul servers
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/coordinate.html#read-wan-coordinates">/v1/coordinate/datacenters</a> endpoint
   */
  @Fluent
  ConsulClient coordinateDatacenters(Handler<AsyncResult<List<DcCoordinates>>> resultHandler);

  /**
   * Like {@link #coordinateDatacenters(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<DcCoordinates>> coordinateDatacenters();

  /**
   * Returns the list of keys that corresponding to the specified key prefix.
   *
   * @param keyPrefix     the prefix
   * @param resultHandler will be provided with keys list
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient getKeys(String keyPrefix, Handler<AsyncResult<List<String>>> resultHandler);

  /**
   * Like {@link #getKeys(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<String>> getKeys(String keyPrefix);

  /**
   * Returns the list of keys that corresponding to the specified key prefix.
   *
   * @param keyPrefix     the prefix
   * @param options       the blocking options
   * @param resultHandler will be provided with keys list
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient getKeysWithOptions(
    String keyPrefix,
    BlockingQueryOptions options,
    Handler<AsyncResult<List<String>>> resultHandler
  );

  /**
   * Like {@link #getKeysWithOptions(String, BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<String>> getKeysWithOptions(String keyPrefix, BlockingQueryOptions options);

  /**
   * Returns key/value pair that corresponding to the specified key.
   * An empty {@link KeyValue} object will be returned if no such key is found.
   *
   * @param key           the key
   * @param resultHandler will be provided with key/value pair
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler);

  /**
   * Like {@link #getValue(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<KeyValue> getValue(String key);

  /**
   * Returns key/value pair that corresponding to the specified key.
   * An empty {@link KeyValue} object will be returned if no such key is found.
   * This is blocking query unlike {@link ConsulClient#getValue(String, Handler)}
   *
   * @param key           the key
   * @param options       the blocking options
   * @param resultHandler will be provided with key/value pair
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient getValueWithOptions(
    String key,
    BlockingQueryOptions options,
    Handler<AsyncResult<KeyValue>> resultHandler
  );

  /**
   * Like {@link #getValueWithOptions(String, BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<KeyValue> getValueWithOptions(String key, BlockingQueryOptions options);

  /**
   * Remove the key/value pair that corresponding to the specified key
   *
   * @param key           the key
   * @param resultHandler will be called on complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#delete-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #deleteValue(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> deleteValue(String key);

  /**
   * Returns the list of key/value pairs that corresponding to the specified key prefix.
   * An empty {@link KeyValueList} object will be returned if no such key prefix is found.
   *
   * @param keyPrefix     the prefix
   * @param resultHandler will be provided with list of key/value pairs
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient getValues(String keyPrefix, Handler<AsyncResult<KeyValueList>> resultHandler);

  /**
   * Like {@link #getValues(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<KeyValueList> getValues(String keyPrefix);

  /**
   * Returns the list of key/value pairs that corresponding to the specified key prefix.
   * An empty {@link KeyValueList} object will be returned if no such key prefix is found.
   * This is blocking query unlike {@link ConsulClient#getValues(String, Handler)}
   *
   * @param keyPrefix     the prefix
   * @param options       the blocking options
   * @param resultHandler will be provided with list of key/value pairs
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#read-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient getValuesWithOptions(
    String keyPrefix,
    BlockingQueryOptions options,
    Handler<AsyncResult<KeyValueList>> resultHandler
  );

  /**
   * Like {@link #getValuesWithOptions(String, BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<KeyValueList> getValuesWithOptions(String keyPrefix, BlockingQueryOptions options);

  /**
   * Removes all the key/value pair that corresponding to the specified key prefix
   *
   * @param keyPrefix     the prefix
   * @param resultHandler will be called on complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#delete-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #deleteValues(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> deleteValues(String keyPrefix);

  /**
   * Adds specified key/value pair
   *
   * @param key           the key
   * @param value         the value
   * @param resultHandler will be provided with success of operation
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#create-update-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler);

  /**
   * Like {@link #putValue(String, String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Boolean> putValue(String key, String value);

  /**
   * @param key           the key
   * @param value         the value
   * @param options       options used to push pair
   * @param resultHandler will be provided with success of operation
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/kv.html#create-update-key">/v1/kv/:key</a> endpoint
   */
  @Fluent
  ConsulClient putValueWithOptions(
    String key,
    String value,
    KeyValueOptions options,
    Handler<AsyncResult<Boolean>> resultHandler
  );

  /**
   * Like {@link #putValueWithOptions(String, String, KeyValueOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Boolean> putValueWithOptions(String key, String value, KeyValueOptions options);

  /**
   * Manages multiple operations inside a single, atomic transaction.
   *
   * @param request       transaction request
   * @param resultHandler will be provided with result of transaction
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/txn.html">/v1/txn</a> endpoint
   */
  @Fluent
  ConsulClient transaction(TxnRequest request, Handler<AsyncResult<TxnResponse>> resultHandler);

  /**
   * Like {@link #transaction(TxnRequest, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<TxnResponse> transaction(TxnRequest request);

  /**
   * Creates a new ACL policy
   *
   * @param policy        properties of policy
   * @param resultHandler will be provided with result of policy
   * @return reference to this, for fluency
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/policies#create-a-policy">/acl/policy</a>
   */
  @Fluent
  ConsulClient createAclPolicy(AclPolicy policy, Handler<AsyncResult<String>> resultHandler);

  /**
   * Creates a new ACL policy
   *
   * @param policy properties of policy
   * @return a future provided with ID of created policy
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/policies#create-a-policy">/acl/policy</a>
   */
  Future<String> createAclPolicy(AclPolicy policy);

  /**
   * Create an Acl token
   *
   * @param token         properties of the token
   * @param resultHandler will be provided with result of token
   * @return reference to this, for fluency
   * {@link io.vertx.ext.consul.token.AclToken} accessorId - required in the URL path or JSON body for getting, updating and cloning token.
   * {@link io.vertx.ext.consul.token.AclToken} secretId - using in {@link ConsulClientOptions#setAclToken(String)}.
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#create-a-token">/v1/acl/create</a> endpoint
   */
  @Fluent
  ConsulClient createAclToken(io.vertx.ext.consul.token.AclToken token, Handler<AsyncResult<io.vertx.ext.consul.token.AclToken>> resultHandler);

  /**
   * Create an Acl token
   *
   * @param token properties of the token
   * @return a future NewAclToken in which two fields accessorId and secretId.
   * {@link io.vertx.ext.consul.token.AclToken} accessorId - required in the URL path or JSON body for getting, updating and cloning token.
   * {@link io.vertx.ext.consul.token.AclToken} secretId - using in {@link ConsulClientOptions#setAclToken(String)}.
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#create-a-token">/v1/acl/create</a> endpoint
   */
  Future<io.vertx.ext.consul.token.AclToken> createAclToken(io.vertx.ext.consul.token.AclToken token);

  /**
   * Update an existing Acl token
   *
   * @param accessorId    uuid of the token
   * @param token         properties of the token
   * @param resultHandler will be provided with result of token
   * @return reference to this, for fluency
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#update-a-token">/acl/token/:accessorId</a> endpoint
   */
  @Fluent
  ConsulClient updateAclToken(String accessorId,
                              io.vertx.ext.consul.token.AclToken token,
                              Handler<AsyncResult<io.vertx.ext.consul.token.AclToken>> resultHandler);

  /**
   * Update an existing Acl token
   *
   * @param accessorId uuid of the token
   * @param token      properties of the token
   * @return a future NewAclToken like in {@link #createAclToken(io.vertx.ext.consul.token.AclToken)}
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#update-a-token">/acl/token/:accessorId</a> endpoint
   */
  Future<io.vertx.ext.consul.token.AclToken> updateAclToken(String accessorId, io.vertx.ext.consul.token.AclToken token);

  /**
   * Clones an existing ACL token
   *
   * @param accessorId    uuid of the token
   * @param cloneAclToken properties of cloned token
   * @param resultHandler will be provided with result of token
   * @return reference to this, for fluency
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#clone-a-token">/acl/token/:accessorId/clone</a> endpoint
   */
  @Fluent
  ConsulClient cloneAclToken(String accessorId,
                             CloneAclTokenOptions cloneAclToken,
                             Handler<AsyncResult<io.vertx.ext.consul.token.AclToken>> resultHandler);

  /**
   * Clones an existing ACL token
   *
   * @param accessorId    uuid of the token
   * @param cloneAclTokenOptions properties of cloned token
   * @return a future NewAclToken like in {@link #createAclToken(io.vertx.ext.consul.token.AclToken)}
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#clone-a-token">/acl/token/:accessorId/clone</a> endpoint
   */
  Future<io.vertx.ext.consul.token.AclToken> cloneAclToken(String accessorId, CloneAclTokenOptions cloneAclTokenOptions);

  /**
   * Get list of Acl token
   *
   * @param resultHandler will be provided with result of tokens
   * @return reference to this, for fluency
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#list-tokens">/v1/acl/tokens</a> endpoint
   */
  @Fluent
  ConsulClient getAclTokens(Handler<AsyncResult<List<io.vertx.ext.consul.token.AclToken>>> resultHandler);

  /**
   * Get list of Acl token
   *
   * @return a future provided with list of tokens
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#list-tokens">/v1/acl/tokens</a> endpoint
   */
  Future<List<io.vertx.ext.consul.token.AclToken>> getAclTokens();

  /**
   * Reads an ACL token with the given Accessor ID
   *
   * @param accessorId    uuid of token
   * @param resultHandler will be provided with result of token
   * @return reference to this, for fluency
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#read-a-token">/v1/acl/token/:AccessorID</a> endpoint
   */
  @Fluent
  ConsulClient readAclToken(String accessorId, Handler<AsyncResult<io.vertx.ext.consul.token.AclToken>> resultHandler);

  /**
   * Reads an ACL token with the given Accessor ID
   *
   * @param accessorId uuid of token
   * @return a future provided with token
   * @see <a href="https://developer.hashicorp.com/consul/api-docs/v1.11.x/acl/tokens#read-a-token">/v1/acl/token/:AccessorID</a> endpoint
   */
  Future<io.vertx.ext.consul.token.AclToken> readAclToken(String accessorId);

  /**
   * Deletes an ACL token
   *
   * @param accessorId    uuid of token
   * @param resultHandler will be provided with result of token deleting
   * @return reference to this, for fluency
   */
  @Fluent
  ConsulClient deleteAclToken(String accessorId, Handler<AsyncResult<Boolean>> resultHandler);

  /**
   * Deletes an ACL token
   *
   * @param accessorId uuid of token
   * @return a future boolean value: true or false, indicating whether the deletion was successful.
   */
  Future<Boolean> deleteAclToken(String accessorId);

  /**
   * Legacy create new Acl token
   *
   * @param token     properties of the token
   * @param idHandler will be provided with ID of created token
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/acl.html#create-acl-token">/v1/acl/create</a> endpoint
   * @deprecated Use {@link #createAclToken(io.vertx.ext.consul.token.AclToken)} instead
   */
  @Fluent
  @Deprecated(since = "Consul version 1.11")
  ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

  /**
   * Like {@link #createAclToken(AclToken, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  @Deprecated(since = "Consul version 1.11")
  Future<String> createAclToken(AclToken token);

  /**
   * Update Acl token
   *
   * @param token     properties of the token to be updated
   * @param idHandler will be provided with ID of updated
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/acl.html#update-acl-token">/v1/acl/update</a> endpoint
   * @deprecated Use {@link #updateAclToken(String, io.vertx.ext.consul.token.AclToken)} instead
   */
  @Fluent
  @Deprecated(since = "Consul version 1.11")
  ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

  /**
   * Like {@link #updateAclToken(AclToken, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  @Deprecated(since = "Consul version 1.11")
  Future<String> updateAclToken(AclToken token);

  /**
   * Clone Acl token
   *
   * @param id        the ID of token to be cloned
   * @param idHandler will be provided with ID of cloned token
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/acl.html#clone-acl-token">/v1/acl/clone/:uuid</a> endpoint
   * @deprecated Use {@link #cloneAclToken(String, CloneAclTokenOptions)} instead
   */
  @Fluent
  @Deprecated(since = "Consul version 1.11")
  ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler);

  /**
   * Like {@link #cloneAclToken(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  @Deprecated(since = "Consul version 1.11")
  Future<String> cloneAclToken(String id);

  /**
   * Get list of Acl token
   *
   * @param resultHandler will be provided with list of tokens
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/acl.html#list-acls">/v1/acl/list</a> endpoint
   * @deprecated Use {@link #getAclTokens()} instead
   */
  @Fluent
  @Deprecated(since = "Consul version 1.11")
  ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler);

  /**
   * Like {@link #listAclTokens(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  @Deprecated(since = "Consul version 1.11")
  Future<List<AclToken>> listAclTokens();

  /**
   * Get info of Acl token
   *
   * @param id           the ID of token
   * @param tokenHandler will be provided with token
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/acl.html#read-acl-token">/v1/acl/info/:uuid</a> endpoint
   * @deprecated Use {@link #readAclToken(String)} instead
   */
  @Fluent
  @Deprecated(since = "Consul version 1.11")
  ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler);

  /**
   * Like {@link #infoAclToken(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  @Deprecated(since = "Consul version 1.11")
  Future<AclToken> infoAclToken(String id);

  /**
   * Destroy Acl token
   *
   * @param id            the ID of token
   * @param resultHandler will be called on complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/acl.html#delete-acl-token">/v1/acl/destroy/:uuid</a> endpoint
   * @deprecated Use {@link #deleteAclToken(String)} instead
   */
  @Fluent
  @Deprecated(since = "Consul version 1.11")
  ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #destroyAclToken(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  @Deprecated(since = "Consul version 1.11")
  Future<Void> destroyAclToken(String id);

  /**
   * Fires a new user event
   *
   * @param name          name of event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/event.html#fire-event">/v1/event/fire/:name</a> endpoint
   */
  @Fluent
  ConsulClient fireEvent(String name, Handler<AsyncResult<Event>> resultHandler);

  /**
   * Like {@link #fireEvent(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Event> fireEvent(String name);

  /**
   * Fires a new user event
   *
   * @param name          name of event
   * @param options       options used to create event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/event.html#fire-event">/v1/event/fire/:name</a> endpoint
   */
  @Fluent
  ConsulClient fireEventWithOptions(String name, EventOptions options, Handler<AsyncResult<Event>> resultHandler);

  /**
   * Like {@link #fireEventWithOptions(String, EventOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Event> fireEventWithOptions(String name, EventOptions options);

  /**
   * Returns the most recent events known by the agent
   *
   * @param resultHandler will be provided with list of events
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/event.html#list-events">/v1/event/list</a> endpoint
   */
  @Fluent
  ConsulClient listEvents(Handler<AsyncResult<EventList>> resultHandler);

  /**
   * Like {@link #listEvents(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<EventList> listEvents();

  /**
   * Returns the most recent events known by the agent.
   * This is blocking query unlike {@link ConsulClient#listEvents(Handler)}. However, the semantics of this endpoint
   * are slightly different. Most blocking queries provide a monotonic index and block until a newer index is available.
   * This can be supported as a consequence of the total ordering of the consensus protocol. With gossip,
   * there is no ordering, and instead {@code X-Consul-Index} maps to the newest event that matches the query.
   * <p>
   * In practice, this means the index is only useful when used against a single agent and has no meaning globally.
   * Because Consul defines the index as being opaque, clients should not be expecting a natural ordering either.
   *
   * @param resultHandler will be provided with list of events
   * @param options       the blocking options
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/event.html#list-events">/v1/event/list</a> endpoint
   */
  @Fluent
  ConsulClient listEventsWithOptions(EventListOptions options, Handler<AsyncResult<EventList>> resultHandler);

  /**
   * Like {@link #listEventsWithOptions(EventListOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<EventList> listEventsWithOptions(EventListOptions options);

  /**
   * Adds a new service, with an optional health check, to the local agent.
   *
   * @param serviceOptions the options of new service
   * @param resultHandler  will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/service.html#register-service">/v1/agent/service/register</a> endpoint
   * @see ServiceOptions
   */
  @Fluent
  ConsulClient registerService(ServiceOptions serviceOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #registerService(ServiceOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> registerService(ServiceOptions serviceOptions);

  /**
   * Places a given service into "maintenance mode"
   *
   * @param maintenanceOptions the maintenance options
   * @param resultHandler      will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/service.html#enable-maintenance-mode">/v1/agent/service/maintenance/:service_id</a> endpoint
   * @see MaintenanceOptions
   */
  @Fluent
  ConsulClient maintenanceService(MaintenanceOptions maintenanceOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #maintenanceService(MaintenanceOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> maintenanceService(MaintenanceOptions maintenanceOptions);

  /**
   * Remove a service from the local agent. The agent will take care of deregistering the service with the Catalog.
   * If there is an associated check, that is also deregistered.
   *
   * @param id            the ID of service
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/service.html#deregister-service">/v1/agent/service/deregister/:service_id</a> endpoint
   */
  @Fluent
  ConsulClient deregisterService(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #deregisterService(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> deregisterService(String id);

  /**
   * Returns the nodes providing a service
   *
   * @param service       name of service
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes-for-service">/v1/catalog/service/:service</a> endpoint
   */
  @Fluent
  ConsulClient catalogServiceNodes(String service, Handler<AsyncResult<ServiceList>> resultHandler);

  /**
   * Like {@link #catalogServiceNodes(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceList> catalogServiceNodes(String service);

  /**
   * Returns the nodes providing a service
   *
   * @param service       name of service
   * @param options       options used to request services
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes-for-service">/v1/catalog/service/:service</a> endpoint
   */
  @Fluent
  ConsulClient catalogServiceNodesWithOptions(
    String service,
    ServiceQueryOptions options,
    Handler<AsyncResult<ServiceList>> resultHandler
  );

  /**
   * Like {@link #catalogServiceNodesWithOptions(String, ServiceQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceList> catalogServiceNodesWithOptions(String service, ServiceQueryOptions options);

  /**
   * Return all the datacenters that are known by the Consul server
   *
   * @param resultHandler will be provided with list of datacenters
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-datacenters">/v1/catalog/datacenters</a> endpoint
   */
  @Fluent
  ConsulClient catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler);

  /**
   * Like {@link #catalogDatacenters(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<String>> catalogDatacenters();

  /**
   * Returns the nodes registered in a datacenter
   *
   * @param resultHandler will be provided with list of nodes
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes">/v1/catalog/nodes</a> endpoint
   */
  @Fluent
  ConsulClient catalogNodes(Handler<AsyncResult<NodeList>> resultHandler);

  /**
   * Like {@link #catalogNodes(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<NodeList> catalogNodes();

  /**
   * Returns the nodes registered in a datacenter
   *
   * @param resultHandler will be provided with list of nodes
   * @param options       options used to request nodes
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-nodes">/v1/catalog/nodes</a> endpoint
   */
  @Fluent
  ConsulClient catalogNodesWithOptions(NodeQueryOptions options, Handler<AsyncResult<NodeList>> resultHandler);

  /**
   * Like {@link #catalogNodesWithOptions(NodeQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<NodeList> catalogNodesWithOptions(NodeQueryOptions options);

  /**
   * Returns the checks associated with the service
   *
   * @param service       the service name
   * @param resultHandler will be provided with list of checks
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/health.html#list-checks-for-service">/v1/health/checks/:service</a> endpoint
   */
  @Fluent
  ConsulClient healthChecks(String service, Handler<AsyncResult<CheckList>> resultHandler);

  /**
   * Like {@link #healthChecks(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<CheckList> healthChecks(String service);

  /**
   * Returns the checks associated with the service
   *
   * @param service       the service name
   * @param options       options used to request checks
   * @param resultHandler will be provided with list of checks
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/health.html#list-checks-for-service">/v1/health/checks/:service</a> endpoint
   */
  @Fluent
  ConsulClient healthChecksWithOptions(
    String service,
    CheckQueryOptions options,
    Handler<AsyncResult<CheckList>> resultHandler
  );

  /**
   * Like {@link #healthChecksWithOptions(String, CheckQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<CheckList> healthChecksWithOptions(String service, CheckQueryOptions options);

  /**
   * Returns the checks in the specified status
   *
   * @param healthState   the health state
   * @param resultHandler will be provided with list of checks
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/health.html#list-checks-in-state">/v1/health/state/:state</a> endpoint
   */
  @Fluent
  ConsulClient healthState(HealthState healthState, Handler<AsyncResult<CheckList>> resultHandler);

  /**
   * Like {@link #healthState(HealthState, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<CheckList> healthState(HealthState healthState);

  /**
   * Returns the checks in the specified status
   *
   * @param healthState   the health state
   * @param options       options used to request checks
   * @param resultHandler will be provided with list of checks
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/health.html#list-checks-in-state">/v1/health/state/:state</a> endpoint
   */
  @Fluent
  ConsulClient healthStateWithOptions(
    HealthState healthState,
    CheckQueryOptions options,
    Handler<AsyncResult<CheckList>> resultHandler
  );

  /**
   * Like {@link #healthStateWithOptions(HealthState, CheckQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<CheckList> healthStateWithOptions(HealthState healthState, CheckQueryOptions options);

  /**
   * Returns the nodes providing the service. This endpoint is very similar to the {@link ConsulClient#catalogServiceNodes} endpoint;
   * however, this endpoint automatically returns the status of the associated health check as well as any system level health checks.
   *
   * @param service       the service name
   * @param passing       if true, filter results to only nodes with all checks in the passing state
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/health.html#list-nodes-for-service">/v1/health/service/:service</a> endpoint
   */
  @Fluent
  ConsulClient healthServiceNodes(
    String service,
    boolean passing,
    Handler<AsyncResult<ServiceEntryList>> resultHandler
  );

  /**
   * Like {@link #healthServiceNodes(String, boolean, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceEntryList> healthServiceNodes(String service, boolean passing);

  /**
   * Returns the nodes providing the service. This endpoint is very similar to the {@link ConsulClient#catalogServiceNodesWithOptions} endpoint;
   * however, this endpoint automatically returns the status of the associated health check as well as any system level health checks.
   *
   * @param service       the service name
   * @param passing       if true, filter results to only nodes with all checks in the passing state
   * @param options       options used to request services
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/health.html#list-nodes-for-service">/v1/health/service/:service</a> endpoint
   */
  @Fluent
  ConsulClient healthServiceNodesWithOptions(
    String service,
    boolean passing,
    ServiceQueryOptions options,
    Handler<AsyncResult<ServiceEntryList>> resultHandler
  );

  /**
   * Like {@link #healthServiceNodesWithOptions(String, boolean, ServiceQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceEntryList> healthServiceNodesWithOptions(String service, boolean passing, ServiceQueryOptions options);

  /**
   * Returns the services registered in a datacenter
   *
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-services">/v1/catalog/services</a> endpoint
   */
  @Fluent
  ConsulClient catalogServices(Handler<AsyncResult<ServiceList>> resultHandler);

  /**
   * Like {@link #catalogServices(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceList> catalogServices();

  /**
   * Returns the services registered in a datacenter
   * This is blocking query unlike {@link ConsulClient#catalogServices(Handler)}
   *
   * @param resultHandler will be provided with list of services
   * @param options       the blocking options
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-services">/v1/catalog/services</a> endpoint
   */
  @Fluent
  ConsulClient catalogServicesWithOptions(
    BlockingQueryOptions options,
    Handler<AsyncResult<ServiceList>> resultHandler
  );

  /**
   * Like {@link #catalogServicesWithOptions(BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceList> catalogServicesWithOptions(BlockingQueryOptions options);

  /**
   * Returns the node's registered services
   *
   * @param node          node name
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-services-for-node">/v1/catalog/node/:node</a> endpoint
   */
  @Fluent
  ConsulClient catalogNodeServices(String node, Handler<AsyncResult<ServiceList>> resultHandler);

  /**
   * Like {@link #catalogNodeServices(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceList> catalogNodeServices(String node);

  /**
   * Returns the node's registered services
   * This is blocking query unlike {@link ConsulClient#catalogNodeServices(String, Handler)}
   *
   * @param node          node name
   * @param options       the blocking options
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/catalog.html#list-services-for-node">/v1/catalog/node/:node</a> endpoint
   */
  @Fluent
  ConsulClient catalogNodeServicesWithOptions(
    String node,
    BlockingQueryOptions options,
    Handler<AsyncResult<ServiceList>> resultHandler
  );

  /**
   * Like {@link #catalogNodeServicesWithOptions(String, BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<ServiceList> catalogNodeServicesWithOptions(String node, BlockingQueryOptions options);

  /**
   * Returns list of services registered with the local agent.
   *
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/service.html#list-services">/v1/agent/services</a> endpoint
   */
  @Fluent
  ConsulClient localServices(Handler<AsyncResult<List<Service>>> resultHandler);

  /**
   * Like {@link #localServices(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<Service>> localServices();

  /**
   * Return all the checks that are registered with the local agent.
   *
   * @param resultHandler will be provided with list of checks
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#list-checks">/v1/agent/checks</a> endpoint
   */
  @Fluent
  ConsulClient localChecks(Handler<AsyncResult<List<Check>>> resultHandler);

  /**
   * Like {@link #localChecks(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<Check>> localChecks();

  /**
   * Add a new check to the local agent. The agent is responsible for managing the status of the check
   * and keeping the Catalog in sync.
   *
   * @param checkOptions  options used to register new check
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#register-check">/v1/agent/check/register</a> endpoint
   */
  @Fluent
  ConsulClient registerCheck(CheckOptions checkOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #registerCheck(CheckOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> registerCheck(CheckOptions checkOptions);

  /**
   * Remove a check from the local agent. The agent will take care of deregistering the check from the Catalog.
   *
   * @param checkId       the ID of check
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#deregister-check">/v1/agent/check/deregister/:check_id</a> endpoint
   */
  @Fluent
  ConsulClient deregisterCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #deregisterCheck(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> deregisterCheck(String checkId);

  /**
   * Set status of the check to "passing". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-pass">/v1/agent/check/pass/:check_id</a> endpoint
   * @see CheckStatus
   */
  @Fluent
  ConsulClient passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #passCheck(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> passCheck(String checkId);

  /**
   * Set status of the check to "passing". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-pass">/v1/agent/check/pass/:check_id</a> endpoint
   * @see CheckStatus
   */
  @Fluent
  ConsulClient passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #passCheckWithNote(String, String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> passCheckWithNote(String checkId, String note);

  /**
   * Set status of the check to "warning". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-warn">/v1/agent/check/warn/:check_id</a> endpoint
   * @see CheckStatus
   */
  @Fluent
  ConsulClient warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #warnCheck(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> warnCheck(String checkId);

  /**
   * Set status of the check to "warning". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-warn">/v1/agent/check/warn/:check_id</a> endpoint
   * @see CheckStatus
   */
  @Fluent
  ConsulClient warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #warnCheckWithNote(String, String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> warnCheckWithNote(String checkId, String note);

  /**
   * Set status of the check to "critical". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-fail">/v1/agent/check/fail/:check_id</a> endpoint
   * @see CheckStatus
   */
  @Fluent
  ConsulClient failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #failCheck(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> failCheck(String checkId);

  /**
   * Set status of the check to "critical". Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-fail">/v1/agent/check/fail/:check_id</a> endpoint
   * @see CheckStatus
   */
  @Fluent
  ConsulClient failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #failCheckWithNote(String, String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> failCheckWithNote(String checkId, String note);

  /**
   * Set status of the check to given status. Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param status        new status of check
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-update">/v1/agent/check/update/:check_id</a> endpoint
   */
  @Fluent
  ConsulClient updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #updateCheck(String, CheckStatus, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> updateCheck(String checkId, CheckStatus status);

  /**
   * Set status of the check to given status. Used with a check that is of the TTL type. The TTL clock will be reset.
   *
   * @param checkId       the ID of check
   * @param status        new status of check
   * @param note          specifies a human-readable message. This will be passed through to the check's {@code Output} field.
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/agent/check.html#ttl-check-update">/v1/agent/check/update/:check_id</a> endpoint
   */
  @Fluent
  ConsulClient updateCheckWithNote(
    String checkId,
    CheckStatus status,
    String note,
    Handler<AsyncResult<Void>> resultHandler
  );

  /**
   * Like {@link #updateCheckWithNote(String, CheckStatus, String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> updateCheckWithNote(String checkId, CheckStatus status, String note);

  /**
   * Get the Raft leader for the datacenter in which the agent is running.
   * It returns an address in format "<code>10.1.10.12:8300</code>"
   *
   * @param resultHandler will be provided with address of cluster leader
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/status.html#get-raft-leader">/v1/status/leader</a> endpoint
   */
  @Fluent
  ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler);

  /**
   * Like {@link #leaderStatus(Handler)} but returns a {@code Future} of the asynchronous result.
   *
   * @return
   */
  Future<String> leaderStatus();

  /**
   * Retrieves the Raft peers for the datacenter in which the the agent is running.
   * It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
   *
   * @param resultHandler will be provided with list of peers
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/status.html#list-raft-peers">/v1/status/peers</a> endpoint
   */
  @Fluent
  ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler);

  /**
   * Like {@link #peersStatus(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<String>> peersStatus();

  /**
   * Initialize a new session
   *
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#create-session">/v1/session/create</a> endpoint
   */
  @Fluent
  ConsulClient createSession(Handler<AsyncResult<String>> idHandler);

  /**
   * Like {@link #createSession(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<String> createSession();

  /**
   * Initialize a new session
   *
   * @param options   options used to create session
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#create-session">/v1/session/create</a> endpoint
   */
  @Fluent
  ConsulClient createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler);

  /**
   * Like {@link #createSessionWithOptions(SessionOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<String> createSessionWithOptions(SessionOptions options);

  /**
   * Returns the requested session information
   *
   * @param id            the ID of requested session
   * @param resultHandler will be provided with info of requested session
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#read-session">/v1/session/info/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler);

  /**
   * Like {@link #infoSession(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Session> infoSession(String id);

  /**
   * Returns the requested session information
   * This is blocking query unlike {@link ConsulClient#infoSession(String, Handler)}
   *
   * @param id            the ID of requested session
   * @param options       the blocking options
   * @param resultHandler will be provided with info of requested session
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#read-session">/v1/session/info/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient infoSessionWithOptions(
    String id,
    BlockingQueryOptions options,
    Handler<AsyncResult<Session>> resultHandler
  );

  /**
   * Like {@link #infoSessionWithOptions(String, BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Session> infoSessionWithOptions(String id, BlockingQueryOptions options);

  /**
   * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
   *
   * @param id            the ID of session that should be renewed
   * @param resultHandler will be provided with info of renewed session
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#renew-session">/v1/session/renew/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler);

  /**
   * Like {@link #renewSession(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Session> renewSession(String id);

  /**
   * Returns the active sessions
   *
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#list-sessions">/v1/session/list</a> endpoint
   */
  @Fluent
  ConsulClient listSessions(Handler<AsyncResult<SessionList>> resultHandler);

  /**
   * Like {@link #listSessions(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<SessionList> listSessions();

  /**
   * Returns the active sessions
   * This is blocking query unlike {@link ConsulClient#listSessions(Handler)}
   *
   * @param options       the blocking options
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#list-sessions">/v1/session/list</a> endpoint
   */
  @Fluent
  ConsulClient listSessionsWithOptions(BlockingQueryOptions options, Handler<AsyncResult<SessionList>> resultHandler);

  /**
   * Like {@link #listSessionsWithOptions(BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<SessionList> listSessionsWithOptions(BlockingQueryOptions options);

  /**
   * Returns the active sessions for a given node
   *
   * @param nodeId        the ID of node
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#list-sessions-for-node">/v1/session/node/:node</a> endpoint
   */
  @Fluent
  ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<SessionList>> resultHandler);

  /**
   * Like {@link #listNodeSessions(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<SessionList> listNodeSessions(String nodeId);

  /**
   * Returns the active sessions for a given node
   * This is blocking query unlike {@link ConsulClient#listNodeSessions(String, Handler)}
   *
   * @param nodeId        the ID of node
   * @param options       the blocking options
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#list-sessions-for-node">/v1/session/node/:node</a> endpoint
   */
  @Fluent
  ConsulClient listNodeSessionsWithOptions(
    String nodeId,
    BlockingQueryOptions options,
    Handler<AsyncResult<SessionList>> resultHandler
  );

  /**
   * Like {@link #listNodeSessionsWithOptions(String, BlockingQueryOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<SessionList> listNodeSessionsWithOptions(String nodeId, BlockingQueryOptions options);

  /**
   * Destroys the given session
   *
   * @param id            the ID of session
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/session.html#delete-session">/v1/session/destroy/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient destroySession(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #destroySession(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> destroySession(String id);

  /**
   * @param definition    definition of the prepare query
   * @param resultHandler will be provided with id of created prepare query
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#create-prepared-query">/v1/query</a> endpoint
   */
  @Fluent
  ConsulClient createPreparedQuery(PreparedQueryDefinition definition, Handler<AsyncResult<String>> resultHandler);

  /**
   * Like {@link #createPreparedQuery(PreparedQueryDefinition, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<String> createPreparedQuery(PreparedQueryDefinition definition);

  /**
   * Returns an existing prepared query
   *
   * @param id            the id of the query to read
   * @param resultHandler will be provided with definition of the prepare query
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#read-prepared-query-1">/v1/query/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient getPreparedQuery(String id, Handler<AsyncResult<PreparedQueryDefinition>> resultHandler);

  /**
   * Like {@link #getPreparedQuery(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<PreparedQueryDefinition> getPreparedQuery(String id);

  /**
   * Returns a list of all prepared queries.
   *
   * @param resultHandler will be provided with list of definitions of the all prepare queries
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#read-prepared-query">/v1/query</a> endpoint
   */
  @Fluent
  ConsulClient getAllPreparedQueries(Handler<AsyncResult<List<PreparedQueryDefinition>>> resultHandler);

  /**
   * Like {@link #getAllPreparedQueries(Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<List<PreparedQueryDefinition>> getAllPreparedQueries();

  /**
   * @param definition    definition of the prepare query
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#update-prepared-query">/v1/query/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient updatePreparedQuery(PreparedQueryDefinition definition, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #updatePreparedQuery(PreparedQueryDefinition, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> updatePreparedQuery(PreparedQueryDefinition definition);

  /**
   * Deletes an existing prepared query
   *
   * @param id            the id of the query to delete
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#delete-prepared-query">/v1/query/:uuid</a> endpoint
   */
  @Fluent
  ConsulClient deletePreparedQuery(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Like {@link #deletePreparedQuery(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<Void> deletePreparedQuery(String id);

  /**
   * Executes an existing prepared query.
   *
   * @param query         the ID of the query to execute. This can also be the name of an existing prepared query,
   *                      or a name that matches a prefix name for a prepared query template.
   * @param resultHandler will be provided with response
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#execute-prepared-query">/v1/query/:uuid/execute</a> endpoint
   */
  @Fluent
  ConsulClient executePreparedQuery(String query, Handler<AsyncResult<PreparedQueryExecuteResponse>> resultHandler);

  /**
   * Like {@link #executePreparedQuery(String, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<PreparedQueryExecuteResponse> executePreparedQuery(String query);

  /**
   * Executes an existing prepared query.
   *
   * @param query         the ID of the query to execute. This can also be the name of an existing prepared query,
   *                      or a name that matches a prefix name for a prepared query template.
   * @param options       the options used to execute prepared query
   * @param resultHandler will be provided with response
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api/query.html#execute-prepared-query">/v1/query/:uuid/execute</a> endpoint
   */
  @Fluent
  ConsulClient executePreparedQueryWithOptions(
    String query,
    PreparedQueryExecuteOptions options,
    Handler<AsyncResult<PreparedQueryExecuteResponse>> resultHandler
  );

  /**
   * Like {@link #executePreparedQueryWithOptions(String, PreparedQueryExecuteOptions, Handler)} but returns a {@code Future} of the asynchronous result.
   */
  Future<PreparedQueryExecuteResponse> executePreparedQueryWithOptions(
    String query,
    PreparedQueryExecuteOptions options
  );

  /**
   * Register node with external service
   *
   * @param nodeOptions    the options of new node
   * @param serviceOptions the options of new service
   * @param resultHandler  will be provided with response
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api-docs/catalog#register-entity">/v1/catalog/register</a> endpoint
   */
  @Fluent
  ConsulClient registerCatalogService(
    Node nodeOptions,
    ServiceOptions serviceOptions,
    Handler<AsyncResult<Void>> resultHandler
  );

  /**
   * Deregister entities from the node or deregister the node itself.
   *
   * @param nodeId        the ID of node
   * @param serviceId     the ID of the service to de-registered; if it is null, the node itself will be de-registered (as well as the entities that belongs to that node)
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   * @see <a href="https://www.consul.io/api-docs/catalog#deregister-entity">/v1/catalog/deregister</a> endpoint
   */
  @Fluent
  ConsulClient deregisterCatalogService(String nodeId, String serviceId, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Close the client and release its resources
   */
  void close();
}
