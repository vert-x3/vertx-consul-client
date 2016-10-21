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

/** @module vertx-consul-js/consul_client */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JConsulClient = io.vertx.ext.consul.ConsulClient;
var BlockingQueryOptions = io.vertx.ext.consul.BlockingQueryOptions;
var Event = io.vertx.ext.consul.Event;
var MaintenanceOptions = io.vertx.ext.consul.MaintenanceOptions;
var Check = io.vertx.ext.consul.Check;
var Service = io.vertx.ext.consul.Service;
var CheckOptions = io.vertx.ext.consul.CheckOptions;
var Coordinate = io.vertx.ext.consul.Coordinate;
var KeyValue = io.vertx.ext.consul.KeyValue;
var ServiceOptions = io.vertx.ext.consul.ServiceOptions;
var KeyValueOptions = io.vertx.ext.consul.KeyValueOptions;
var AclToken = io.vertx.ext.consul.AclToken;
var SessionOptions = io.vertx.ext.consul.SessionOptions;
var DcCoordinates = io.vertx.ext.consul.DcCoordinates;
var Session = io.vertx.ext.consul.Session;
var EventOptions = io.vertx.ext.consul.EventOptions;
var Node = io.vertx.ext.consul.Node;

/**
 A Vert.x service used to interact with Consul.

 @class
*/
var ConsulClient = function(j_val) {

  var j_consulClient = j_val;
  var that = this;

  /**
   Returns the LAN network coordinates for all nodes in a given DC

   @public
   @param resultHandler {function} will be provided with network coordinates of nodes in datacenter 
   @return {ConsulClient} reference to this, for fluency
   */
  this.coordinateNodes = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["coordinateNodes(io.vertx.core.Handler)"](function(ar) {
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
   Returns the WAN network coordinates for all Consul servers, organized by DCs

   @public
   @param resultHandler {function} will be provided with network coordinates for all Consul servers 
   @return {ConsulClient} reference to this, for fluency
   */
  this.coordinateDatacenters = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["coordinateDatacenters(io.vertx.core.Handler)"](function(ar) {
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
   @return {ConsulClient}
   */
  this.getValue = function(key, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["getValue(java.lang.String,io.vertx.core.Handler)"](key, function(ar) {
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
   @return {ConsulClient}
   */
  this.getValueBlocking = function(key, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulClient["getValueBlocking(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](key, options != null ? new BlockingQueryOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
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
   @return {ConsulClient}
   */
  this.deleteValue = function(key, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["deleteValue(java.lang.String,io.vertx.core.Handler)"](key, function(ar) {
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
   @return {ConsulClient}
   */
  this.getValues = function(keyPrefix, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["getValues(java.lang.String,io.vertx.core.Handler)"](keyPrefix, function(ar) {
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
   @param options {Object} 
   @param resultHandler {function} 
   @return {ConsulClient}
   */
  this.getValuesBlocking = function(keyPrefix, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulClient["getValuesBlocking(java.lang.String,io.vertx.ext.consul.BlockingQueryOptions,io.vertx.core.Handler)"](keyPrefix, options != null ? new BlockingQueryOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
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
   @return {ConsulClient}
   */
  this.deleteValues = function(keyPrefix, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["deleteValues(java.lang.String,io.vertx.core.Handler)"](keyPrefix, function(ar) {
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
   @return {ConsulClient}
   */
  this.putValue = function(key, value, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulClient["putValue(java.lang.String,java.lang.String,io.vertx.core.Handler)"](key, value, function(ar) {
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
   @return {ConsulClient}
   */
  this.putValueWithOptions = function(key, value, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && (typeof __args[2] === 'object' && __args[2] != null) && typeof __args[3] === 'function') {
      j_consulClient["putValueWithOptions(java.lang.String,java.lang.String,io.vertx.ext.consul.KeyValueOptions,io.vertx.core.Handler)"](key, value, options != null ? new KeyValueOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
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
   Create new Acl token

   @public
   @param token {Object} properties of the token 
   @param idHandler {function} will be provided with ID of created token 
   @return {ConsulClient} reference to this, for fluency
   */
  this.createAclToken = function(token, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulClient["createAclToken(io.vertx.ext.consul.AclToken,io.vertx.core.Handler)"](token != null ? new AclToken(new JsonObject(JSON.stringify(token))) : null, function(ar) {
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
   Update Acl token

   @public
   @param token {Object} properties of the token to be updated 
   @param idHandler {function} will be provided with ID of updated 
   @return {ConsulClient} reference to this, for fluency
   */
  this.updateAclToken = function(token, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulClient["updateAclToken(io.vertx.ext.consul.AclToken,io.vertx.core.Handler)"](token != null ? new AclToken(new JsonObject(JSON.stringify(token))) : null, function(ar) {
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
   Clone Acl token

   @public
   @param id {string} the ID of token to be cloned 
   @param idHandler {function} will be provided with ID of cloned token 
   @return {ConsulClient} reference to this, for fluency
   */
  this.cloneAclToken = function(id, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["cloneAclToken(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Get list of Acl token

   @public
   @param resultHandler {function} will be provided with list of tokens 
   @return {ConsulClient} reference to this, for fluency
   */
  this.listAclTokens = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["listAclTokens(io.vertx.core.Handler)"](function(ar) {
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
   Get info of Acl token

   @public
   @param id {string} the ID of token 
   @param tokenHandler {function} will be provided with token 
   @return {ConsulClient} reference to this, for fluency
   */
  this.infoAclToken = function(id, tokenHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["infoAclToken(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Destroy Acl token

   @public
   @param id {string} the ID of token 
   @param resultHandler {function} will be called on complete 
   @return {ConsulClient} reference to this, for fluency
   */
  this.destroyAclToken = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["destroyAclToken(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Fires a new user event

   @public
   @param name {string} name of event 
   @param resultHandler {function} will be provided with properties of event 
   @return {ConsulClient} reference to this, for fluency
   */
  this.fireEvent = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["fireEvent(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
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
   Fires a new user event

   @public
   @param name {string} name of event 
   @param options {Object} options used to create event 
   @param resultHandler {function} will be provided with properties of event 
   @return {ConsulClient} reference to this, for fluency
   */
  this.fireEventWithOptions = function(name, options, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_consulClient["fireEventWithOptions(java.lang.String,io.vertx.ext.consul.EventOptions,io.vertx.core.Handler)"](name, options != null ? new EventOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
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
   Returns the most recent events known by the agent

   @public
   @param resultHandler {function} will be provided with list of events 
   @return {ConsulClient} reference to this, for fluency
   */
  this.listEvents = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["listEvents(io.vertx.core.Handler)"](function(ar) {
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
   @return {ConsulClient}
   */
  this.registerService = function(service, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulClient["registerService(io.vertx.ext.consul.ServiceOptions,io.vertx.core.Handler)"](service != null ? new ServiceOptions(new JsonObject(JSON.stringify(service))) : null, function(ar) {
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
   @return {ConsulClient}
   */
  this.maintenanceService = function(maintenanceOptions, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulClient["maintenanceService(io.vertx.ext.consul.MaintenanceOptions,io.vertx.core.Handler)"](maintenanceOptions != null ? new MaintenanceOptions(new JsonObject(JSON.stringify(maintenanceOptions))) : null, function(ar) {
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
   @return {ConsulClient}
   */
  this.deregisterService = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["deregisterService(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Returns the nodes providing a service

   @public
   @param service {string} name of service 
   @param resultHandler {function} will be provided with list of nodes providing given service 
   @return {ConsulClient} reference to this, for fluency
   */
  this.catalogServiceNodes = function(service, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["catalogServiceNodes(java.lang.String,io.vertx.core.Handler)"](service, function(ar) {
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
   Returns the nodes providing a service, filtered by tag

   @public
   @param service {string} name of service 
   @param tag {string} service tag 
   @param resultHandler {function} will be provided with list of nodes providing given service 
   @return {ConsulClient} reference to this, for fluency
   */
  this.catalogServiceNodesWithTag = function(service, tag, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulClient["catalogServiceNodesWithTag(java.lang.String,java.lang.String,io.vertx.core.Handler)"](service, tag, function(ar) {
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
   Return all the datacenters that are known by the Consul server

   @public
   @param resultHandler {function} will be provided with list of datacenters 
   @return {ConsulClient} reference to this, for fluency
   */
  this.catalogDatacenters = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["catalogDatacenters(io.vertx.core.Handler)"](function(ar) {
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
   Returns the nodes registered in a datacenter

   @public
   @param resultHandler {function} will be provided with list of nodes 
   @return {ConsulClient} reference to this, for fluency
   */
  this.catalogNodes = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["catalogNodes(io.vertx.core.Handler)"](function(ar) {
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
   Returns the services registered in a datacenter

   @public
   @param resultHandler {function} will be provided with list of services 
   @return {ConsulClient} reference to this, for fluency
   */
  this.catalogServices = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["catalogServices(io.vertx.core.Handler)"](function(ar) {
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
   Returns the node's registered services

   @public
   @param node {string} node name 
   @param resultHandler {function} will be provided with list of services 
   @return {ConsulClient} reference to this, for fluency
   */
  this.catalogNodeServices = function(node, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["catalogNodeServices(java.lang.String,io.vertx.core.Handler)"](node, function(ar) {
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
   @return {ConsulClient}
   */
  this.localServices = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["localServices(io.vertx.core.Handler)"](function(ar) {
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
   @return {ConsulClient}
   */
  this.localChecks = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["localChecks(io.vertx.core.Handler)"](function(ar) {
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
   @return {ConsulClient}
   */
  this.registerCheck = function(check, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulClient["registerCheck(io.vertx.ext.consul.CheckOptions,io.vertx.core.Handler)"](check != null ? new CheckOptions(new JsonObject(JSON.stringify(check))) : null, function(ar) {
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
   @return {ConsulClient}
   */
  this.deregisterCheck = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["deregisterCheck(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   @return {ConsulClient}
   */
  this.passCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["passCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @return {ConsulClient}
   */
  this.passCheckWithNote = function(checkId, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulClient["passCheckWithNote(java.lang.String,java.lang.String,io.vertx.core.Handler)"](checkId, note, function(ar) {
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
   @return {ConsulClient}
   */
  this.warnCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["warnCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @return {ConsulClient}
   */
  this.warnCheckWithNote = function(checkId, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulClient["warnCheckWithNote(java.lang.String,java.lang.String,io.vertx.core.Handler)"](checkId, note, function(ar) {
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
   @return {ConsulClient}
   */
  this.failCheck = function(checkId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["failCheck(java.lang.String,io.vertx.core.Handler)"](checkId, function(ar) {
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
   @return {ConsulClient}
   */
  this.failCheckWithNote = function(checkId, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulClient["failCheckWithNote(java.lang.String,java.lang.String,io.vertx.core.Handler)"](checkId, note, function(ar) {
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
   @return {ConsulClient}
   */
  this.updateCheck = function(checkId, status, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_consulClient["updateCheck(java.lang.String,io.vertx.ext.consul.CheckStatus,io.vertx.core.Handler)"](checkId, io.vertx.ext.consul.CheckStatus.valueOf(status), function(ar) {
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
   @return {ConsulClient}
   */
  this.updateCheckWithNote = function(checkId, status, note, resultHandler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'string' && typeof __args[3] === 'function') {
      j_consulClient["updateCheckWithNote(java.lang.String,io.vertx.ext.consul.CheckStatus,java.lang.String,io.vertx.core.Handler)"](checkId, io.vertx.ext.consul.CheckStatus.valueOf(status), note, function(ar) {
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
   Get the Raft leader for the datacenter in which the agent is running.
   It returns an address in format "<code>10.1.10.12:8300</code>"

   @public
   @param resultHandler {function} will be provided with address of cluster leader 
   @return {ConsulClient} reference to this, for fluency
   */
  this.leaderStatus = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["leaderStatus(io.vertx.core.Handler)"](function(ar) {
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
   Retrieves the Raft peers for the datacenter in which the the agent is running.
   It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"

   @public
   @param resultHandler {function} will be provided with list of peers 
   @return {ConsulClient} reference to this, for fluency
   */
  this.peersStatus = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["peersStatus(io.vertx.core.Handler)"](function(ar) {
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
   Initialize a new session

   @public
   @param idHandler {function} will be provided with ID of new session 
   @return {ConsulClient} reference to this, for fluency
   */
  this.createSession = function(idHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["createSession(io.vertx.core.Handler)"](function(ar) {
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
   Initialize a new session

   @public
   @param options {Object} options used to create session 
   @param idHandler {function} will be provided with ID of new session 
   @return {ConsulClient} reference to this, for fluency
   */
  this.createSessionWithOptions = function(options, idHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_consulClient["createSessionWithOptions(io.vertx.ext.consul.SessionOptions,io.vertx.core.Handler)"](options != null ? new SessionOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
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
   Returns the requested session information

   @public
   @param id {string} the ID of requested session 
   @param resultHandler {function} will be provided with info of requested session 
   @return {ConsulClient} reference to this, for fluency
   */
  this.infoSession = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["infoSession(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL

   @public
   @param id {string} the ID of session that should be renewed 
   @param resultHandler {function} will be provided with info of renewed session 
   @return {ConsulClient} reference to this, for fluency
   */
  this.renewSession = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["renewSession(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Returns the active sessions

   @public
   @param resultHandler {function} will be provided with list of sessions 
   @return {ConsulClient} reference to this, for fluency
   */
  this.listSessions = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_consulClient["listSessions(io.vertx.core.Handler)"](function(ar) {
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
   Returns the active sessions for a given node

   @public
   @param nodeId {string} the ID of node 
   @param resultHandler {function} will be provided with list of sessions 
   @return {ConsulClient} reference to this, for fluency
   */
  this.listNodeSessions = function(nodeId, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["listNodeSessions(java.lang.String,io.vertx.core.Handler)"](nodeId, function(ar) {
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
   Destroys the given session

   @public
   @param id {string} the ID of session 
   @param resultHandler {function} will be called when complete 
   @return {ConsulClient} reference to this, for fluency
   */
  this.destroySession = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_consulClient["destroySession(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
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
   Close the client and release its resources

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_consulClient["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_consulClient;
};

/**
 Create a Consul client.

 @memberof module:vertx-consul-js/consul_client
 @param vertx {Vertx} the Vert.x instance 
 @param config {Object} the configuration 
 @return {ConsulClient} the client
 */
ConsulClient.create = function(vertx, config) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(JConsulClient["create(io.vertx.core.Vertx,io.vertx.core.json.JsonObject)"](vertx._jdel, utils.convParamJsonObject(config)), ConsulClient);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = ConsulClient;