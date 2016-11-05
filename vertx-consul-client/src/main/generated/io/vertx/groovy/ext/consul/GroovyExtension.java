package io.vertx.groovy.ext.consul;
public class GroovyExtension {
  public static io.vertx.ext.consul.ConsulClient agentInfo(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.agentInfo(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(event)));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient coordinateNodes(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.coordinateNodes(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Coordinate>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Coordinate>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient coordinateDatacenters(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.coordinateDatacenters(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.DcCoordinates>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.DcCoordinates>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient getValue(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String key, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.getValue(key,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.KeyValue>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.KeyValue> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient getValueBlocking(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String key, java.util.Map<String, Object> options, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.getValueBlocking(key,
      options != null ? new io.vertx.ext.consul.BlockingQueryOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.KeyValue>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.KeyValue> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient getValues(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String keyPrefix, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.getValues(keyPrefix,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient getValuesBlocking(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String keyPrefix, java.util.Map<String, Object> options, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.getValuesBlocking(keyPrefix,
      options != null ? new io.vertx.ext.consul.BlockingQueryOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.KeyValue>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient putValueWithOptions(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String key, java.lang.String value, java.util.Map<String, Object> options, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Boolean>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.putValueWithOptions(key,
      value,
      options != null ? new io.vertx.ext.consul.KeyValueOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Boolean>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Boolean> ar) {
        resultHandler.handle(ar.map(event -> event));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient createAclToken(io.vertx.ext.consul.ConsulClient j_receiver, java.util.Map<String, Object> token, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>> idHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.createAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.ConversionHelper.toJsonObject(token)) : null,
      idHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.String> ar) {
        idHandler.handle(ar.map(event -> event));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient updateAclToken(io.vertx.ext.consul.ConsulClient j_receiver, java.util.Map<String, Object> token, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>> idHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.updateAclToken(token != null ? new io.vertx.ext.consul.AclToken(io.vertx.lang.groovy.ConversionHelper.toJsonObject(token)) : null,
      idHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.String> ar) {
        idHandler.handle(ar.map(event -> event));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient listAclTokens(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.listAclTokens(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.AclToken>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.AclToken>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient infoAclToken(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String id, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> tokenHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.infoAclToken(id,
      tokenHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.AclToken>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.AclToken> ar) {
        tokenHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient fireEvent(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String name, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.fireEvent(name,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.Event>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.Event> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient fireEventWithOptions(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String name, java.util.Map<String, Object> options, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.fireEventWithOptions(name,
      options != null ? new io.vertx.ext.consul.EventOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.Event>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.Event> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient listEvents(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.listEvents(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Event>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Event>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient registerService(io.vertx.ext.consul.ConsulClient j_receiver, java.util.Map<String, Object> serviceOptions, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.registerService(serviceOptions != null ? new io.vertx.ext.consul.ServiceOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(serviceOptions)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient maintenanceService(io.vertx.ext.consul.ConsulClient j_receiver, java.util.Map<String, Object> maintenanceOptions, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.maintenanceService(maintenanceOptions != null ? new io.vertx.ext.consul.MaintenanceOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(maintenanceOptions)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient catalogServiceNodes(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String service, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.catalogServiceNodes(service,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient catalogServiceNodesWithTag(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String service, java.lang.String tag, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.catalogServiceNodesWithTag(service,
      tag,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient catalogNodes(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.catalogNodes(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Node>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Node>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient catalogServices(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.catalogServices(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient catalogNodeServices(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String node, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.catalogNodeServices(node,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient localServices(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.localServices(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Service>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient localChecks(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.localChecks(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Check>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Check>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient registerCheck(io.vertx.ext.consul.ConsulClient j_receiver, java.util.Map<String, Object> checkOptions, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.registerCheck(checkOptions != null ? new io.vertx.ext.consul.CheckOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(checkOptions)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient createSessionWithOptions(io.vertx.ext.consul.ConsulClient j_receiver, java.util.Map<String, Object> options, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>> idHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.createSessionWithOptions(options != null ? new io.vertx.ext.consul.SessionOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null,
      idHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.String> ar) {
        idHandler.handle(ar.map(event -> event));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient infoSession(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String id, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.infoSession(id,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.Session>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.Session> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient renewSession(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String id, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.renewSession(id,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.consul.Session>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.consul.Session> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient listSessions(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.listSessions(resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Session>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Session>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
  public static io.vertx.ext.consul.ConsulClient listNodeSessions(io.vertx.ext.consul.ConsulClient j_receiver, java.lang.String nodeId, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.listNodeSessions(nodeId,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Session>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.ext.consul.Session>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null));
    return j_receiver;
  }
}
