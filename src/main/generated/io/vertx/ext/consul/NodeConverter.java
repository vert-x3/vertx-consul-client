package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.Node}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Node} original class using Vert.x codegen.
 */
public class NodeConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Node obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "address":
          if (member.getValue() instanceof String) {
            obj.setAddress((String)member.getValue());
          }
          break;
        case "lanAddress":
          if (member.getValue() instanceof String) {
            obj.setLanAddress((String)member.getValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "wanAddress":
          if (member.getValue() instanceof String) {
            obj.setWanAddress((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Node obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Node obj, java.util.Map<String, Object> json) {
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
