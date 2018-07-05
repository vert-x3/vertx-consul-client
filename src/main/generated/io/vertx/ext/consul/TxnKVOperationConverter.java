package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.TxnKVOperation}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.TxnKVOperation} original class using Vert.x codegen.
 */
public class TxnKVOperationConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TxnKVOperation obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "flags":
          if (member.getValue() instanceof Number) {
            obj.setFlags(((Number)member.getValue()).longValue());
          }
          break;
        case "index":
          if (member.getValue() instanceof Number) {
            obj.setIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "key":
          if (member.getValue() instanceof String) {
            obj.setKey((String)member.getValue());
          }
          break;
        case "session":
          if (member.getValue() instanceof String) {
            obj.setSession((String)member.getValue());
          }
          break;
        case "type":
          if (member.getValue() instanceof String) {
            obj.setType(io.vertx.ext.consul.TxnKVVerb.valueOf((String)member.getValue()));
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

  public static void toJson(TxnKVOperation obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TxnKVOperation obj, java.util.Map<String, Object> json) {
    json.put("flags", obj.getFlags());
    json.put("index", obj.getIndex());
    if (obj.getKey() != null) {
      json.put("key", obj.getKey());
    }
    if (obj.getSession() != null) {
      json.put("session", obj.getSession());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType().name());
    }
    if (obj.getValue() != null) {
      json.put("value", obj.getValue());
    }
  }
}
