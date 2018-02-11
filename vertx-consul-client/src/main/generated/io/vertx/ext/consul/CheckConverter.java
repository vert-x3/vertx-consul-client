/*
 * Copyright (c) 2014 Red Hat, Inc. and others
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

package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.Check}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Check} original class using Vert.x codegen.
 */
public class CheckConverter {

  public static void fromJson(JsonObject json, Check obj) {
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
    if (json.getValue("nodeName") instanceof String) {
      obj.setNodeName((String)json.getValue("nodeName"));
    }
    if (json.getValue("notes") instanceof String) {
      obj.setNotes((String)json.getValue("notes"));
    }
    if (json.getValue("output") instanceof String) {
      obj.setOutput((String)json.getValue("output"));
    }
    if (json.getValue("serviceId") instanceof String) {
      obj.setServiceId((String)json.getValue("serviceId"));
    }
    if (json.getValue("serviceName") instanceof String) {
      obj.setServiceName((String)json.getValue("serviceName"));
    }
    if (json.getValue("status") instanceof String) {
      obj.setStatus(io.vertx.ext.consul.CheckStatus.valueOf((String)json.getValue("status")));
    }
  }

  public static void toJson(Check obj, JsonObject json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getNodeName() != null) {
      json.put("nodeName", obj.getNodeName());
    }
    if (obj.getNotes() != null) {
      json.put("notes", obj.getNotes());
    }
    if (obj.getOutput() != null) {
      json.put("output", obj.getOutput());
    }
    if (obj.getServiceId() != null) {
      json.put("serviceId", obj.getServiceId());
    }
    if (obj.getServiceName() != null) {
      json.put("serviceName", obj.getServiceName());
    }
    if (obj.getStatus() != null) {
      json.put("status", obj.getStatus().name());
    }
  }
}