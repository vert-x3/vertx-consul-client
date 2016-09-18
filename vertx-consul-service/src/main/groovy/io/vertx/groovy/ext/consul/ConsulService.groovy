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

package io.vertx.groovy.ext.consul;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.ext.consul.Event
import io.vertx.groovy.core.Vertx
import io.vertx.ext.consul.ServiceInfo
import io.vertx.ext.consul.CheckInfo
import io.vertx.ext.consul.CheckOptions
import io.vertx.ext.consul.ServiceOptions
import io.vertx.ext.consul.KeyValuePair
import java.util.List
import io.vertx.ext.consul.AclToken
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
@CompileStatic
public class ConsulService extends ConsulClient {
  private final def io.vertx.ext.consul.ConsulService delegate;
  public ConsulService(Object delegate) {
    super((io.vertx.ext.consul.ConsulService) delegate);
    this.delegate = (io.vertx.ext.consul.ConsulService) delegate;
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
    def ret = InternalHelper.safeCreate(io.vertx.ext.consul.ConsulService.createEventBusProxy(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, address), io.vertx.groovy.ext.consul.ConsulService.class);
    return ret;
  }
  public ConsulService getValue(String key, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).getValue(key, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.KeyValuePair>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.KeyValuePair> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).deleteValue(key, resultHandler);
    return this;
  }
  public ConsulService getValues(String keyPrefix, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).getValues(keyPrefix, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.KeyValuePair>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.KeyValuePair>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).deleteValues(keyPrefix, resultHandler);
    return this;
  }
  public ConsulService putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).putValue(key, value, resultHandler);
    return this;
  }
  public ConsulService createAclToken(Map<String, Object> token = [:], Handler<AsyncResult<String>> idHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).createAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.InternalHelper.toJsonObject(token)) : null, idHandler);
    return this;
  }
  public ConsulService infoAclToken(String id, Handler<AsyncResult<Map<String, Object>>> tokenHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).infoAclToken(id, tokenHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.AclToken>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.AclToken> ar) {
        if (ar.succeeded()) {
          tokenHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          tokenHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).destroyAclToken(id, resultHandler);
    return this;
  }
  public ConsulService fireEvent(Map<String, Object> event = [:], Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).fireEvent(event != null ? new io.vertx.ext.consul.Event(io.vertx.lang.groovy.InternalHelper.toJsonObject(event)) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.Event>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.Event> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService listEvents(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).listEvents(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Event>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Event>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService registerService(Map<String, Object> service = [:], Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).registerService(service != null ? new io.vertx.ext.consul.ServiceOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(service)) : null, resultHandler);
    return this;
  }
  public ConsulService infoService(String name, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).infoService(name, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.ServiceInfo>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.ServiceInfo>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService localServices(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).localServices(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.ServiceInfo>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.ServiceInfo>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService localChecks(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).localChecks(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.CheckInfo>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.CheckInfo>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulService registerCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).registerCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulService deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).deregisterCheck(id, resultHandler);
    return this;
  }
  public ConsulService passCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).passCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulService warnCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).warnCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulService failCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).failCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulService updateCheck(Map<String, Object> checkInfo = [:], Handler<AsyncResult<Void>> resultHandler) {
    ((io.vertx.ext.consul.ConsulService) delegate).updateCheck(checkInfo != null ? new io.vertx.ext.consul.CheckInfo(io.vertx.lang.groovy.InternalHelper.toJsonObject(checkInfo)) : null, resultHandler);
    return this;
  }
  public void close() {
    ((io.vertx.ext.consul.ConsulService) delegate).close();
  }
}
