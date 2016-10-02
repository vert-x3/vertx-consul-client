/*
* Copyright 2014 Red Hat, Inc.
*
* Red Hat licenses this file to you under the Apache License, version 2.0
* (the "License"); you may not use this file except in compliance with the
* License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/

package io.vertx.ext.consul;

import io.vertx.ext.consul.ConsulService;
import io.vertx.core.Vertx;
import io.vertx.core.Handler;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.serviceproxy.ProxyHandler;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
import io.vertx.ext.consul.ConsulService;
import io.vertx.ext.consul.Event;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.MaintenanceOptions;
import io.vertx.ext.consul.ServiceInfo;
import io.vertx.ext.consul.CheckInfo;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.ServiceOptions;
import java.util.List;
import io.vertx.ext.consul.KeyValueOptions;
import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.SessionOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.Session;

/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConsulServiceVertxProxyHandler extends ProxyHandler {

  public static final long DEFAULT_CONNECTION_TIMEOUT = 5 * 60; // 5 minutes 

  private final Vertx vertx;
  private final ConsulService service;
  private final long timerID;
  private long lastAccessed;
  private final long timeoutSeconds;

  public ConsulServiceVertxProxyHandler(Vertx vertx, ConsulService service) {
    this(vertx, service, DEFAULT_CONNECTION_TIMEOUT);
  }

  public ConsulServiceVertxProxyHandler(Vertx vertx, ConsulService service, long timeoutInSecond) {
    this(vertx, service, true, timeoutInSecond);
  }

  public ConsulServiceVertxProxyHandler(Vertx vertx, ConsulService service, boolean topLevel, long timeoutSeconds) {
    this.vertx = vertx;
    this.service = service;
    this.timeoutSeconds = timeoutSeconds;
    try {
      this.vertx.eventBus().registerDefaultCodec(ServiceException.class,
          new ServiceExceptionMessageCodec());
    } catch (IllegalStateException ex) {}
    if (timeoutSeconds != -1 && !topLevel) {
      long period = timeoutSeconds * 1000 / 2;
      if (period > 10000) {
        period = 10000;
      }
      this.timerID = vertx.setPeriodic(period, this::checkTimedOut);
    } else {
      this.timerID = -1;
    }
    accessed();
  }

  public MessageConsumer<JsonObject> registerHandler(String address) {
    MessageConsumer<JsonObject> consumer = vertx.eventBus().<JsonObject>consumer(address).handler(this);
    this.setConsumer(consumer);
    return consumer;
  }

  private void checkTimedOut(long id) {
    long now = System.nanoTime();
    if (now - lastAccessed > timeoutSeconds * 1000000000) {
      close();
    }
  }

  @Override
  public void close() {
    if (timerID != -1) {
      vertx.cancelTimer(timerID);
    }
    super.close();
  }

  private void accessed() {
    this.lastAccessed = System.nanoTime();
  }

  public void handle(Message<JsonObject> msg) {
    try {
      JsonObject json = msg.body();
      String action = msg.headers().get("action");
      if (action == null) {
        throw new IllegalStateException("action not specified");
      }
      accessed();
      switch (action) {

        case "getValue": {
          service.getValue((java.lang.String)json.getValue("key"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(res.result() == null ? null : res.result().toJson());
            }
         });
          break;
        }
        case "deleteValue": {
          service.deleteValue((java.lang.String)json.getValue("key"), createHandler(msg));
          break;
        }
        case "getValues": {
          service.getValues((java.lang.String)json.getValue("keyPrefix"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(KeyValue::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "deleteValues": {
          service.deleteValues((java.lang.String)json.getValue("keyPrefix"), createHandler(msg));
          break;
        }
        case "putValue": {
          service.putValue((java.lang.String)json.getValue("key"), (java.lang.String)json.getValue("value"), createHandler(msg));
          break;
        }
        case "putValueWithOptions": {
          service.putValueWithOptions((java.lang.String)json.getValue("key"), (java.lang.String)json.getValue("value"), json.getJsonObject("options") == null ? null : new io.vertx.ext.consul.KeyValueOptions(json.getJsonObject("options")), createHandler(msg));
          break;
        }
        case "createAclToken": {
          service.createAclToken(json.getJsonObject("token") == null ? null : new io.vertx.ext.consul.AclToken(json.getJsonObject("token")), createHandler(msg));
          break;
        }
        case "updateAclToken": {
          service.updateAclToken(json.getJsonObject("token") == null ? null : new io.vertx.ext.consul.AclToken(json.getJsonObject("token")), createHandler(msg));
          break;
        }
        case "cloneAclToken": {
          service.cloneAclToken((java.lang.String)json.getValue("id"), createHandler(msg));
          break;
        }
        case "listAclTokens": {
          service.listAclTokens(res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(AclToken::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "infoAclToken": {
          service.infoAclToken((java.lang.String)json.getValue("id"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(res.result() == null ? null : res.result().toJson());
            }
         });
          break;
        }
        case "destroyAclToken": {
          service.destroyAclToken((java.lang.String)json.getValue("id"), createHandler(msg));
          break;
        }
        case "fireEvent": {
          service.fireEvent(json.getJsonObject("event") == null ? null : new io.vertx.ext.consul.Event(json.getJsonObject("event")), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(res.result() == null ? null : res.result().toJson());
            }
         });
          break;
        }
        case "listEvents": {
          service.listEvents(res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(Event::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "registerService": {
          service.registerService(json.getJsonObject("service") == null ? null : new io.vertx.ext.consul.ServiceOptions(json.getJsonObject("service")), createHandler(msg));
          break;
        }
        case "maintenanceService": {
          service.maintenanceService(json.getJsonObject("maintenanceOptions") == null ? null : new io.vertx.ext.consul.MaintenanceOptions(json.getJsonObject("maintenanceOptions")), createHandler(msg));
          break;
        }
        case "deregisterService": {
          service.deregisterService((java.lang.String)json.getValue("id"), createHandler(msg));
          break;
        }
        case "infoService": {
          service.infoService((java.lang.String)json.getValue("name"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(ServiceInfo::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "localServices": {
          service.localServices(res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(ServiceInfo::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "localChecks": {
          service.localChecks(res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(CheckInfo::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "registerCheck": {
          service.registerCheck(json.getJsonObject("check") == null ? null : new io.vertx.ext.consul.CheckOptions(json.getJsonObject("check")), createHandler(msg));
          break;
        }
        case "deregisterCheck": {
          service.deregisterCheck((java.lang.String)json.getValue("id"), createHandler(msg));
          break;
        }
        case "passCheck": {
          service.passCheck(json.getJsonObject("check") == null ? null : new io.vertx.ext.consul.CheckOptions(json.getJsonObject("check")), createHandler(msg));
          break;
        }
        case "warnCheck": {
          service.warnCheck(json.getJsonObject("check") == null ? null : new io.vertx.ext.consul.CheckOptions(json.getJsonObject("check")), createHandler(msg));
          break;
        }
        case "failCheck": {
          service.failCheck(json.getJsonObject("check") == null ? null : new io.vertx.ext.consul.CheckOptions(json.getJsonObject("check")), createHandler(msg));
          break;
        }
        case "updateCheck": {
          service.updateCheck(json.getJsonObject("checkInfo") == null ? null : new io.vertx.ext.consul.CheckInfo(json.getJsonObject("checkInfo")), createHandler(msg));
          break;
        }
        case "leaderStatus": {
          service.leaderStatus(createHandler(msg));
          break;
        }
        case "peersStatus": {
          service.peersStatus(createListHandler(msg));
          break;
        }
        case "createSession": {
          service.createSession(json.getJsonObject("options") == null ? null : new io.vertx.ext.consul.SessionOptions(json.getJsonObject("options")), createHandler(msg));
          break;
        }
        case "infoSession": {
          service.infoSession((java.lang.String)json.getValue("id"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(res.result() == null ? null : res.result().toJson());
            }
         });
          break;
        }
        case "renewSession": {
          service.renewSession((java.lang.String)json.getValue("id"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(res.result() == null ? null : res.result().toJson());
            }
         });
          break;
        }
        case "listSessions": {
          service.listSessions(res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(Session::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "listNodeSessions": {
          service.listNodeSessions((java.lang.String)json.getValue("nodeId"), res -> {
            if (res.failed()) {
              if (res.cause() instanceof ServiceException) {
                msg.reply(res.cause());
              } else {
                msg.reply(new ServiceException(-1, res.cause().getMessage()));
              }
            } else {
              msg.reply(new JsonArray(res.result().stream().map(Session::toJson).collect(Collectors.toList())));
            }
         });
          break;
        }
        case "destroySession": {
          service.destroySession((java.lang.String)json.getValue("id"), createHandler(msg));
          break;
        }
        case "close": {
          service.close();
          break;
        }
        default: {
          throw new IllegalStateException("Invalid action: " + action);
        }
      }
    } catch (Throwable t) {
      msg.reply(new ServiceException(500, t.getMessage()));
      throw t;
    }
  }

  private <T> Handler<AsyncResult<T>> createHandler(Message msg) {
    return res -> {
      if (res.failed()) {
        if (res.cause() instanceof ServiceException) {
          msg.reply(res.cause());
        } else {
          msg.reply(new ServiceException(-1, res.cause().getMessage()));
        }
      } else {
        if (res.result() != null  && res.result().getClass().isEnum()) {
          msg.reply(((Enum) res.result()).name());
        } else {
          msg.reply(res.result());
        }
      }
    };
  }

  private <T> Handler<AsyncResult<List<T>>> createListHandler(Message msg) {
    return res -> {
      if (res.failed()) {
        if (res.cause() instanceof ServiceException) {
          msg.reply(res.cause());
        } else {
          msg.reply(new ServiceException(-1, res.cause().getMessage()));
        }
      } else {
        msg.reply(new JsonArray(res.result()));
      }
    };
  }

  private <T> Handler<AsyncResult<Set<T>>> createSetHandler(Message msg) {
    return res -> {
      if (res.failed()) {
        if (res.cause() instanceof ServiceException) {
          msg.reply(res.cause());
        } else {
          msg.reply(new ServiceException(-1, res.cause().getMessage()));
        }
      } else {
        msg.reply(new JsonArray(new ArrayList<>(res.result())));
      }
    };
  }

  private Handler<AsyncResult<List<Character>>> createListCharHandler(Message msg) {
    return res -> {
      if (res.failed()) {
        if (res.cause() instanceof ServiceException) {
          msg.reply(res.cause());
        } else {
          msg.reply(new ServiceException(-1, res.cause().getMessage()));
        }
      } else {
        JsonArray arr = new JsonArray();
        for (Character chr: res.result()) {
          arr.add((int) chr);
        }
        msg.reply(arr);
      }
    };
  }

  private Handler<AsyncResult<Set<Character>>> createSetCharHandler(Message msg) {
    return res -> {
      if (res.failed()) {
        if (res.cause() instanceof ServiceException) {
          msg.reply(res.cause());
        } else {
          msg.reply(new ServiceException(-1, res.cause().getMessage()));
        }
      } else {
        JsonArray arr = new JsonArray();
        for (Character chr: res.result()) {
          arr.add((int) chr);
        }
        msg.reply(arr);
      }
    };
  }

  private <T> Map<String, T> convertMap(Map map) {
    return (Map<String, T>)map;
  }

  private <T> List<T> convertList(List list) {
    return (List<T>)list;
  }

  private <T> Set<T> convertSet(List list) {
    return new HashSet<T>((List<T>)list);
  }
}