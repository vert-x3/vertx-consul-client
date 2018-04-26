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
 * Converter for {@link io.vertx.ext.consul.Coordinate}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Coordinate} original class using Vert.x codegen.
 */
public class CoordinateConverter {

  public static void fromJson(JsonObject json, Coordinate obj) {
    if (json.getValue("adj") instanceof Number) {
      obj.setAdj(((Number)json.getValue("adj")).floatValue());
    }
    if (json.getValue("err") instanceof Number) {
      obj.setErr(((Number)json.getValue("err")).floatValue());
    }
    if (json.getValue("height") instanceof Number) {
      obj.setHeight(((Number)json.getValue("height")).floatValue());
    }
    if (json.getValue("node") instanceof String) {
      obj.setNode((String)json.getValue("node"));
    }
    if (json.getValue("vec") instanceof JsonArray) {
      java.util.ArrayList<java.lang.Float> list = new java.util.ArrayList<>();
      json.getJsonArray("vec").forEach( item -> {
        if (item instanceof Number)
          list.add(((Number)item).floatValue());
      });
      obj.setVec(list);
    }
  }

  public static void toJson(Coordinate obj, JsonObject json) {
    json.put("adj", obj.getAdj());
    json.put("err", obj.getErr());
    json.put("height", obj.getHeight());
    if (obj.getNode() != null) {
      json.put("node", obj.getNode());
    }
    if (obj.getVec() != null) {
      JsonArray array = new JsonArray();
      obj.getVec().forEach(item -> array.add(item));
      json.put("vec", array);
    }
  }
}