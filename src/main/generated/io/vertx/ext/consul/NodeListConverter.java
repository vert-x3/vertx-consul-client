package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.NodeList}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.NodeList} original class using Vert.x codegen.
 */
public class NodeListConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, NodeList obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "index":
          if (member.getValue() instanceof Number) {
            obj.setIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "list":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.consul.Node> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.consul.Node((JsonObject)item));
            });
            obj.setList(list);
          }
          break;
      }
    }
  }

  public static void toJson(NodeList obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(NodeList obj, java.util.Map<String, Object> json) {
    json.put("index", obj.getIndex());
    if (obj.getList() != null) {
      JsonArray array = new JsonArray();
      obj.getList().forEach(item -> array.add(item.toJson()));
      json.put("list", array);
    }
  }
}
