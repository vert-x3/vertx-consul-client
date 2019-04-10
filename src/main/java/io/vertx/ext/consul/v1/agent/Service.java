package io.vertx.ext.consul.v1.agent;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface Service {

  Service get(String serviceId, Handler<AsyncResult<ServiceInfo>> handler);

  Service healthByName(String name, Handler<AsyncResult<HealthStatus>> handler);

  Service healthById(String serviceId, Handler<AsyncResult<HealthStatus>> handler);

  Service register(RegisterPayload registerPayload, Handler<AsyncResult<Boolean>> handler);

  Service deregister(String serviceId, Handler<AsyncResult<Boolean>> handler);

  Service maintenance(String serviceId, Boolean enable, Handler<AsyncResult<Boolean>> handler);

  Service maintenance(String serviceId, Boolean enable, String reason, Handler<AsyncResult<Boolean>> handler);

}
