package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.SessionList}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.SessionList} original class using Vert.x codegen.
 */
public class SessionListConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, SessionList obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "index":
          if (member.getValue() instanceof Number) {
            obj.setIndex(((Number)member.getValue()).longValue());
          }
          break;
        case "list":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.consul.Session> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.consul.Session((JsonObject)item));
            });
            obj.setList(list);
          }
          break;
      }
    }
  }

  public static void toJson(SessionList obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(SessionList obj, java.util.Map<String, Object> json) {
    json.put("index", obj.getIndex());
    if (obj.getList() != null) {
      JsonArray array = new JsonArray();
      obj.getList().forEach(item -> array.add(item.toJson()));
      json.put("list", array);
    }
  }
}
