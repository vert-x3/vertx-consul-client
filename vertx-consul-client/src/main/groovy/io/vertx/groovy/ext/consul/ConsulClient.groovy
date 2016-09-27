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
import io.vertx.ext.consul.MaintenanceOptions
import io.vertx.ext.consul.ServiceInfo
import io.vertx.ext.consul.CheckInfo
import io.vertx.ext.consul.CheckOptions
import io.vertx.ext.consul.ServiceOptions
import io.vertx.ext.consul.KeyValuePair
import java.util.List
import io.vertx.ext.consul.AclToken
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * A Vert.x service used to interact with Consul.
*/
@CompileStatic
public class ConsulClient {
  private final def io.vertx.ext.consul.ConsulClient delegate;
  public ConsulClient(Object delegate) {
    this.delegate = (io.vertx.ext.consul.ConsulClient) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static ConsulClient create(Vertx vertx, Map<String, Object> config) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.consul.ConsulClient.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, config != null ? new io.vertx.core.json.JsonObject(config) : null), io.vertx.groovy.ext.consul.ConsulClient.class);
    return ret;
  }
  public ConsulClient getValue(String key, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.getValue(key, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.KeyValuePair>>() {
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
  public ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler) {
    delegate.deleteValue(key, resultHandler);
    return this;
  }
  public ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.getValues(keyPrefix, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.KeyValuePair>>>() {
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
  public ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler) {
    delegate.deleteValues(keyPrefix, resultHandler);
    return this;
  }
  public ConsulClient putValue(String key, String value, Handler<AsyncResult<Void>> resultHandler) {
    delegate.putValue(key, value, resultHandler);
    return this;
  }
  public ConsulClient createAclToken(Map<String, Object> token = [:], Handler<AsyncResult<String>> idHandler) {
    delegate.createAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.InternalHelper.toJsonObject(token)) : null, idHandler);
    return this;
  }
  public ConsulClient updateAclToken(Map<String, Object> token = [:], Handler<AsyncResult<String>> idHandler) {
    delegate.updateAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.InternalHelper.toJsonObject(token)) : null, idHandler);
    return this;
  }
  public ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler) {
    delegate.cloneAclToken(id, idHandler);
    return this;
  }
  public ConsulClient listAclTokens(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.listAclTokens(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.AclToken>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.AclToken>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulClient infoAclToken(String id, Handler<AsyncResult<Map<String, Object>>> tokenHandler) {
    delegate.infoAclToken(id, tokenHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.AclToken>>() {
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
  public ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler) {
    delegate.destroyAclToken(id, resultHandler);
    return this;
  }
  public ConsulClient fireEvent(Map<String, Object> event = [:], Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.fireEvent(event != null ? new io.vertx.ext.consul.Event(io.vertx.lang.groovy.InternalHelper.toJsonObject(event)) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.Event>>() {
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
  public ConsulClient listEvents(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.listEvents(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Event>>>() {
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
  public ConsulClient registerService(Map<String, Object> service = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.registerService(service != null ? new io.vertx.ext.consul.ServiceOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(service)) : null, resultHandler);
    return this;
  }
  public ConsulClient maintenanceService(Map<String, Object> maintenanceOptions = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.maintenanceService(maintenanceOptions != null ? new io.vertx.ext.consul.MaintenanceOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(maintenanceOptions)) : null, resultHandler);
    return this;
  }
  public ConsulClient deregisterService(String id, Handler<AsyncResult<Void>> resultHandler) {
    delegate.deregisterService(id, resultHandler);
    return this;
  }
  public ConsulClient infoService(String name, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.infoService(name, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.ServiceInfo>>>() {
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
  public ConsulClient localServices(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.localServices(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.ServiceInfo>>>() {
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
  public ConsulClient localChecks(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.localChecks(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.CheckInfo>>>() {
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
  public ConsulClient registerCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.registerCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulClient deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler) {
    delegate.deregisterCheck(id, resultHandler);
    return this;
  }
  public ConsulClient passCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.passCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulClient warnCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.warnCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulClient failCheck(Map<String, Object> check = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.failCheck(check != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(check)) : null, resultHandler);
    return this;
  }
  public ConsulClient updateCheck(Map<String, Object> checkInfo = [:], Handler<AsyncResult<Void>> resultHandler) {
    delegate.updateCheck(checkInfo != null ? new io.vertx.ext.consul.CheckInfo(io.vertx.lang.groovy.InternalHelper.toJsonObject(checkInfo)) : null, resultHandler);
    return this;
  }
  public ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler) {
    delegate.leaderStatus(resultHandler);
    return this;
  }
  public ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler) {
    delegate.peersStatus(resultHandler != null ? new Handler<AsyncResult<java.util.List<java.lang.String>>>() {
      public void handle(AsyncResult<java.util.List<java.lang.String>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(ar.result()));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Close the client and release its resources
   */
  public void close() {
    delegate.close();
  }
}
