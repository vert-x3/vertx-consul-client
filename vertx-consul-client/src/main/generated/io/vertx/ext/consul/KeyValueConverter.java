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
 * Converter for {@link io.vertx.ext.consul.KeyValue}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.KeyValue} original class using Vert.x codegen.
 */
public class KeyValueConverter {

  public static void fromJson(JsonObject json, KeyValue obj) {
    if (json.getValue("createIndex") instanceof Number) {
      obj.setCreateIndex(((Number)json.getValue("createIndex")).longValue());
    }
    if (json.getValue("flags") instanceof Number) {
      obj.setFlags(((Number)json.getValue("flags")).longValue());
    }
    if (json.getValue("key") instanceof String) {
      obj.setKey((String)json.getValue("key"));
    }
    if (json.getValue("lockIndex") instanceof Number) {
      obj.setLockIndex(((Number)json.getValue("lockIndex")).longValue());
    }
    if (json.getValue("modifyIndex") instanceof Number) {
      obj.setModifyIndex(((Number)json.getValue("modifyIndex")).longValue());
    }
    if (json.getValue("session") instanceof String) {
      obj.setSession((String)json.getValue("session"));
    }
    if (json.getValue("value") instanceof String) {
      obj.setValue((String)json.getValue("value"));
    }
  }

  public static void toJson(KeyValue obj, JsonObject json) {
    json.put("createIndex", obj.getCreateIndex());
    json.put("flags", obj.getFlags());
    if (obj.getKey() != null) {
      json.put("key", obj.getKey());
    }
    json.put("lockIndex", obj.getLockIndex());
    json.put("modifyIndex", obj.getModifyIndex());
    if (obj.getSession() != null) {
      json.put("session", obj.getSession());
    }
    if (obj.getValue() != null) {
      json.put("value", obj.getValue());
    }
  }
}