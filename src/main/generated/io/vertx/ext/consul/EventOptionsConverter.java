package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.EventOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.EventOptions} original class using Vert.x codegen.
 */
public class EventOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, EventOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "node":
          if (member.getValue() instanceof String) {
            obj.setNode((String)member.getValue());
          }
          break;
        case "payload":
          if (member.getValue() instanceof String) {
            obj.setPayload((String)member.getValue());
          }
          break;
        case "service":
          if (member.getValue() instanceof String) {
            obj.setService((String)member.getValue());
          }
          break;
        case "tag":
          if (member.getValue() instanceof String) {
            obj.setTag((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(EventOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(EventOptions obj, java.util.Map<String, Object> json) {
    if (obj.getNode() != null) {
      json.put("node", obj.getNode());
    }
    if (obj.getPayload() != null) {
      json.put("payload", obj.getPayload());
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService());
    }
    if (obj.getTag() != null) {
      json.put("tag", obj.getTag());
    }
  }
}
