/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.ext.consul;

import java.util.Map;
import rx.Observable;
import io.vertx.ext.consul.CheckStatus;
import io.vertx.ext.consul.Event;
import io.vertx.rxjava.core.Vertx;
import io.vertx.ext.consul.MaintenanceOptions;
import io.vertx.ext.consul.Check;
import io.vertx.ext.consul.Service;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.ServiceOptions;
import java.util.List;
import io.vertx.ext.consul.KeyValueOptions;
import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.SessionOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.Session;
import io.vertx.ext.consul.EventOptions;
import io.vertx.ext.consul.Node;

/**
 * A Vert.x service used to interact with Consul.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ConsulClient original} non RX-ified interface using Vert.x codegen.
 */

public class ConsulClient {

  final io.vertx.ext.consul.ConsulClient delegate;

  public ConsulClient(io.vertx.ext.consul.ConsulClient delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a Consul client.
   * @param vertx the Vert.x instance
   * @param config the configuration
   * @return the client
   */
  public static ConsulClient create(Vertx vertx, JsonObject config) { 
    ConsulClient ret = ConsulClient.newInstance(io.vertx.ext.consul.ConsulClient.create((io.vertx.core.Vertx)vertx.getDelegate(), config));
    return ret;
  }

  public ConsulClient getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler) { 
    delegate.getValue(key, resultHandler);
    return this;
  }

  public Observable<KeyValue> getValueObservable(String key) { 
    io.vertx.rx.java.ObservableFuture<KeyValue> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getValue(key, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.deleteValue(key, resultHandler);
    return this;
  }

  public Observable<Void> deleteValueObservable(String key) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deleteValue(key, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValue>>> resultHandler) { 
    delegate.getValues(keyPrefix, resultHandler);
    return this;
  }

  public Observable<List<KeyValue>> getValuesObservable(String keyPrefix) { 
    io.vertx.rx.java.ObservableFuture<List<KeyValue>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getValues(keyPrefix, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.deleteValues(keyPrefix, resultHandler);
    return this;
  }

  public Observable<Void> deleteValuesObservable(String keyPrefix) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deleteValues(keyPrefix, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) { 
    delegate.putValue(key, value, resultHandler);
    return this;
  }

  public Observable<Boolean> putValueObservable(String key, String value) { 
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putValue(key, value, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient putValueWithOptions(String key, String value, KeyValueOptions options, Handler<AsyncResult<Boolean>> resultHandler) { 
    delegate.putValueWithOptions(key, value, options, resultHandler);
    return this;
  }

  public Observable<Boolean> putValueWithOptionsObservable(String key, String value, KeyValueOptions options) { 
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putValueWithOptions(key, value, options, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Create new Acl token
   * @param token properties of the token
   * @param idHandler will be provided with ID of created token
   * @return reference to this, for fluency
   */
  public ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) { 
    delegate.createAclToken(token, idHandler);
    return this;
  }

  /**
   * Create new Acl token
   * @param token properties of the token
   * @return 
   */
  public Observable<String> createAclTokenObservable(AclToken token) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createAclToken(token, idHandler.toHandler());
    return idHandler;
  }

  /**
   * Update Acl token
   * @param token properties of the token to be updated
   * @param idHandler will be provided with ID of updated
   * @return reference to this, for fluency
   */
  public ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) { 
    delegate.updateAclToken(token, idHandler);
    return this;
  }

  /**
   * Update Acl token
   * @param token properties of the token to be updated
   * @return 
   */
  public Observable<String> updateAclTokenObservable(AclToken token) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateAclToken(token, idHandler.toHandler());
    return idHandler;
  }

  /**
   * Clone Acl token
   * @param id the ID of token to be cloned
   * @param idHandler will be provided with ID of cloned token
   * @return reference to this, for fluency
   */
  public ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) { 
    delegate.cloneAclToken(id, idHandler);
    return this;
  }

  /**
   * Clone Acl token
   * @param id the ID of token to be cloned
   * @return 
   */
  public Observable<String> cloneAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    cloneAclToken(id, idHandler.toHandler());
    return idHandler;
  }

  /**
   * Get list of Acl token
   * @param resultHandler will be provided with list of tokens
   * @return reference to this, for fluency
   */
  public ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler) { 
    delegate.listAclTokens(resultHandler);
    return this;
  }

  /**
   * Get list of Acl token
   * @return 
   */
  public Observable<List<AclToken>> listAclTokensObservable() { 
    io.vertx.rx.java.ObservableFuture<List<AclToken>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listAclTokens(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Get info of Acl token
   * @param id the ID of token
   * @param tokenHandler will be provided with token
   * @return reference to this, for fluency
   */
  public ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) { 
    delegate.infoAclToken(id, tokenHandler);
    return this;
  }

  /**
   * Get info of Acl token
   * @param id the ID of token
   * @return 
   */
  public Observable<AclToken> infoAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<AclToken> tokenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoAclToken(id, tokenHandler.toHandler());
    return tokenHandler;
  }

  /**
   * Destroy Acl token
   * @param id the ID of token
   * @param resultHandler will be called on complete
   * @return reference to this, for fluency
   */
  public ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.destroyAclToken(id, resultHandler);
    return this;
  }

  /**
   * Destroy Acl token
   * @param id the ID of token
   * @return 
   */
  public Observable<Void> destroyAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    destroyAclToken(id, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Fires a new user event
   * @param name name of event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   */
  public ConsulClient fireEvent(String name, Handler<AsyncResult<Event>> resultHandler) { 
    delegate.fireEvent(name, resultHandler);
    return this;
  }

  /**
   * Fires a new user event
   * @param name name of event
   * @return 
   */
  public Observable<Event> fireEventObservable(String name) { 
    io.vertx.rx.java.ObservableFuture<Event> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    fireEvent(name, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Fires a new user event
   * @param name name of event
   * @param options options used to create event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   */
  public ConsulClient fireEventWithOptions(String name, EventOptions options, Handler<AsyncResult<Event>> resultHandler) { 
    delegate.fireEventWithOptions(name, options, resultHandler);
    return this;
  }

  /**
   * Fires a new user event
   * @param name name of event
   * @param options options used to create event
   * @return 
   */
  public Observable<Event> fireEventWithOptionsObservable(String name, EventOptions options) { 
    io.vertx.rx.java.ObservableFuture<Event> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    fireEventWithOptions(name, options, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the most recent events known by the agent
   * @param resultHandler will be provided with list of events
   * @return reference to this, for fluency
   */
  public ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler) { 
    delegate.listEvents(resultHandler);
    return this;
  }

  /**
   * Returns the most recent events known by the agent
   * @return 
   */
  public Observable<List<Event>> listEventsObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Event>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listEvents(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient registerService(ServiceOptions service, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.registerService(service, resultHandler);
    return this;
  }

  public Observable<Void> registerServiceObservable(ServiceOptions service) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerService(service, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient maintenanceService(MaintenanceOptions maintenanceOptions, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.maintenanceService(maintenanceOptions, resultHandler);
    return this;
  }

  public Observable<Void> maintenanceServiceObservable(MaintenanceOptions maintenanceOptions) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    maintenanceService(maintenanceOptions, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient deregisterService(String id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.deregisterService(id, resultHandler);
    return this;
  }

  public Observable<Void> deregisterServiceObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deregisterService(id, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the nodes providing a service
   * @param service name of service
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   */
  public ConsulClient catalogServiceNodes(String service, Handler<AsyncResult<List<Service>>> resultHandler) { 
    delegate.catalogServiceNodes(service, resultHandler);
    return this;
  }

  /**
   * Returns the nodes providing a service
   * @param service name of service
   * @return 
   */
  public Observable<List<Service>> catalogServiceNodesObservable(String service) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogServiceNodes(service, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the nodes providing a service, filtered by tag
   * @param service name of service
   * @param tag service tag
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   */
  public ConsulClient catalogServiceNodesWithTag(String service, String tag, Handler<AsyncResult<List<Service>>> resultHandler) { 
    delegate.catalogServiceNodesWithTag(service, tag, resultHandler);
    return this;
  }

  /**
   * Returns the nodes providing a service, filtered by tag
   * @param service name of service
   * @param tag service tag
   * @return 
   */
  public Observable<List<Service>> catalogServiceNodesWithTagObservable(String service, String tag) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogServiceNodesWithTag(service, tag, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Return all the datacenters that are known by the Consul server
   * @param resultHandler will be provided with list of datacenters
   * @return reference to this, for fluency
   */
  public ConsulClient catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler) { 
    delegate.catalogDatacenters(resultHandler);
    return this;
  }

  /**
   * Return all the datacenters that are known by the Consul server
   * @return 
   */
  public Observable<List<String>> catalogDatacentersObservable() { 
    io.vertx.rx.java.ObservableFuture<List<String>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogDatacenters(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the nodes registered in a datacenter
   * @param resultHandler will be provided with list of nodes
   * @return reference to this, for fluency
   */
  public ConsulClient catalogNodes(Handler<AsyncResult<List<Node>>> resultHandler) { 
    delegate.catalogNodes(resultHandler);
    return this;
  }

  /**
   * Returns the nodes registered in a datacenter
   * @return 
   */
  public Observable<List<Node>> catalogNodesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Node>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogNodes(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the services registered in a datacenter
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   */
  public ConsulClient catalogServices(Handler<AsyncResult<List<Service>>> resultHandler) { 
    delegate.catalogServices(resultHandler);
    return this;
  }

  /**
   * Returns the services registered in a datacenter
   * @return 
   */
  public Observable<List<Service>> catalogServicesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogServices(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the node's registered services
   * @param node node name
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   */
  public ConsulClient catalogNodeServices(String node, Handler<AsyncResult<List<Service>>> resultHandler) { 
    delegate.catalogNodeServices(node, resultHandler);
    return this;
  }

  /**
   * Returns the node's registered services
   * @param node node name
   * @return 
   */
  public Observable<List<Service>> catalogNodeServicesObservable(String node) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogNodeServices(node, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient localServices(Handler<AsyncResult<List<Service>>> resultHandler) { 
    delegate.localServices(resultHandler);
    return this;
  }

  public Observable<List<Service>> localServicesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    localServices(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient localChecks(Handler<AsyncResult<List<Check>>> resultHandler) { 
    delegate.localChecks(resultHandler);
    return this;
  }

  public Observable<List<Check>> localChecksObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Check>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    localChecks(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.registerCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> registerCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.deregisterCheck(id, resultHandler);
    return this;
  }

  public Observable<Void> deregisterCheckObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deregisterCheck(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.passCheck(checkId, resultHandler);
    return this;
  }

  public Observable<Void> passCheckObservable(String checkId) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    passCheck(checkId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.passCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  public Observable<Void> passCheckWithNoteObservable(String checkId, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    passCheckWithNote(checkId, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.warnCheck(checkId, resultHandler);
    return this;
  }

  public Observable<Void> warnCheckObservable(String checkId) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    warnCheck(checkId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.warnCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  public Observable<Void> warnCheckWithNoteObservable(String checkId, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    warnCheckWithNote(checkId, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.failCheck(checkId, resultHandler);
    return this;
  }

  public Observable<Void> failCheckObservable(String checkId) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    failCheck(checkId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.failCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  public Observable<Void> failCheckWithNoteObservable(String checkId, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    failCheckWithNote(checkId, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.updateCheck(checkId, status, resultHandler);
    return this;
  }

  public Observable<Void> updateCheckObservable(String checkId, CheckStatus status) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateCheck(checkId, status, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient updateCheckWithNote(String checkId, CheckStatus status, String note, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.updateCheckWithNote(checkId, status, note, resultHandler);
    return this;
  }

  public Observable<Void> updateCheckWithNoteObservable(String checkId, CheckStatus status, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateCheckWithNote(checkId, status, note, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Get the Raft leader for the datacenter in which the agent is running.
   * It returns an address in format "<code>10.1.10.12:8300</code>"
   * @param resultHandler will be provided with address of cluster leader
   * @return reference to this, for fluency
   */
  public ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler) { 
    delegate.leaderStatus(resultHandler);
    return this;
  }

  /**
   * Get the Raft leader for the datacenter in which the agent is running.
   * It returns an address in format "<code>10.1.10.12:8300</code>"
   * @return 
   */
  public Observable<String> leaderStatusObservable() { 
    io.vertx.rx.java.ObservableFuture<String> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    leaderStatus(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Retrieves the Raft peers for the datacenter in which the the agent is running.
   * It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
   * @param resultHandler will be provided with list of peers
   * @return reference to this, for fluency
   */
  public ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler) { 
    delegate.peersStatus(resultHandler);
    return this;
  }

  /**
   * Retrieves the Raft peers for the datacenter in which the the agent is running.
   * It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
   * @return 
   */
  public Observable<List<String>> peersStatusObservable() { 
    io.vertx.rx.java.ObservableFuture<List<String>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    peersStatus(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Initialize a new session
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   */
  public ConsulClient createSession(Handler<AsyncResult<String>> idHandler) { 
    delegate.createSession(idHandler);
    return this;
  }

  /**
   * Initialize a new session
   * @return 
   */
  public Observable<String> createSessionObservable() { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createSession(idHandler.toHandler());
    return idHandler;
  }

  /**
   * Initialize a new session
   * @param options options used to create session
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   */
  public ConsulClient createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler) { 
    delegate.createSessionWithOptions(options, idHandler);
    return this;
  }

  /**
   * Initialize a new session
   * @param options options used to create session
   * @return 
   */
  public Observable<String> createSessionWithOptionsObservable(SessionOptions options) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createSessionWithOptions(options, idHandler.toHandler());
    return idHandler;
  }

  /**
   * Returns the requested session information
   * @param id the ID of requested session
   * @param resultHandler will be provided with info of requested session
   * @return reference to this, for fluency
   */
  public ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler) { 
    delegate.infoSession(id, resultHandler);
    return this;
  }

  /**
   * Returns the requested session information
   * @param id the ID of requested session
   * @return 
   */
  public Observable<Session> infoSessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Session> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoSession(id, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
   * @param id the ID of session that should be renewed
   * @param resultHandler will be provided with info of renewed session
   * @return reference to this, for fluency
   */
  public ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler) { 
    delegate.renewSession(id, resultHandler);
    return this;
  }

  /**
   * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
   * @param id the ID of session that should be renewed
   * @return 
   */
  public Observable<Session> renewSessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Session> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    renewSession(id, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the active sessions
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   */
  public ConsulClient listSessions(Handler<AsyncResult<List<Session>>> resultHandler) { 
    delegate.listSessions(resultHandler);
    return this;
  }

  /**
   * Returns the active sessions
   * @return 
   */
  public Observable<List<Session>> listSessionsObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Session>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listSessions(resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Returns the active sessions for a given node
   * @param nodeId the ID of node
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   */
  public ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler) { 
    delegate.listNodeSessions(nodeId, resultHandler);
    return this;
  }

  /**
   * Returns the active sessions for a given node
   * @param nodeId the ID of node
   * @return 
   */
  public Observable<List<Session>> listNodeSessionsObservable(String nodeId) { 
    io.vertx.rx.java.ObservableFuture<List<Session>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listNodeSessions(nodeId, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Destroys the given session
   * @param id the ID of session
   * @param resultHandler will be called when complete
   * @return reference to this, for fluency
   */
  public ConsulClient destroySession(String id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.destroySession(id, resultHandler);
    return this;
  }

  /**
   * Destroys the given session
   * @param id the ID of session
   * @return 
   */
  public Observable<Void> destroySessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    destroySession(id, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Close the client and release its resources
   */
  public void close() { 
    delegate.close();
  }


  public static ConsulClient newInstance(io.vertx.ext.consul.ConsulClient arg) {
    return arg != null ? new ConsulClient(arg) : null;
  }
}
