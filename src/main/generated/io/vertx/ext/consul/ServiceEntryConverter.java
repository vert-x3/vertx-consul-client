package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.ServiceEntry}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.ServiceEntry} original class using Vert.x codegen.
 */
public class ServiceEntryConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ServiceEntry obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "checks":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.consul.Check> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.consul.Check((JsonObject)item));
            });
            obj.setChecks(list);
          }
          break;
        case "node":
          if (member.getValue() instanceof JsonObject) {
            obj.setNode(new io.vertx.ext.consul.Node((JsonObject)member.getValue()));
          }
          break;
        case "service":
          if (member.getValue() instanceof JsonObject) {
            obj.setService(new io.vertx.ext.consul.Service((JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(ServiceEntry obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ServiceEntry obj, java.util.Map<String, Object> json) {
    if (obj.getChecks() != null) {
      JsonArray array = new JsonArray();
      obj.getChecks().forEach(item -> array.add(item.toJson()));
      json.put("checks", array);
    }
    if (obj.getNode() != null) {
      json.put("node", obj.getNode().toJson());
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService().toJson());
    }
  }
}
