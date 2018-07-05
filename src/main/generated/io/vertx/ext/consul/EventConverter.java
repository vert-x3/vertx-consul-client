package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.Event}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Event} original class using Vert.x codegen.
 */
public class EventConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Event obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "lTime":
          if (member.getValue() instanceof Number) {
            obj.setLTime(((Number)member.getValue()).intValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
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
        case "version":
          if (member.getValue() instanceof Number) {
            obj.setVersion(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

  public static void toJson(Event obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Event obj, java.util.Map<String, Object> json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    json.put("lTime", obj.getLTime());
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
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
    json.put("version", obj.getVersion());
  }
}
