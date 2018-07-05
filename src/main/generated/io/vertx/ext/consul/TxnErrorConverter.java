package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.TxnError}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.TxnError} original class using Vert.x codegen.
 */
public class TxnErrorConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TxnError obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "opIndex":
          if (member.getValue() instanceof Number) {
            obj.setOpIndex(((Number)member.getValue()).intValue());
          }
          break;
        case "what":
          if (member.getValue() instanceof String) {
            obj.setWhat((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(TxnError obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TxnError obj, java.util.Map<String, Object> json) {
    json.put("opIndex", obj.getOpIndex());
    if (obj.getWhat() != null) {
      json.put("what", obj.getWhat());
    }
  }
}
