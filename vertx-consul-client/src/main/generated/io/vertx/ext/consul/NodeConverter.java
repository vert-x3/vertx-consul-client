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
 * Converter for {@link io.vertx.ext.consul.Node}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Node} original class using Vert.x codegen.
 */
public class NodeConverter {

  public static void fromJson(JsonObject json, Node obj) {
    if (json.getValue("address") instanceof String) {
      obj.setAddress((String)json.getValue("address"));
    }
    if (json.getValue("lanAddress") instanceof String) {
      obj.setLanAddress((String)json.getValue("lanAddress"));
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
    if (json.getValue("wanAddress") instanceof String) {
      obj.setWanAddress((String)json.getValue("wanAddress"));
    }
  }

  public static void toJson(Node obj, JsonObject json) {
    if (obj.getAddress() != null) {
      json.put("address", obj.getAddress());
    }
    if (obj.getLanAddress() != null) {
      json.put("lanAddress", obj.getLanAddress());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getWanAddress() != null) {
      json.put("wanAddress", obj.getWanAddress());
    }
  }
}