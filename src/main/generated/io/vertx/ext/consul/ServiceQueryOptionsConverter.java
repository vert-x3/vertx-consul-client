package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.ServiceQueryOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ServiceQueryOptions} original class using Vert.x codegen.
 */
public class ServiceQueryOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ServiceQueryOptions obj) {
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
        case "tag":
          if (member.getValue() instanceof String) {
            obj.setTag((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ServiceQueryOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ServiceQueryOptions obj, java.util.Map<String, Object> json) {
    if (obj.getBlockingOptions() != null) {
      json.put("blockingOptions", obj.getBlockingOptions().toJson());
    }
    if (obj.getNear() != null) {
      json.put("near", obj.getNear());
    }
    if (obj.getTag() != null) {
      json.put("tag", obj.getTag());
    }
  }
}
