package io.vertx.ext.consul.v1.acl.token;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

public interface Tokens {

  Tokens list(Handler<AsyncResult<List<TokenInfo>>> handler);

  Tokens list(String policy, Handler<AsyncResult<List<TokenInfo>>> handler);
}
