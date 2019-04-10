package io.vertx.ext.consul.v1.agent;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface Services {

  Services list(Handler<AsyncResult<ServiceInfo>> handler);
}
