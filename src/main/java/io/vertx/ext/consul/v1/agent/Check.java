package io.vertx.ext.consul.v1.agent;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface Check {

  Check register(RegisterPayload payload, Handler<AsyncResult<Boolean>> handler);

  Check deregister(String checkId, Handler<AsyncResult<Boolean>> handler);

  Check pass(String checkId, String note, Handler<AsyncResult<Boolean>> handler);

  Check warn(String checkId, String note, Handler<AsyncResult<Boolean>> handler);

  Check fail(String checkId, String note, Handler<AsyncResult<Boolean>> handler);

  Check update(String checkId, String status, String output, Handler<AsyncResult<Boolean>> handler);
}
