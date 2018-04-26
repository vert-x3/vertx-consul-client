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
 * Converter for {@link io.vertx.ext.consul.PreparedQueryExecuteOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.PreparedQueryExecuteOptions} original class using Vert.x codegen.
 */
public class PreparedQueryExecuteOptionsConverter {

  public static void fromJson(JsonObject json, PreparedQueryExecuteOptions obj) {
    if (json.getValue("limit") instanceof Number) {
      obj.setLimit(((Number)json.getValue("limit")).intValue());
    }
    if (json.getValue("near") instanceof String) {
      obj.setNear((String)json.getValue("near"));
    }
  }

  public static void toJson(PreparedQueryExecuteOptions obj, JsonObject json) {
    json.put("limit", obj.getLimit());
    if (obj.getNear() != null) {
      json.put("near", obj.getNear());
    }
  }
}