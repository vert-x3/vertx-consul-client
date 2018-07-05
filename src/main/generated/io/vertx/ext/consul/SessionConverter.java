package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.Session}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Session} original class using Vert.x codegen.
 */
public class SessionConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Session obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "checks":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setChecks(list);
          }
          break;
        case "createIndex":
          if (member.getValue() instanceof Number) {
            obj.setCreateIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "index":
          if (member.getValue() instanceof Number) {
            obj.setIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "lockDelay":
          if (member.getValue() instanceof Number) {
            obj.setLockDelay(((Number)member.getValue()).longValue());
          }
          break;
        case "node":
          if (member.getValue() instanceof String) {
            obj.setNode((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Session obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Session obj, java.util.Map<String, Object> json) {
    if (obj.getChecks() != null) {
      JsonArray array = new JsonArray();
      obj.getChecks().forEach(item -> array.add(item));
      json.put("checks", array);
    }
    json.put("createIndex", obj.getCreateIndex());
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    json.put("index", obj.getIndex());
    json.put("lockDelay", obj.getLockDelay());
    if (obj.getNode() != null) {
      json.put("node", obj.getNode());
    }
  }
}
