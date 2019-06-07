package io.vertx.ext.consul.v1.kv;

public class KvDeleteOptions {

  private boolean recurse;
  private int cas;

  public boolean isRecurse() {
    return recurse;
  }

  public void setRecurse(boolean recurse) {
    this.recurse = recurse;
  }

  public int getCas() {
    return cas;
  }

  public void setCas(int cas) {
    this.cas = cas;
  }
}
