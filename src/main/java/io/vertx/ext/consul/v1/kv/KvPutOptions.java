package io.vertx.ext.consul.v1.kv;

public class KvPutOptions {

  private String dc;
  private int flags;
  private int cas;
  private String acquire;
  private String release;

  public String getDc() {
    return dc;
  }

  public void setDc(String dc) {
    this.dc = dc;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public int getCas() {
    return cas;
  }

  public void setCas(int cas) {
    this.cas = cas;
  }

  public String getAcquire() {
    return acquire;
  }

  public void setAcquire(String acquire) {
    this.acquire = acquire;
  }

  public String getRelease() {
    return release;
  }

  public void setRelease(String release) {
    this.release = release;
  }
}
