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
 * Converter for {@link io.vertx.ext.consul.Event}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Event} original class using Vert.x codegen.
 */
public class EventConverter {

  public static void fromJson(JsonObject json, Event obj) {
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("lTime") instanceof Number) {
      obj.setLTime(((Number)json.getValue("lTime")).intValue());
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
    if (json.getValue("node") instanceof String) {
      obj.setNode((String)json.getValue("node"));
    }
    if (json.getValue("payload") instanceof String) {
      obj.setPayload((String)json.getValue("payload"));
    }
    if (json.getValue("service") instanceof String) {
      obj.setService((String)json.getValue("service"));
    }
    if (json.getValue("tag") instanceof String) {
      obj.setTag((String)json.getValue("tag"));
    }
    if (json.getValue("version") instanceof Number) {
      obj.setVersion(((Number)json.getValue("version")).intValue());
    }
  }

  public static void toJson(Event obj, JsonObject json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    json.put("lTime", obj.getLTime());
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getNode() != null) {
      json.put("node", obj.getNode());
    }
    if (obj.getPayload() != null) {
      json.put("payload", obj.getPayload());
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService());
    }
    if (obj.getTag() != null) {
      json.put("tag", obj.getTag());
    }
    json.put("version", obj.getVersion());
  }
}