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
 * Converter for {@link io.vertx.ext.consul.ConsulClientOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ConsulClientOptions} original class using Vert.x codegen.
 */
public class ConsulClientOptionsConverter {

  public static void fromJson(JsonObject json, ConsulClientOptions obj) {
    if (json.getValue("aclToken") instanceof String) {
      obj.setAclToken((String)json.getValue("aclToken"));
    }
    if (json.getValue("dc") instanceof String) {
      obj.setDc((String)json.getValue("dc"));
    }
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("pemTrustOptions") instanceof JsonObject) {
      obj.setPemTrustOptions(new io.vertx.core.net.PemTrustOptions((JsonObject)json.getValue("pemTrustOptions")));
    }
    if (json.getValue("port") instanceof Number) {
      obj.setPort(((Number)json.getValue("port")).intValue());
    }
    if (json.getValue("ssl") instanceof Boolean) {
      obj.setSsl((Boolean)json.getValue("ssl"));
    }
    if (json.getValue("timeoutMs") instanceof Number) {
      obj.setTimeoutMs(((Number)json.getValue("timeoutMs")).longValue());
    }
    if (json.getValue("trustAll") instanceof Boolean) {
      obj.setTrustAll((Boolean)json.getValue("trustAll"));
    }
  }

  public static void toJson(ConsulClientOptions obj, JsonObject json) {
    if (obj.getAclToken() != null) {
      json.put("aclToken", obj.getAclToken());
    }
    if (obj.getDc() != null) {
      json.put("dc", obj.getDc());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getPemTrustOptions() != null) {
      json.put("pemTrustOptions", obj.getPemTrustOptions().toJson());
    }
    json.put("port", obj.getPort());
    json.put("ssl", obj.isSsl());
    json.put("timeoutMs", obj.getTimeoutMs());
    json.put("trustAll", obj.isTrustAll());
  }
}