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
 * Converter for {@link io.vertx.ext.consul.PreparedQueryExecuteResponse}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.PreparedQueryExecuteResponse} original class using Vert.x codegen.
 */
public class PreparedQueryExecuteResponseConverter {

  public static void fromJson(JsonObject json, PreparedQueryExecuteResponse obj) {
    if (json.getValue("dc") instanceof String) {
      obj.setDc((String)json.getValue("dc"));
    }
    if (json.getValue("dnsTtl") instanceof String) {
      obj.setDnsTtl((String)json.getValue("dnsTtl"));
    }
    if (json.getValue("failovers") instanceof Number) {
      obj.setFailovers(((Number)json.getValue("failovers")).intValue());
    }
    if (json.getValue("nodes") instanceof JsonArray) {
      java.util.ArrayList<io.vertx.ext.consul.ServiceEntry> list = new java.util.ArrayList<>();
      json.getJsonArray("nodes").forEach( item -> {
        if (item instanceof JsonObject)
          list.add(new io.vertx.ext.consul.ServiceEntry((JsonObject)item));
      });
      obj.setNodes(list);
    }
    if (json.getValue("service") instanceof String) {
      obj.setService((String)json.getValue("service"));
    }
  }

  public static void toJson(PreparedQueryExecuteResponse obj, JsonObject json) {
    if (obj.getDc() != null) {
      json.put("dc", obj.getDc());
    }
    if (obj.getDnsTtl() != null) {
      json.put("dnsTtl", obj.getDnsTtl());
    }
    json.put("failovers", obj.getFailovers());
    if (obj.getNodes() != null) {
      JsonArray array = new JsonArray();
      obj.getNodes().forEach(item -> array.add(item.toJson()));
      json.put("nodes", array);
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService());
    }
  }
}