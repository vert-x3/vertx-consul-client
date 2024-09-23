package io.vertx.ext.consul.tests.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.KeyValueList;
import io.vertx.ext.consul.impl.WatchImpl;

public class WatchKeyPrefixCnt extends WatchImpl.KeyPrefix {

  private int cnt = 0;

  public WatchKeyPrefixCnt(String keyPrefix, Vertx vertx, ConsulClientOptions options) {
    super(keyPrefix, vertx, options);
  }

  @Override
  protected void wait(long index, Handler<AsyncResult<WatchImpl.State<KeyValueList>>> handler) {
    cnt++;
    super.wait(index, handler);
  }

  public int cnt() {
    return cnt;
  }
}
