package io.vertx.ext.consul;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.consul.CheckOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.consul.CheckOptions} original class using Vert.x codegen.
 */
public class CheckOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, CheckOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "deregisterAfter":
          if (member.getValue() instanceof String) {
            obj.setDeregisterAfter((String)member.getValue());
          }
          break;
        case "grpc":
          if (member.getValue() instanceof String) {
            obj.setGrpc((String)member.getValue());
          }
          break;
        case "grpcTls":
          if (member.getValue() instanceof Boolean) {
            obj.setGrpcTls((Boolean)member.getValue());
          }
          break;
        case "http":
          if (member.getValue() instanceof String) {
            obj.setHttp((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "interval":
          if (member.getValue() instanceof String) {
            obj.setInterval((String)member.getValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "notes":
          if (member.getValue() instanceof String) {
            obj.setNotes((String)member.getValue());
          }
          break;
        case "scriptArgs":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setScriptArgs(list);
          }
          break;
        case "serviceId":
          if (member.getValue() instanceof String) {
            obj.setServiceId((String)member.getValue());
          }
          break;
        case "status":
          if (member.getValue() instanceof String) {
            obj.setStatus(io.vertx.ext.consul.CheckStatus.valueOf((String)member.getValue()));
          }
          break;
        case "tcp":
          if (member.getValue() instanceof String) {
            obj.setTcp((String)member.getValue());
          }
          break;
        case "tlsSkipVerify":
          if (member.getValue() instanceof Boolean) {
            obj.setTlsSkipVerify((Boolean)member.getValue());
          }
          break;
        case "ttl":
          if (member.getValue() instanceof String) {
            obj.setTtl((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(CheckOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(CheckOptions obj, java.util.Map<String, Object> json) {
    if (obj.getDeregisterAfter() != null) {
      json.put("deregisterAfter", obj.getDeregisterAfter());
    }
    if (obj.getGrpc() != null) {
      json.put("grpc", obj.getGrpc());
    }
    json.put("grpcTls", obj.isGrpcTls());
    if (obj.getHttp() != null) {
      json.put("http", obj.getHttp());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getInterval() != null) {
      json.put("interval", obj.getInterval());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getNotes() != null) {
      json.put("notes", obj.getNotes());
    }
    if (obj.getScriptArgs() != null) {
      JsonArray array = new JsonArray();
      obj.getScriptArgs().forEach(item -> array.add(item));
      json.put("scriptArgs", array);
    }
    if (obj.getServiceId() != null) {
      json.put("serviceId", obj.getServiceId());
    }
    if (obj.getStatus() != null) {
      json.put("status", obj.getStatus().name());
    }
    if (obj.getTcp() != null) {
      json.put("tcp", obj.getTcp());
    }
    json.put("tlsSkipVerify", obj.isTlsSkipVerify());
    if (obj.getTtl() != null) {
      json.put("ttl", obj.getTtl());
    }
  }
}
