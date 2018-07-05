package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.ConsulClientOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ConsulClientOptions} original class using Vert.x codegen.
 */
public class ConsulClientOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ConsulClientOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "aclToken":
          if (member.getValue() instanceof String) {
            obj.setAclToken((String)member.getValue());
          }
          break;
        case "dc":
          if (member.getValue() instanceof String) {
            obj.setDc((String)member.getValue());
          }
          break;
        case "timeout":
          if (member.getValue() instanceof Number) {
            obj.setTimeout(((Number)member.getValue()).longValue());
          }
          break;
      }
    }
  }

  public static void toJson(ConsulClientOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ConsulClientOptions obj, java.util.Map<String, Object> json) {
    if (obj.getAclToken() != null) {
      json.put("aclToken", obj.getAclToken());
    }
    if (obj.getDc() != null) {
      json.put("dc", obj.getDc());
    }
    json.put("timeout", obj.getTimeout());
  }
}
