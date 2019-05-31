package io.vertx.ext.consul.v1.kv;

import io.vertx.ext.consul.BlockingQueryOptions;
import io.vertx.ext.consul.v1.ConsistencyMode;

public class KvGetOptions {

  private BlockingQueryOptions blockingQueryOptions;
  private ConsistencyMode consistencyMode;
  private boolean recurse;


  public BlockingQueryOptions getBlockingQueryOptions() {
    return blockingQueryOptions;
  }

  public ConsistencyMode getConsistencyMode() {
    return consistencyMode;
  }

  public boolean isRecurse() {
    return recurse;
  }
}
