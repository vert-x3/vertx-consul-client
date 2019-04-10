package io.vertx.ext.consul.v1.acl.token;


import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface Token {

  Token create(Payload payload, Handler<AsyncResult<TokenInfo>> handler);

  Token read(String accessorId, Handler<AsyncResult<TokenInfo>> handler);

  Token self(String consulToken, Handler<AsyncResult<TokenInfo>> handler);

  Token update(Payload payload, Handler<AsyncResult<TokenInfo>> handler);

  Token clone(Payload payload, Handler<AsyncResult<TokenInfo>> handler);

  Token clone(Payload payload, String description, Handler<AsyncResult<TokenInfo>> handler);

  Token delete(String accessorId, Handler<AsyncResult<Boolean>> handler);
}
