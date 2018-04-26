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
 * Converter for {@link io.vertx.ext.consul.ServiceList}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ServiceList} original class using Vert.x codegen.
 */
public class ServiceListConverter {

  public static void fromJson(JsonObject json, ServiceList obj) {
    if (json.getValue("index") instanceof Number) {
      obj.setIndex(((Number)json.getValue("index")).longValue());
    }
    if (json.getValue("list") instanceof JsonArray) {
      java.util.ArrayList<io.vertx.ext.consul.Service> list = new java.util.ArrayList<>();
      json.getJsonArray("list").forEach( item -> {
        if (item instanceof JsonObject)
          list.add(new io.vertx.ext.consul.Service((JsonObject)item));
      });
      obj.setList(list);
    }
  }

  public static void toJson(ServiceList obj, JsonObject json) {
    json.put("index", obj.getIndex());
    if (obj.getList() != null) {
      JsonArray array = new JsonArray();
      obj.getList().forEach(item -> array.add(item.toJson()));
      json.put("list", array);
    }
  }
}