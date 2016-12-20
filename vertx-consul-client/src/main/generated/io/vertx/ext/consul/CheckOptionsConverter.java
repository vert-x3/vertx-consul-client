/*
 * Copyright 2014 Red Hat, Inc.
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
 * Converter for {@link io.vertx.ext.consul.CheckOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.CheckOptions} original class using Vert.x codegen.
 */
public class CheckOptionsConverter {

  public static void fromJson(JsonObject json, CheckOptions obj) {
    if (json.getValue("http") instanceof String) {
      obj.setHttp((String)json.getValue("http"));
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("interval") instanceof String) {
      obj.setInterval((String)json.getValue("interval"));
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
    if (json.getValue("notes") instanceof String) {
      obj.setNotes((String)json.getValue("notes"));
    }
    if (json.getValue("script") instanceof String) {
      obj.setScript((String)json.getValue("script"));
    }
    if (json.getValue("tcp") instanceof String) {
      obj.setTcp((String)json.getValue("tcp"));
    }
    if (json.getValue("ttl") instanceof String) {
      obj.setTtl((String)json.getValue("ttl"));
    }
  }

  public static void toJson(CheckOptions obj, JsonObject json) {
    if (obj.getHttp() != null) {
      json.put("http", obj.getHttp());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getInterval() != null) {
      json.put("interval", obj.getInterval());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getNotes() != null) {
      json.put("notes", obj.getNotes());
    }
    if (obj.getScript() != null) {
      json.put("script", obj.getScript());
    }
    if (obj.getTcp() != null) {
      json.put("tcp", obj.getTcp());
    }
    if (obj.getTtl() != null) {
      json.put("ttl", obj.getTtl());
    }
  }
}