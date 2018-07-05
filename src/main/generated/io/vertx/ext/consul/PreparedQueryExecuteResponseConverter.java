package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.PreparedQueryExecuteResponse}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.PreparedQueryExecuteResponse} original class using Vert.x codegen.
 */
public class PreparedQueryExecuteResponseConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, PreparedQueryExecuteResponse obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "dc":
          if (member.getValue() instanceof String) {
            obj.setDc((String)member.getValue());
          }
          break;
        case "dnsTtl":
          if (member.getValue() instanceof String) {
            obj.setDnsTtl((String)member.getValue());
          }
          break;
        case "failovers":
          if (member.getValue() instanceof Number) {
            obj.setFailovers(((Number)member.getValue()).intValue());
          }
          break;
        case "nodes":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.consul.ServiceEntry> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.consul.ServiceEntry((JsonObject)item));
            });
            obj.setNodes(list);
          }
          break;
        case "service":
          if (member.getValue() instanceof String) {
            obj.setService((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(PreparedQueryExecuteResponse obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(PreparedQueryExecuteResponse obj, java.util.Map<String, Object> json) {
    if (obj.getDc() != null) {
      json.put("dc", obj.getDc());
    }
    if (obj.getDnsTtl() != null) {
      json.put("dnsTtl", obj.getDnsTtl());
    }
    json.put("failovers", obj.getFailovers());
    if (obj.getNodes() != null) {
      JsonArray array = new JsonArray();
      obj.getNodes().forEach(item -> array.add(item.toJson()));
      json.put("nodes", array);
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService());
    }
  }
}
