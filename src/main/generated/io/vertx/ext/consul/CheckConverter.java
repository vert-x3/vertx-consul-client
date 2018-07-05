package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.Check}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.Check} original class using Vert.x codegen.
 */
public class CheckConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Check obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "nodeName":
          if (member.getValue() instanceof String) {
            obj.setNodeName((String)member.getValue());
          }
          break;
        case "notes":
          if (member.getValue() instanceof String) {
            obj.setNotes((String)member.getValue());
          }
          break;
        case "output":
          if (member.getValue() instanceof String) {
            obj.setOutput((String)member.getValue());
          }
          break;
        case "serviceId":
          if (member.getValue() instanceof String) {
            obj.setServiceId((String)member.getValue());
          }
          break;
        case "serviceName":
          if (member.getValue() instanceof String) {
            obj.setServiceName((String)member.getValue());
          }
          break;
        case "status":
          if (member.getValue() instanceof String) {
            obj.setStatus(io.vertx.ext.consul.CheckStatus.valueOf((String)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(Check obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Check obj, java.util.Map<String, Object> json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getNodeName() != null) {
      json.put("nodeName", obj.getNodeName());
    }
    if (obj.getNotes() != null) {
      json.put("notes", obj.getNotes());
    }
    if (obj.getOutput() != null) {
      json.put("output", obj.getOutput());
    }
    if (obj.getServiceId() != null) {
      json.put("serviceId", obj.getServiceId());
    }
    if (obj.getServiceName() != null) {
      json.put("serviceName", obj.getServiceName());
    }
    if (obj.getStatus() != null) {
      json.put("status", obj.getStatus().name());
    }
  }
}
