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
import io.vertx.ext.consul.ServiceInfo;
import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.ext.consul.KeyValuePair;
import java.util.List;
import io.vertx.ext.consul.AclToken;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


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

  public ConsulService getValue(String key, Handler<AsyncResult<KeyValuePair>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).getValue(key, resultHandler);
    return this;
  }

  public Observable<KeyValuePair> getValueObservable(String key) { 
    io.vertx.rx.java.ObservableFuture<KeyValuePair> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
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

  public ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<KeyValuePair>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).getValues(keyPrefix, resultHandler);
    return this;
  }

  public Observable<List<KeyValuePair>> getValuesObservable(String keyPrefix) { 
    io.vertx.rx.java.ObservableFuture<List<KeyValuePair>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
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

  public ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).putValue(key, value, resultHandler);
    return this;
  }

  public Observable<Void> putValueObservable(String key, String value) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putValue(key, value, resultHandler.toHandler());
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

  public ConsulService deregisterService(String id, Handler<AsyncResult<Void>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).deregisterService(id, resultHandler);
    return this;
  }

  public Observable<Void> deregisterServiceObservable(String id) { 
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deregisterService(id, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService infoService(String name, Handler<AsyncResult<List<ServiceInfo>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).infoService(name, resultHandler);
    return this;
  }

  public Observable<List<ServiceInfo>> infoServiceObservable(String name) { 
    io.vertx.rx.java.ObservableFuture<List<ServiceInfo>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    infoService(name, resultHandler.toHandler());
    return resultHandler;
  }

  public ConsulService localServices(Handler<AsyncResult<List<ServiceInfo>>> resultHandler) { 
    ((io.vertx.ext.consul.ConsulService) delegate).localServices(resultHandler);
    return this;
  }

  public Observable<List<ServiceInfo>> localServicesObservable() { 
    io.vertx.rx.java.ObservableFuture<List<ServiceInfo>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    localServices(resultHandler.toHandler());
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

  public void close() { 
    ((io.vertx.ext.consul.ConsulService) delegate).close();
  }


  public static ConsulService newInstance(io.vertx.ext.consul.ConsulService arg) {
    return arg != null ? new ConsulService(arg) : null;
  }
}
