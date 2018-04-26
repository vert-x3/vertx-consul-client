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
 * Converter for {@link io.vertx.ext.consul.ServiceEntry}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ServiceEntry} original class using Vert.x codegen.
 */
public class ServiceEntryConverter {

  public static void fromJson(JsonObject json, ServiceEntry obj) {
    if (json.getValue("checks") instanceof JsonArray) {
      java.util.ArrayList<io.vertx.ext.consul.Check> list = new java.util.ArrayList<>();
      json.getJsonArray("checks").forEach( item -> {
        if (item instanceof JsonObject)
          list.add(new io.vertx.ext.consul.Check((JsonObject)item));
      });
      obj.setChecks(list);
    }
    if (json.getValue("node") instanceof JsonObject) {
      obj.setNode(new io.vertx.ext.consul.Node((JsonObject)json.getValue("node")));
    }
    if (json.getValue("service") instanceof JsonObject) {
      obj.setService(new io.vertx.ext.consul.Service((JsonObject)json.getValue("service")));
    }
  }

  public static void toJson(ServiceEntry obj, JsonObject json) {
    if (obj.getChecks() != null) {
      JsonArray array = new JsonArray();
      obj.getChecks().forEach(item -> array.add(item.toJson()));
      json.put("checks", array);
    }
    if (obj.getNode() != null) {
      json.put("node", obj.getNode().toJson());
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService().toJson());
    }
  }
}