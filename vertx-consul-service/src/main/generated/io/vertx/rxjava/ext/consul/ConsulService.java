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
import io.vertx.ext.consul.Event;
import io.vertx.rxjava.core.Vertx;
import io.vertx.ext.consul.MaintenanceOptions;
import io.vertx.ext.consul.CheckInfo;
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

  public ConsulService fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).fireEvent(event, resultHandler);
    return this;
  }

  public Observable<Event> fireEventObservable(Event event) { 
    io.vertx.rx.java.ObservableFuture<Event> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    fireEvent(event, resultHandler.toHandler());
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

  public ConsulService infoService(String name, Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).infoService(name, resultHandler);
    return this;
  }

  public Observable<List<Service>> infoServiceObservable(String name) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoService(name, resultHandler.toHandler());
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

  public ConsulService nodeServices(String nodeId, Handler<AsyncResult<List<Service>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).nodeServices(nodeId, resultHandler);
    return this;
  }

  public Observable<List<Service>> nodeServicesObservable(String nodeId) { 
    io.vertx.rx.java.ObservableFuture<List<Service>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    nodeServices(nodeId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService localChecks(Handler<AsyncResult<List<CheckInfo>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).localChecks(resultHandler);
    return this;
  }

  public Observable<List<CheckInfo>> localChecksObservable() { 
    io.vertx.rx.java.ObservableFuture<List<CheckInfo>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
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

  public ConsulService passCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).passCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> passCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    passCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService warnCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).warnCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> warnCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    warnCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService failCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).failCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> failCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    failCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService updateCheck(CheckInfo checkInfo, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).updateCheck(checkInfo, resultHandler);
    return this;
  }

  public Observable<Void> updateCheckObservable(CheckInfo checkInfo) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateCheck(checkInfo, resultHandler.toHandler());
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
