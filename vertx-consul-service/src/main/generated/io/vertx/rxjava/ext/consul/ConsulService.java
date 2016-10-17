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
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.Session;
import io.vertx.ext.consul.EventOptions;
import io.vertx.ext.consul.Node;


public class ConsulService extends ConsulClient {

  final io.vertx.ext.consul.ConsulService delegate;

  public ConsulService(io.vertx.ext.consul.ConsulService delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a proxy to a service that is deployed somewhere on the event bus
   * @param vertx the Vert.x instance
   * @param address the address the service is listening on on the event bus
   * @return the service
   */
  public static ConsulService createEventBusProxy(Vertx vertx, String address) { 
    ConsulService ret = ConsulService.newInstance(io.vertx.ext.consul.ConsulService.createEventBusProxy((io.vertx.core.Vertx)vertx.getDelegate(), address));
    return ret;
  }

  public ConsulService getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).getValue(key, resultHandler);
    return this;
  }

  public Observable<KeyValue> getValueObservable(String key) { 
    io.vertx.rx.java.ObservableFuture<KeyValue> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getValue(key, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).deleteValue(key, resultHandler);
    return this;
  }

  public Observable<Void> deleteValueObservable(String key) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deleteValue(key, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<KeyValue>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).getValues(keyPrefix, resultHandler);
    return this;
  }

  public Observable<List<KeyValue>> getValuesObservable(String keyPrefix) { 
    io.vertx.rx.java.ObservableFuture<List<KeyValue>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getValues(keyPrefix, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).deleteValues(keyPrefix, resultHandler);
    return this;
  }

  public Observable<Void> deleteValuesObservable(String keyPrefix) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deleteValues(keyPrefix, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).putValue(key, value, resultHandler);
    return this;
  }

  public Observable<Boolean> putValueObservable(String key, String value) { 
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putValue(key, value, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService putValueWithOptions(String key, String value, KeyValueOptions options, Handler<AsyncResult<Boolean>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).putValueWithOptions(key, value, options, resultHandler);
    return this;
  }

  public Observable<Boolean> putValueWithOptionsObservable(String key, String value, KeyValueOptions options) { 
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putValueWithOptions(key, value, options, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).createAclToken(token, idHandler);
    return this;
  }

  public Observable<String> createAclTokenObservable(AclToken token) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createAclToken(token, idHandler.toHandler());
    return idHandler;
  }

  public ConsulService updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).updateAclToken(token, idHandler);
    return this;
  }

  public Observable<String> updateAclTokenObservable(AclToken token) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateAclToken(token, idHandler.toHandler());
    return idHandler;
  }

  public ConsulService cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).cloneAclToken(id, idHandler);
    return this;
  }

  public Observable<String> cloneAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    cloneAclToken(id, idHandler.toHandler());
    return idHandler;
  }

  public ConsulService listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).listAclTokens(resultHandler);
    return this;
  }

  public Observable<List<AclToken>> listAclTokensObservable() { 
    io.vertx.rx.java.ObservableFuture<List<AclToken>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listAclTokens(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).infoAclToken(id, tokenHandler);
    return this;
  }

  public Observable<AclToken> infoAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<AclToken> tokenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoAclToken(id, tokenHandler.toHandler());
    return tokenHandler;
  }

  public ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).destroyAclToken(id, resultHandler);
    return this;
  }

  public Observable<Void> destroyAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    destroyAclToken(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService fireEvent(String name, Handler<AsyncResult<Event>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).fireEvent(name, resultHandler);
    return this;
  }

  public Observable<Event> fireEventObservable(String name) { 
    io.vertx.rx.java.ObservableFuture<Event> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    fireEvent(name, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService fireEventWithOptions(String name, EventOptions options, Handler<AsyncResult<Event>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).fireEventWithOptions(name, options, resultHandler);
    return this;
  }

  public Observable<Event> fireEventWithOptionsObservable(String name, EventOptions options) { 
    io.vertx.rx.java.ObservableFuture<Event> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    fireEventWithOptions(name, options, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService listEvents(Handler<AsyncResult<List<Event>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).listEvents(resultHandler);
    return this;
  }

  public Observable<List<Event>> listEventsObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Event>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listEvents(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService registerService(ServiceOptions service, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).registerService(service, resultHandler);
    return this;
  }

  public Observable<Void> registerServiceObservable(ServiceOptions service) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerService(service, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService maintenanceService(MaintenanceOptions maintenanceOptions, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).maintenanceService(maintenanceOptions, resultHandler);
    return this;
  }

  public Observable<Void> maintenanceServiceObservable(MaintenanceOptions maintenanceOptions) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    maintenanceService(maintenanceOptions, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService deregisterService(String id, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).deregisterService(id, resultHandler);
    return this;
  }

  public Observable<Void> deregisterServiceObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deregisterService(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService catalogServiceNodes(String service, Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).catalogServiceNodes(service, resultHandler);
    return this;
  }

  public Observable<List<Service>> catalogServiceNodesObservable(String service) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogServiceNodes(service, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService catalogServiceNodesWithTag(String service, String tag, Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).catalogServiceNodesWithTag(service, tag, resultHandler);
    return this;
  }

  public Observable<List<Service>> catalogServiceNodesWithTagObservable(String service, String tag) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogServiceNodesWithTag(service, tag, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).catalogDatacenters(resultHandler);
    return this;
  }

  public Observable<List<String>> catalogDatacentersObservable() { 
    io.vertx.rx.java.ObservableFuture<List<String>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogDatacenters(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService catalogNodes(Handler<AsyncResult<List<Node>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).catalogNodes(resultHandler);
    return this;
  }

  public Observable<List<Node>> catalogNodesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Node>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogNodes(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService catalogServices(Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).catalogServices(resultHandler);
    return this;
  }

  public Observable<List<Service>> catalogServicesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogServices(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService localServices(Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).localServices(resultHandler);
    return this;
  }

  public Observable<List<Service>> localServicesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    localServices(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService catalogNodeServices(String node, Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).catalogNodeServices(node, resultHandler);
    return this;
  }

  public Observable<List<Service>> catalogNodeServicesObservable(String node) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    catalogNodeServices(node, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService localChecks(Handler<AsyncResult<List<Check>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).localChecks(resultHandler);
    return this;
  }

  public Observable<List<Check>> localChecksObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Check>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    localChecks(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).registerCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> registerCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).deregisterCheck(id, resultHandler);
    return this;
  }

  public Observable<Void> deregisterCheckObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deregisterCheck(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).passCheck(checkId, resultHandler);
    return this;
  }

  public Observable<Void> passCheckObservable(String checkId) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    passCheck(checkId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).passCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  public Observable<Void> passCheckWithNoteObservable(String checkId, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    passCheckWithNote(checkId, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).warnCheck(checkId, resultHandler);
    return this;
  }

  public Observable<Void> warnCheckObservable(String checkId) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    warnCheck(checkId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).warnCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  public Observable<Void> warnCheckWithNoteObservable(String checkId, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    warnCheckWithNote(checkId, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).failCheck(checkId, resultHandler);
    return this;
  }

  public Observable<Void> failCheckObservable(String checkId) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    failCheck(checkId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).failCheckWithNote(checkId, note, resultHandler);
    return this;
  }

  public Observable<Void> failCheckWithNoteObservable(String checkId, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    failCheckWithNote(checkId, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).updateCheck(checkId, status, resultHandler);
    return this;
  }

  public Observable<Void> updateCheckObservable(String checkId, CheckStatus status) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateCheck(checkId, status, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService updateCheckWithNote(String checkId, CheckStatus status, String note, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).updateCheckWithNote(checkId, status, note, resultHandler);
    return this;
  }

  public Observable<Void> updateCheckWithNoteObservable(String checkId, CheckStatus status, String note) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateCheckWithNote(checkId, status, note, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService leaderStatus(Handler<AsyncResult<String>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).leaderStatus(resultHandler);
    return this;
  }

  public Observable<String> leaderStatusObservable() { 
    io.vertx.rx.java.ObservableFuture<String> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    leaderStatus(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService peersStatus(Handler<AsyncResult<List<String>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).peersStatus(resultHandler);
    return this;
  }

  public Observable<List<String>> peersStatusObservable() { 
    io.vertx.rx.java.ObservableFuture<List<String>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    peersStatus(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService createSession(Handler<AsyncResult<String>> idHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).createSession(idHandler);
    return this;
  }

  public Observable<String> createSessionObservable() { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createSession(idHandler.toHandler());
    return idHandler;
  }

  public ConsulService createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).createSessionWithOptions(options, idHandler);
    return this;
  }

  public Observable<String> createSessionWithOptionsObservable(SessionOptions options) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createSessionWithOptions(options, idHandler.toHandler());
    return idHandler;
  }

  public ConsulService infoSession(String id, Handler<AsyncResult<Session>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).infoSession(id, resultHandler);
    return this;
  }

  public Observable<Session> infoSessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Session> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoSession(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService renewSession(String id, Handler<AsyncResult<Session>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).renewSession(id, resultHandler);
    return this;
  }

  public Observable<Session> renewSessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Session> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    renewSession(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService listSessions(Handler<AsyncResult<List<Session>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).listSessions(resultHandler);
    return this;
  }

  public Observable<List<Session>> listSessionsObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Session>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listSessions(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).listNodeSessions(nodeId, resultHandler);
    return this;
  }

  public Observable<List<Session>> listNodeSessionsObservable(String nodeId) { 
    io.vertx.rx.java.ObservableFuture<List<Session>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listNodeSessions(nodeId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService destroySession(String id, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).destroySession(id, resultHandler);
    return this;
  }

  public Observable<Void> destroySessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    destroySession(id, resultHandler.toHandler());
    return resultHandler;
  }

  public void close() { 
    ((io.vertx.ext.consul.ConsulService) delegate).close();
  }


  public static ConsulService newInstance(io.vertx.ext.consul.ConsulService arg) {
    return arg != null ? new ConsulService(arg) : null;
  }
}
