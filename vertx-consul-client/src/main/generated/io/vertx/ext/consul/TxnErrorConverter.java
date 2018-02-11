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
 * Converter for {@link io.vertx.ext.consul.TxnError}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.TxnError} original class using Vert.x codegen.
 */
public class TxnErrorConverter {

  public static void fromJson(JsonObject json, TxnError obj) {
    if (json.getValue("opIndex") instanceof Number) {
      obj.setOpIndex(((Number)json.getValue("opIndex")).intValue());
    }
    if (json.getValue("what") instanceof String) {
      obj.setWhat((String)json.getValue("what"));
    }
  }

  public static void toJson(TxnError obj, JsonObject json) {
    json.put("opIndex", obj.getOpIndex());
    if (obj.getWhat() != null) {
      json.put("what", obj.getWhat());
    }
  }
}