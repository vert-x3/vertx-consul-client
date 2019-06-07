package io.vertx.ext.consul.v1.kv;

import io.vertx.ext.consul.BlockingQueryOptions;
import io.vertx.ext.consul.v1.ConsistencyMode;

public class KvGetOptions {

  private BlockingQueryOptions blockingQueryOptions;
  private ConsistencyMode consistencyMode;
  private boolean recurse;
  private String dc;
  private String separator;

  public void setBlockingQueryOptions(BlockingQueryOptions blockingQueryOptions) {
    this.blockingQueryOptions = blockingQueryOptions;
  }

  public void setConsistencyMode(ConsistencyMode consistencyMode) {
    this.consistencyMode = consistencyMode;
  }

  public void setRecurse(boolean recurse) {
    this.recurse = recurse;
  }

  public String getDc() {
    return dc;
  }

  public void setDc(String dc) {
    this.dc = dc;
  }

  public String getSeparator() {
    return separator;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }

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
