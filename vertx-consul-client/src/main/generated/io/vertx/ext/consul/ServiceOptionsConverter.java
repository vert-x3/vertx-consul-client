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
 * Converter for {@link io.vertx.ext.consul.ServiceOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ServiceOptions} original class using Vert.x codegen.
 */
public class ServiceOptionsConverter {

  public static void fromJson(JsonObject json, ServiceOptions obj) {
    if (json.getValue("address") instanceof String) {
      obj.setAddress((String)json.getValue("address"));
    }
    if (json.getValue("checkOptions") instanceof JsonObject) {
      obj.setCheckOptions(new io.vertx.ext.consul.CheckOptions((JsonObject)json.getValue("checkOptions")));
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
    if (json.getValue("port") instanceof Number) {
      obj.setPort(((Number)json.getValue("port")).intValue());
    }
    if (json.getValue("tags") instanceof JsonArray) {
      java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
      json.getJsonArray("tags").forEach( item -> {
        if (item instanceof String)
          list.add((String)item);
      });
      obj.setTags(list);
    }
  }

  public static void toJson(ServiceOptions obj, JsonObject json) {
    if (obj.getAddress() != null) {
      json.put("address", obj.getAddress());
    }
    if (obj.getCheckOptions() != null) {
      json.put("checkOptions", obj.getCheckOptions().toJson());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    json.put("port", obj.getPort());
    if (obj.getTags() != null) {
      JsonArray array = new JsonArray();
      obj.getTags().forEach(item -> array.add(item));
      json.put("tags", array);
    }
  }
}