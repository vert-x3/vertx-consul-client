package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.KeyValue}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.KeyValue} original class using Vert.x codegen.
 */
public class KeyValueConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, KeyValue obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "createIndex":
          if (member.getValue() instanceof Number) {
            obj.setCreateIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "flags":
          if (member.getValue() instanceof Number) {
            obj.setFlags(((Number)member.getValue()).longValue());
          }
          break;
        case "key":
          if (member.getValue() instanceof String) {
            obj.setKey((String)member.getValue());
          }
          break;
        case "lockIndex":
          if (member.getValue() instanceof Number) {
            obj.setLockIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "modifyIndex":
          if (member.getValue() instanceof Number) {
            obj.setModifyIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "session":
          if (member.getValue() instanceof String) {
            obj.setSession((String)member.getValue());
          }
          break;
        case "value":
          if (member.getValue() instanceof String) {
            obj.setValue((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(KeyValue obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(KeyValue obj, java.util.Map<String, Object> json) {
    json.put("createIndex", obj.getCreateIndex());
    json.put("flags", obj.getFlags());
    if (obj.getKey() != null) {
      json.put("key", obj.getKey());
    }
    json.put("lockIndex", obj.getLockIndex());
    json.put("modifyIndex", obj.getModifyIndex());
    if (obj.getSession() != null) {
      json.put("session", obj.getSession());
    }
    if (obj.getValue() != null) {
      json.put("value", obj.getValue());
    }
  }
}
