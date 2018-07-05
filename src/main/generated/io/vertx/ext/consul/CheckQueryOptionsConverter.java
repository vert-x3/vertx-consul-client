package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.CheckQueryOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.CheckQueryOptions} original class using Vert.x codegen.
 */
public class CheckQueryOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, CheckQueryOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "blockingOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setBlockingOptions(new io.vertx.ext.consul.BlockingQueryOptions((JsonObject)member.getValue()));
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

  public static void toJson(CheckQueryOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(CheckQueryOptions obj, java.util.Map<String, Object> json) {
    if (obj.getBlockingOptions() != null) {
      json.put("blockingOptions", obj.getBlockingOptions().toJson());
    }
    if (obj.getNear() != null) {
      json.put("near", obj.getNear());
    }
  }
}
