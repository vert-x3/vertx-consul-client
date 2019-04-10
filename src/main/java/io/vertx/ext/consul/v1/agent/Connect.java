package io.vertx.ext.consul.v1.agent;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


public interface Connect {

  Connect authorize(String target, String clientCertURI, String clientCertSerial, Handler<AsyncResult<AuthorizeResult>> handler);

  Connect caRoots(Handler<AsyncResult<CaRoots>> handler);

  Connect caLeaf(String service, Handler<AsyncResult<CaLeaf>> handler);

  Connect proxy(String id, Handler<AsyncResult<ProxyInfo>> handler);

}
