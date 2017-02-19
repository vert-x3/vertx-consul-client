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
var JConsulService = Java.type('io.vertx.ext.consul.ConsulService');
var ServiceQueryOptions = Java.type('io.vertx.ext.consul.ServiceQueryOptions');
var Event = Java.type('io.vertx.ext.consul.Event');
var EventListOptions = Java.type('io.vertx.ext.consul.EventListOptions');
var SessionList = Java.type('io.vertx.ext.consul.SessionList');
var MaintenanceOptions = Java.type('io.vertx.ext.consul.MaintenanceOptions');
var Check = Java.type('io.vertx.ext.consul.Check');
var Service = Java.type('io.vertx.ext.consul.Service');
var CheckOptions = Java.type('io.vertx.ext.consul.CheckOptions');
var ServiceEntryList = Java.type('io.vertx.ext.consul.ServiceEntryList');
var NodeQueryOptions = Java.type('io.vertx.ext.consul.NodeQueryOptions');
var KeyValue = Java.type('io.vertx.ext.consul.KeyValue');
var ServiceOptions = Java.type('io.vertx.ext.consul.ServiceOptions');
var TxnRequest = Java.type('io.vertx.ext.consul.TxnRequest');
var ServiceList = Java.type('io.vertx.ext.consul.ServiceList');
var BlockingQueryOptions = Java.type('io.vertx.ext.consul.BlockingQueryOptions');
var NodeList = Java.type('io.vertx.ext.consul.NodeList');
var TxnResponse = Java.type('io.vertx.ext.consul.TxnResponse');
var EventList = Java.type('io.vertx.ext.consul.EventList');
var CoordinateList = Java.type('io.vertx.ext.consul.CoordinateList');
var KeyValueOptions = Java.type('io.vertx.ext.consul.KeyValueOptions');
var AclToken = Java.type('io.vertx.ext.consul.AclToken');
var SessionOptions = Java.type('io.vertx.ext.consul.SessionOptions');
var KeyValueList = Java.type('io.vertx.ext.consul.KeyValueList');
var DcCoordinates = Java.type('io.vertx.ext.consul.DcCoordinates');
var Session = Java.type('io.vertx.ext.consul.Session');
var EventOptions = Java.type('io.vertx.ext.consul.EventOptions');

/**
 @class
*/
var ConsulService = function(j_val) {

  var j_consulService = j_val;
  var that = this;
  ConsulClient.call(this, j_val);

  /**

   @public
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.agentInfo = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["agentInfo(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnJson(ar.result()), null);
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
  this.coordinateNodes = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["coordinateNodes(io.vertx.core.Handler)"](function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.coordinateNodesWithOptions = function(options, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["coordinateNodesWithOptions(io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
  this.coordinateDatacenters = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["coordinateDatacenters(io.vertx.core.Handler)"](function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.getValueWithOptions = function(key, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["getValueWithOptions(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](key, options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param keyPrefix {string} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.getValuesWithOptions = function(keyPrefix, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["getValuesWithOptions(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](keyPrefix, options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
        resultHandler(ar.result(), null);
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.putValueWithOptions = function(key, value, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && (typeof __args[2] === 'object' && __args[2] != null) && typeof __args[3] === 'function') {
      j_consulService["putValueWithOptions(java.lang.String,java.lang.String,io.vertx.ext.consul.KeyValueOptions,io.vertx.core.Handler)"](key, value, options != null ? new KeyValueOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param request {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.transaction = function(request, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["transaction(io.vertx.ext.consul.TxnRequest,io.vertx.core.Handler)"](request != null ? new TxnRequest(new JsonObject(Java.asJSONCompatible(request))) : null, function(ar) {
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
   @param token {Object} 
   @param idHandler {function} 
   @return {ConsulService}
   */
  this.createAclToken = function(token, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["createAclToken(io.vertx.ext.consul.AclToken,io.vertx.core.Handler)"](token != null ? new AclToken(new JsonObject(Java.asJSONCompatible(token))) : null, function(ar) {
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
      j_consulService["updateAclToken(io.vertx.ext.consul.AclToken,io.vertx.core.Handler)"](token != null ? new AclToken(new JsonObject(Java.asJSONCompatible(token))) : null, function(ar) {
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
   @param name {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.fireEvent = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["fireEvent(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
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
   @param name {string} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.fireEventWithOptions = function(name, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["fireEventWithOptions(java.lang.String,io.vertx.ext.consul.EventOptions,io.vertx.core.Handler)"](name, options != null ? new EventOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.listEventsWithOptions = function(options, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["listEventsWithOptions(io.vertx.ext.consul.EventListOptions,io.vertx.core.Handler)"](options != null ? new EventListOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param serviceOptions {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.registerService = function(serviceOptions, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["registerService(io.vertx.ext.consul.ServiceOptions,io.vertx.core.Handler)"](serviceOptions != null ? new ServiceOptions(new JsonObject(Java.asJSONCompatible(serviceOptions))) : null, function(ar) {
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
   @param maintenanceOptions {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.maintenanceService = function(maintenanceOptions, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["maintenanceService(io.vertx.ext.consul.MaintenanceOptions,io.vertx.core.Handler)"](maintenanceOptions != null ? new MaintenanceOptions(new JsonObject(Java.asJSONCompatible(maintenanceOptions))) : null, function(ar) {
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
   @param service {string} 
   @param passing {boolean} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.healthServiceNodes = function(service, passing, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean' && typeof __args[2] === 'function') {
      j_consulService["healthServiceNodes(java.lang.String,boolean,io.vertx.core.Handler)"](service, passing, function(ar) {
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
   @param service {string} 
   @param passing {boolean} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.healthServiceNodesWithOptions = function(service, passing, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean' && (typeof __args[2] === 'object' && __args[2] != null) && typeof __args[3] === 'function') {
      j_consulService["healthServiceNodesWithOptions(java.lang.String,boolean,io.vertx.ext.consul.ServiceQueryOptions,io.vertx.core.Handler)"](service, passing, options != null ? new ServiceQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param service {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.catalogServiceNodes = function(service, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["catalogServiceNodes(java.lang.String,io.vertx.core.Handler)"](service, function(ar) {
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
   @param service {string} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.catalogServiceNodesWithOptions = function(service, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["catalogServiceNodesWithOptions(java.lang.String,io.vertx.ext.consul.ServiceQueryOptions,io.vertx.core.Handler)"](service, options != null ? new ServiceQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
  this.catalogDatacenters = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["catalogDatacenters(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
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
  this.catalogNodes = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["catalogNodes(io.vertx.core.Handler)"](function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.catalogNodesWithOptions = function(options, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["catalogNodesWithOptions(io.vertx.ext.consul.NodeQueryOptions,io.vertx.core.Handler)"](options != null ? new NodeQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
  this.catalogServices = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["catalogServices(io.vertx.core.Handler)"](function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.catalogServicesWithOptions = function(options, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["catalogServicesWithOptions(io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param node {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.catalogNodeServices = function(node, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["catalogNodeServices(java.lang.String,io.vertx.core.Handler)"](node, function(ar) {
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
   @param node {string} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.catalogNodeServicesWithOptions = function(node, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["catalogNodeServicesWithOptions(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](node, options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param checkOptions {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.registerCheck = function(checkOptions, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["registerCheck(io.vertx.ext.consul.CheckOptions,io.vertx.core.Handler)"](checkOptions != null ? new CheckOptions(new JsonObject(Java.asJSONCompatible(checkOptions))) : null, function(ar) {
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
   @param checkId {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.deregisterCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["deregisterCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @param checkId {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.passCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["passCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @param checkId {string} 
   @param note {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.passCheckWithNote = function(checkId, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulService["passCheckWithNote(java.lang.String,java.lang.String,io.vertx.core.Handler)"](checkId, note, function(ar) {
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
   @param checkId {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.warnCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["warnCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @param checkId {string} 
   @param note {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.warnCheckWithNote = function(checkId, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulService["warnCheckWithNote(java.lang.String,java.lang.String,io.vertx.core.Handler)"](checkId, note, function(ar) {
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
   @param checkId {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.failCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["failCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @param checkId {string} 
   @param note {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.failCheckWithNote = function(checkId, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulService["failCheckWithNote(java.lang.String,java.lang.String,io.vertx.core.Handler)"](checkId, note, function(ar) {
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
   @param checkId {string} 
   @param status {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.updateCheck = function(checkId, status, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulService["updateCheck(java.lang.String,io.vertx.ext.consul.CheckStatus,io.vertx.core.Handler)"](checkId, io.vertx.ext.consul.CheckStatus.valueOf(status), function(ar) {
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
   @param checkId {string} 
   @param status {Object} 
   @param note {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.updateCheckWithNote = function(checkId, status, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'string' && typeof __args[3] === 'function') {
      j_consulService["updateCheckWithNote(java.lang.String,io.vertx.ext.consul.CheckStatus,java.lang.String,io.vertx.core.Handler)"](checkId, io.vertx.ext.consul.CheckStatus.valueOf(status), note, function(ar) {
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
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.leaderStatus = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["leaderStatus(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
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
  this.peersStatus = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["peersStatus(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(ar.result(), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param idHandler {function} 
   @return {ConsulService}
   */
  this.createSession = function(idHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["createSession(io.vertx.core.Handler)"](function(ar) {
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
   @param options {Object} 
   @param idHandler {function} 
   @return {ConsulService}
   */
  this.createSessionWithOptions = function(options, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["createSessionWithOptions(io.vertx.ext.consul.SessionOptions,io.vertx.core.Handler)"](options != null ? new SessionOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.infoSession = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["infoSession(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   @param id {string} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.infoSessionWithOptions = function(id, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["infoSessionWithOptions(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](id, options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param id {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.renewSession = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["renewSession(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
  this.listSessions = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulService["listSessions(io.vertx.core.Handler)"](function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.listSessionsWithOptions = function(options, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulService["listSessionsWithOptions(io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param nodeId {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.listNodeSessions = function(nodeId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["listNodeSessions(java.lang.String,io.vertx.core.Handler)"](nodeId, function(ar) {
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
   @param nodeId {string} 
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.listNodeSessionsWithOptions = function(nodeId, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulService["listNodeSessionsWithOptions(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](nodeId, options != null ? new BlockingQueryOptions(new JsonObject(Java.asJSONCompatible(options))) : null, function(ar) {
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
   @param id {string} 
   @param resultHandler {function} 
   @return {ConsulService}
   */
  this.destroySession = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulService["destroySession(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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

ConsulService._jclass = utils.getJavaClass("io.vertx.ext.consul.ConsulService");
ConsulService._jtype = {
  accept: function(obj) {
    return ConsulService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(ConsulService.prototype, {});
    ConsulService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
ConsulService._create = function(jdel) {
  var obj = Object.create(ConsulService.prototype, {});
  ConsulService.apply(obj, arguments);
  return obj;
}
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
    return utils.convReturnVertxGen(ConsulService, JConsulService["createEventBusProxy(io.vertx.core.Vertx,java.lang.String)"](vertx._jdel, address));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = ConsulService;