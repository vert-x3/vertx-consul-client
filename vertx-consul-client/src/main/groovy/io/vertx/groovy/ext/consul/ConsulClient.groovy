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
import io.vertx.ext.consul.MaintenanceOptions
import io.vertx.ext.consul.Check
import io.vertx.ext.consul.Service
import io.vertx.ext.consul.CheckOptions
import io.vertx.ext.consul.Coordinate
import io.vertx.ext.consul.KeyValue
import io.vertx.ext.consul.ServiceOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.ext.consul.Node
import io.vertx.ext.consul.BlockingQueryOptions
import io.vertx.ext.consul.CheckStatus
import io.vertx.groovy.core.Vertx
import java.util.List
import io.vertx.ext.consul.KeyValueOptions
import io.vertx.ext.consul.AclToken
import io.vertx.ext.consul.SessionOptions
import io.vertx.core.Handler
import io.vertx.ext.consul.DcCoordinates
import io.vertx.ext.consul.Session
import io.vertx.ext.consul.EventOptions
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
  /**
   * Create a Consul client.
   * @param vertx the Vert.x instance
   * @param config the configuration
   * @return the client
   */
  public static ConsulClient create(Vertx vertx, Map<String, Object> config) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.consul.ConsulClient.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, config != null ? new io.vertx.core.json.JsonObject(config) : null), io.vertx.groovy.ext.consul.ConsulClient.class);
    return ret;
  }
  /**
   * Returns the LAN network coordinates for all nodes in a given DC
   * @param resultHandler will be provided with network coordinates of nodes in datacenter
   * @return reference to this, for fluency
   */
  public ConsulClient coordinateNodes(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.coordinateNodes(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Coordinate>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Coordinate>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Returns the WAN network coordinates for all Consul servers, organized by DCs
   * @param resultHandler will be provided with network coordinates for all Consul servers
   * @return reference to this, for fluency
   */
  public ConsulClient coordinateDatacenters(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.coordinateDatacenters(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.DcCoordinates>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.DcCoordinates>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulClient getValue(String key, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.getValue(key, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.KeyValue>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.KeyValue> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulClient getValueBlocking(String key, Map<String, Object> options, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.getValueBlocking(key, options != null ? new io.vertx.ext.consul.BlockingQueryOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.KeyValue>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.KeyValue> ar) {
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
    delegate.getValues(keyPrefix, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  public ConsulClient getValuesBlocking(String keyPrefix, Map<String, Object> options, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.getValuesBlocking(keyPrefix, options != null ? new io.vertx.ext.consul.BlockingQueryOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>> ar) {
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
  public ConsulClient putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) {
    delegate.putValue(key, value, resultHandler);
    return this;
  }
  public ConsulClient putValueWithOptions(String key, String value, Map<String, Object> options, Handler<AsyncResult<Boolean>> resultHandler) {
    delegate.putValueWithOptions(key, value, options != null ? new io.vertx.ext.consul.KeyValueOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null, resultHandler);
    return this;
  }
  /**
   * Create new Acl token
   * @param token properties of the token (see <a href="../../../../../../../cheatsheet/AclToken.html">AclToken</a>)
   * @param idHandler will be provided with ID of created token
   * @return reference to this, for fluency
   */
  public ConsulClient createAclToken(Map<String, Object> token = [:], Handler<AsyncResult<String>> idHandler) {
    delegate.createAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.InternalHelper.toJsonObject(token)) : null, idHandler);
    return this;
  }
  /**
   * Update Acl token
   * @param token properties of the token to be updated (see <a href="../../../../../../../cheatsheet/AclToken.html">AclToken</a>)
   * @param idHandler will be provided with ID of updated
   * @return reference to this, for fluency
   */
  public ConsulClient updateAclToken(Map<String, Object> token = [:], Handler<AsyncResult<String>> idHandler) {
    delegate.updateAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.InternalHelper.toJsonObject(token)) : null, idHandler);
    return this;
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
   * Get list of Acl token
   * @param resultHandler will be provided with list of tokens
   * @return reference to this, for fluency
   */
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
  /**
   * Get info of Acl token
   * @param id the ID of token
   * @param tokenHandler will be provided with token
   * @return reference to this, for fluency
   */
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
   * Fires a new user event
   * @param name name of event
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   */
  public ConsulClient fireEvent(String name, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.fireEvent(name, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.Event>>() {
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
  /**
   * Fires a new user event
   * @param name name of event
   * @param options options used to create event (see <a href="../../../../../../../cheatsheet/EventOptions.html">EventOptions</a>)
   * @param resultHandler will be provided with properties of event
   * @return reference to this, for fluency
   */
  public ConsulClient fireEventWithOptions(String name, Map<String, Object> options, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.fireEventWithOptions(name, options != null ? new io.vertx.ext.consul.EventOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.Event>>() {
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
  /**
   * Returns the most recent events known by the agent
   * @param resultHandler will be provided with list of events
   * @return reference to this, for fluency
   */
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
  /**
   * Returns the nodes providing a service
   * @param service name of service
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   */
  public ConsulClient catalogServiceNodes(String service, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.catalogServiceNodes(service, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Returns the nodes providing a service, filtered by tag
   * @param service name of service
   * @param tag service tag
   * @param resultHandler will be provided with list of nodes providing given service
   * @return reference to this, for fluency
   */
  public ConsulClient catalogServiceNodesWithTag(String service, String tag, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.catalogServiceNodesWithTag(service, tag, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Return all the datacenters that are known by the Consul server
   * @param resultHandler will be provided with list of datacenters
   * @return reference to this, for fluency
   */
  public ConsulClient catalogDatacenters(Handler<AsyncResult<List<String>>> resultHandler) {
    delegate.catalogDatacenters(resultHandler != null ? new Handler<AsyncResult<java.util.List<java.lang.String>>>() {
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
   * Returns the nodes registered in a datacenter
   * @param resultHandler will be provided with list of nodes
   * @return reference to this, for fluency
   */
  public ConsulClient catalogNodes(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.catalogNodes(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Node>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Node>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Returns the services registered in a datacenter
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   */
  public ConsulClient catalogServices(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.catalogServices(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Returns the node's registered services
   * @param node node name
   * @param resultHandler will be provided with list of services
   * @return reference to this, for fluency
   */
  public ConsulClient catalogNodeServices(String node, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.catalogNodeServices(node, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
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
    delegate.localServices(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
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
    delegate.localChecks(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Check>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Check>> ar) {
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
  public ConsulClient passCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    delegate.passCheck(checkId, resultHandler);
    return this;
  }
  public ConsulClient passCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    delegate.passCheckWithNote(checkId, note, resultHandler);
    return this;
  }
  public ConsulClient warnCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    delegate.warnCheck(checkId, resultHandler);
    return this;
  }
  public ConsulClient warnCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    delegate.warnCheckWithNote(checkId, note, resultHandler);
    return this;
  }
  public ConsulClient failCheck(String checkId, Handler<AsyncResult<Void>> resultHandler) {
    delegate.failCheck(checkId, resultHandler);
    return this;
  }
  public ConsulClient failCheckWithNote(String checkId, String note, Handler<AsyncResult<Void>> resultHandler) {
    delegate.failCheckWithNote(checkId, note, resultHandler);
    return this;
  }
  public ConsulClient updateCheck(String checkId, CheckStatus status, Handler<AsyncResult<Void>> resultHandler) {
    delegate.updateCheck(checkId, status, resultHandler);
    return this;
  }
  public ConsulClient updateCheckWithNote(String checkId, CheckStatus status, String note, Handler<AsyncResult<Void>> resultHandler) {
    delegate.updateCheckWithNote(checkId, status, note, resultHandler);
    return this;
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
   * Retrieves the Raft peers for the datacenter in which the the agent is running.
   * It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
   * @param resultHandler will be provided with list of peers
   * @return reference to this, for fluency
   */
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
   * @param options options used to create session (see <a href="../../../../../../../cheatsheet/SessionOptions.html">SessionOptions</a>)
   * @param idHandler will be provided with ID of new session
   * @return reference to this, for fluency
   */
  public ConsulClient createSessionWithOptions(Map<String, Object> options = [:], Handler<AsyncResult<String>> idHandler) {
    delegate.createSessionWithOptions(options != null ? new io.vertx.ext.consul.SessionOptions(io.vertx.lang.groovy.InternalHelper.toJsonObject(options)) : null, idHandler);
    return this;
  }
  /**
   * Returns the requested session information
   * @param id the ID of requested session
   * @param resultHandler will be provided with info of requested session
   * @return reference to this, for fluency
   */
  public ConsulClient infoSession(String id, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.infoSession(id, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.Session>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.Session> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
   * @param id the ID of session that should be renewed
   * @param resultHandler will be provided with info of renewed session
   * @return reference to this, for fluency
   */
  public ConsulClient renewSession(String id, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.renewSession(id, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.consul.Session>>() {
      public void handle(AsyncResult<io.vertx.ext.consul.Session> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Returns the active sessions
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   */
  public ConsulClient listSessions(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.listSessions(resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Session>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Session>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
  }
  /**
   * Returns the active sessions for a given node
   * @param nodeId the ID of node
   * @param resultHandler will be provided with list of sessions
   * @return reference to this, for fluency
   */
  public ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.listNodeSessions(nodeId, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.consul.Session>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.consul.Session>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
    return this;
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
   * Close the client and release its resources
   */
  public void close() {
    delegate.close();
  }
}
