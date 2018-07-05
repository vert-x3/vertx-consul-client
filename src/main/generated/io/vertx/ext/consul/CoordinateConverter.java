package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.Coordinate}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Coordinate} original class using Vert.x codegen.
 */
public class CoordinateConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Coordinate obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "adj":
          if (member.getValue() instanceof Number) {
            obj.setAdj(((Number)member.getValue()).floatValue());
          }
          break;
        case "err":
          if (member.getValue() instanceof Number) {
            obj.setErr(((Number)member.getValue()).floatValue());
          }
          break;
        case "height":
          if (member.getValue() instanceof Number) {
            obj.setHeight(((Number)member.getValue()).floatValue());
          }
          break;
        case "node":
          if (member.getValue() instanceof String) {
            obj.setNode((String)member.getValue());
          }
          break;
        case "vec":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Float> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).floatValue());
            });
            obj.setVec(list);
          }
          break;
      }
    }
  }

  public static void toJson(Coordinate obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Coordinate obj, java.util.Map<String, Object> json) {
    json.put("adj", obj.getAdj());
    json.put("err", obj.getErr());
    json.put("height", obj.getHeight());
    if (obj.getNode() != null) {
      json.put("node", obj.getNode());
    }
    if (obj.getVec() != null) {
      JsonArray array = new JsonArray();
      obj.getVec().forEach(item -> array.add(item));
      json.put("vec", array);
    }
  }
}
