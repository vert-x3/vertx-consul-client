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
 * Converter for {@link io.vertx.ext.consul.EventListOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.EventListOptions} original class using Vert.x codegen.
 */
public class EventListOptionsConverter {

  public static void fromJson(JsonObject json, EventListOptions obj) {
    if (json.getValue("blockingOptions") instanceof JsonObject) {
      obj.setBlockingOptions(new io.vertx.ext.consul.BlockingQueryOptions((JsonObject)json.getValue("blockingOptions")));
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
  }

  public static void toJson(EventListOptions obj, JsonObject json) {
    if (obj.getBlockingOptions() != null) {
      json.put("blockingOptions", obj.getBlockingOptions().toJson());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}