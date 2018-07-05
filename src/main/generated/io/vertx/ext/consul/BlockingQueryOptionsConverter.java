package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.BlockingQueryOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.BlockingQueryOptions} original class using Vert.x codegen.
 */
public class BlockingQueryOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, BlockingQueryOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "index":
          if (member.getValue() instanceof Number) {
            obj.setIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "wait":
          if (member.getValue() instanceof String) {
            obj.setWait((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(BlockingQueryOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(BlockingQueryOptions obj, java.util.Map<String, Object> json) {
    json.put("index", obj.getIndex());
    if (obj.getWait() != null) {
      json.put("wait", obj.getWait());
    }
  }
}
