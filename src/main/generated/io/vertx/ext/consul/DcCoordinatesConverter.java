package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.DcCoordinates}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.DcCoordinates} original class using Vert.x codegen.
 */
public class DcCoordinatesConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DcCoordinates obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "datacenter":
          if (member.getValue() instanceof String) {
            obj.setDatacenter((String)member.getValue());
          }
          break;
        case "servers":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.consul.Coordinate> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.consul.Coordinate((JsonObject)item));
            });
            obj.setServers(list);
          }
          break;
      }
    }
  }

  public static void toJson(DcCoordinates obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DcCoordinates obj, java.util.Map<String, Object> json) {
    if (obj.getDatacenter() != null) {
      json.put("datacenter", obj.getDatacenter());
    }
    if (obj.getServers() != null) {
      JsonArray array = new JsonArray();
      obj.getServers().forEach(item -> array.add(item.toJson()));
      json.put("servers", array);
    }
  }
}
