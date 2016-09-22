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

/** @module vertx-consul-js/consul_service */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var ConsulClient = require('vertx-consul-js/consul_client');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JConsulService = io.vertx.ext.consul.ConsulService;
var ServiceOptions = io.vertx.ext.consul.ServiceOptions;
var KeyValuePair = io.vertx.ext.consul.KeyValuePair;
var AclToken = io.vertx.ext.consul.AclToken;
var Event = io.vertx.ext.consul.Event;
var ServiceInfo = io.vertx.ext.consul.ServiceInfo;
var CheckInfo = io.vertx.ext.consul.CheckInfo;
var CheckOptions = io.vertx.ext.consul.CheckOptions;

/**
 @class
*/
var ConsulService = function(j_val) {

  var j_consulService = j_val;
  var that = this;
  ConsulClient.call(this, j_val);

  /**

   @public
   @param key {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.getValue = function(key, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["getValue(java.lang.String,io.vertx.core.Handler)"](key, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param key {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.deleteValue = function(key, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["deleteValue(java.lang.String,io.vertx.core.Handler)"](key, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param keyPrefix {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.getValues = function(keyPrefix, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["getValues(java.lang.String,io.vertx.core.Handler)"](keyPrefix, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param keyPrefix {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.deleteValues = function(keyPrefix, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["deleteValues(java.lang.String,io.vertx.core.Handler)"](keyPrefix, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param key {string} 
   @param value {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.putValue = function(key, value, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulService["putValue(java.lang.String,java.lang.String,io.vertx.core.Handler)"](key, value, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param token {Object} 
   @param idHandler {function} 
   @return {ConsulService}
   */
  this.createAclToken = function(token, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["createAclToken(io.vertx.ext.consul.AclToken,io.vertx.core.Handler)"](token != null ? new AclToken(new JsonObject(JSON.stringify(token))) : null, function(ar) {
      if (ar.succeeded()) {
        idHandler(ar.result(), null);
      } else {
        idHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param token {Object} 
   @param idHandler {function} 
   @return {ConsulService}
   */
  this.updateAclToken = function(token, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["updateAclToken(io.vertx.ext.consul.AclToken,io.vertx.core.Handler)"](token != null ? new AclToken(new JsonObject(JSON.stringify(token))) : null, function(ar) {
      if (ar.succeeded()) {
        idHandler(ar.result(), null);
      } else {
        idHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @param idHandler {function} 
   @return {ConsulService}
   */
  this.cloneAclToken = function(id, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["cloneAclToken(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
      if (ar.succeeded()) {
        idHandler(ar.result(), null);
      } else {
        idHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.listAclTokens = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["listAclTokens(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @param tokenHandler {function} 
   @return {ConsulService}
   */
  this.infoAclToken = function(id, tokenHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["infoAclToken(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
      if (ar.succeeded()) {
        tokenHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        tokenHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.destroyAclToken = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["destroyAclToken(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param event {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.fireEvent = function(event, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["fireEvent(io.vertx.ext.consul.Event,io.vertx.core.Handler)"](event != null ? new Event(new JsonObject(JSON.stringify(event))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.listEvents = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["listEvents(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param service {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.registerService = function(service, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["registerService(io.vertx.ext.consul.ServiceOptions,io.vertx.core.Handler)"](service != null ? new ServiceOptions(new JsonObject(JSON.stringify(service))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.deregisterService = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["deregisterService(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param name {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.infoService = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["infoService(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.localServices = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["localServices(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.localChecks = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["localChecks(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param check {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.registerCheck = function(check, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["registerCheck(io.vertx.ext.consul.CheckOptions,io.vertx.core.Handler)"](check != null ? new CheckOptions(new JsonObject(JSON.stringify(check))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.deregisterCheck = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["deregisterCheck(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param check {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.passCheck = function(check, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["passCheck(io.vertx.ext.consul.CheckOptions,io.vertx.core.Handler)"](check != null ? new CheckOptions(new JsonObject(JSON.stringify(check))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param check {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.warnCheck = function(check, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["warnCheck(io.vertx.ext.consul.CheckOptions,io.vertx.core.Handler)"](check != null ? new CheckOptions(new JsonObject(JSON.stringify(check))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param check {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.failCheck = function(check, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["failCheck(io.vertx.ext.consul.CheckOptions,io.vertx.core.Handler)"](check != null ? new CheckOptions(new JsonObject(JSON.stringify(check))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param checkInfo {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.updateCheck = function(checkInfo, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["updateCheck(io.vertx.ext.consul.CheckInfo,io.vertx.core.Handler)"](checkInfo != null ? new CheckInfo(new JsonObject(JSON.stringify(checkInfo))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_consulService["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_consulService;
};

/**
 Create a proxy to a service that is deployed somewhere on the event bus

 @memberof module:vertx-consul-js/consul_service
 @param vertx {Vertx} the Vert.x instance 
 @param address {string} the address the service is listening on on the event bus 
 @return {ConsulService} the service
 */
ConsulService.createEventBusProxy = function(vertx, address) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(JConsulService["createEventBusProxy(io.vertx.core.Vertx,java.lang.String)"](vertx._jdel, address), ConsulService);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = ConsulService;