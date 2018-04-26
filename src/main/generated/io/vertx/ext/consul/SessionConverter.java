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
 * Converter for {@link io.vertx.ext.consul.Session}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Session} original class using Vert.x codegen.
 */
public class SessionConverter {

  public static void fromJson(JsonObject json, Session obj) {
    if (json.getValue("checks") instanceof JsonArray) {
      java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
      json.getJsonArray("checks").forEach( item -> {
        if (item instanceof String)
          list.add((String)item);
      });
      obj.setChecks(list);
    }
    if (json.getValue("createIndex") instanceof Number) {
      obj.setCreateIndex(((Number)json.getValue("createIndex")).longValue());
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("index") instanceof Number) {
      obj.setIndex(((Number)json.getValue("index")).longValue());
    }
    if (json.getValue("lockDelay") instanceof Number) {
      obj.setLockDelay(((Number)json.getValue("lockDelay")).longValue());
    }
    if (json.getValue("node") instanceof String) {
      obj.setNode((String)json.getValue("node"));
    }
  }

  public static void toJson(Session obj, JsonObject json) {
    if (obj.getChecks() != null) {
      JsonArray array = new JsonArray();
      obj.getChecks().forEach(item -> array.add(item));
      json.put("checks", array);
    }
    json.put("createIndex", obj.getCreateIndex());
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    json.put("index", obj.getIndex());
    json.put("lockDelay", obj.getLockDelay());
    if (obj.getNode() != null) {
      json.put("node", obj.getNode());
    }
  }
}