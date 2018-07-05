package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.PreparedQueryExecuteOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.PreparedQueryExecuteOptions} original class using Vert.x codegen.
 */
public class PreparedQueryExecuteOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, PreparedQueryExecuteOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "limit":
          if (member.getValue() instanceof Number) {
            obj.setLimit(((Number)member.getValue()).intValue());
          }
          break;
        case "near":
          if (member.getValue() instanceof String) {
            obj.setNear((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(PreparedQueryExecuteOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(PreparedQueryExecuteOptions obj, java.util.Map<String, Object> json) {
    json.put("limit", obj.getLimit());
    if (obj.getNear() != null) {
      json.put("near", obj.getNear());
    }
  }
}
