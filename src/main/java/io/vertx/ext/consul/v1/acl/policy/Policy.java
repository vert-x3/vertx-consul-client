package io.vertx.ext.consul.v1.acl.policy;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface Policy {

  Policy create(PolicyPayload payload, Handler<AsyncResult<PolicyInfo>> handler);

  Policy read(String id, Handler<AsyncResult<PolicyInfo>> handler);

  Policy update(String id, PolicyPayload payload, Handler<AsyncResult<PolicyInfo>> handler);

  Policy delete(String id, Handler<AsyncResult<Boolean>> handler);

  Policy list();
}
