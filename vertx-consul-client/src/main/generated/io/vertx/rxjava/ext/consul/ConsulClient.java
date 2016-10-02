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
import io.vertx.ext.consul.ServiceInfo;
import io.vertx.ext.consul.CheckInfo;
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

  public ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) { 
    delegate.createAclToken(token, idHandler);
    return this;
  }

  public Observable<String> createAclTokenObservable(AclToken token) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createAclToken(token, idHandler.toHandler());
    return idHandler;
  }

  public ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler) { 
    delegate.updateAclToken(token, idHandler);
    return this;
  }

  public Observable<String> updateAclTokenObservable(AclToken token) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateAclToken(token, idHandler.toHandler());
    return idHandler;
  }

  public ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) { 
    delegate.cloneAclToken(id, idHandler);
    return this;
  }

  public Observable<String> cloneAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    cloneAclToken(id, idHandler.toHandler());
    return idHandler;
  }

  public ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler) { 
    delegate.listAclTokens(resultHandler);
    return this;
  }

  public Observable<List<AclToken>> listAclTokensObservable() { 
    io.vertx.rx.java.ObservableFuture<List<AclToken>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listAclTokens(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler) { 
    delegate.infoAclToken(id, tokenHandler);
    return this;
  }

  public Observable<AclToken> infoAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<AclToken> tokenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoAclToken(id, tokenHandler.toHandler());
    return tokenHandler;
  }

  public ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.destroyAclToken(id, resultHandler);
    return this;
  }

  public Observable<Void> destroyAclTokenObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    destroyAclToken(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler) { 
    delegate.fireEvent(event, resultHandler);
    return this;
  }

  public Observable<Event> fireEventObservable(Event event) { 
    io.vertx.rx.java.ObservableFuture<Event> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    fireEvent(event, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler) { 
    delegate.listEvents(resultHandler);
    return this;
  }

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

  public ConsulClient infoService(String name, Handler<AsyncResult<List<ServiceInfo>>> resultHandler) { 
    delegate.infoService(name, resultHandler);
    return this;
  }

  public Observable<List<ServiceInfo>> infoServiceObservable(String name) { 
    io.vertx.rx.java.ObservableFuture<List<ServiceInfo>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoService(name, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient localServices(Handler<AsyncResult<List<ServiceInfo>>> resultHandler) { 
    delegate.localServices(resultHandler);
    return this;
  }

  public Observable<List<ServiceInfo>> localServicesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<ServiceInfo>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    localServices(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient localChecks(Handler<AsyncResult<List<CheckInfo>>> resultHandler) { 
    delegate.localChecks(resultHandler);
    return this;
  }

  public Observable<List<CheckInfo>> localChecksObservable() { 
    io.vertx.rx.java.ObservableFuture<List<CheckInfo>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
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

  public ConsulClient passCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.passCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> passCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    passCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient warnCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.warnCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> warnCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    warnCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient failCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.failCheck(check, resultHandler);
    return this;
  }

  public Observable<Void> failCheckObservable(CheckOptions check) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    failCheck(check, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient updateCheck(CheckInfo checkInfo, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.updateCheck(checkInfo, resultHandler);
    return this;
  }

  public Observable<Void> updateCheckObservable(CheckInfo checkInfo) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    updateCheck(checkInfo, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler) { 
    delegate.leaderStatus(resultHandler);
    return this;
  }

  public Observable<String> leaderStatusObservable() { 
    io.vertx.rx.java.ObservableFuture<String> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    leaderStatus(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler) { 
    delegate.peersStatus(resultHandler);
    return this;
  }

  public Observable<List<String>> peersStatusObservable() { 
    io.vertx.rx.java.ObservableFuture<List<String>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    peersStatus(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient createSession(SessionOptions options, Handler<AsyncResult<String>> idHandler) { 
    delegate.createSession(options, idHandler);
    return this;
  }

  public Observable<String> createSessionObservable(SessionOptions options) { 
    io.vertx.rx.java.ObservableFuture<String> idHandler = io.vertx.rx.java.RxHelper.observableFuture();
    createSession(options, idHandler.toHandler());
    return idHandler;
  }

  public ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler) { 
    delegate.infoSession(id, resultHandler);
    return this;
  }

  public Observable<Session> infoSessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Session> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoSession(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler) { 
    delegate.renewSession(id, resultHandler);
    return this;
  }

  public Observable<Session> renewSessionObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Session> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    renewSession(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient listSessions(Handler<AsyncResult<List<Session>>> resultHandler) { 
    delegate.listSessions(resultHandler);
    return this;
  }

  public Observable<List<Session>> listSessionsObservable() { 
    io.vertx.rx.java.ObservableFuture<List<Session>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listSessions(resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler) { 
    delegate.listNodeSessions(nodeId, resultHandler);
    return this;
  }

  public Observable<List<Session>> listNodeSessionsObservable(String nodeId) { 
    io.vertx.rx.java.ObservableFuture<List<Session>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listNodeSessions(nodeId, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulClient destroySession(String id, Handler<AsyncResult<Void>> resultHandler) { 
    delegate.destroySession(id, resultHandler);
    return this;
  }

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
