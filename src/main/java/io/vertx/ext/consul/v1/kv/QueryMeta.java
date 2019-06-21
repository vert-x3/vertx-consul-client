package io.vertx.ext.consul.v1.kv;

import io.vertx.codegen.annotations.DataObject;

@DataObject(generateConverter = true)
public class QueryMeta {

  // LastIndex. This can be used as a WaitIndex to perform
  // a blocking query
  private long lastIndex;

  // LastContentHash. This can be used as a WaitHash to perform a blocking query
  // for endpoints that support hash-based blocking. Endpoints that do not
  // support it will return an empty hash.
  private String lastContentHash;

  // Time of last contact from the leader for the
  // server servicing the request
  private long lastContactMillis;

  // Is there a known leader
  private boolean knownLeader;

  // How long did the request take
  private long requestTimeMillis;

  // Is address translation enabled for HTTP responses on this agent
  private boolean addressTranslationEnabled;

  // CacheHit is true if the result was served from agent-local cache.
  private boolean cacheHit;

  // CacheAge is set if request was ?cached and indicates how stale the cached
  // response is.
  private long cacheAgeSeconds;

  public long getLastIndex() {
    return lastIndex;
  }

  public void setLastIndex(long lastIndex) {
    this.lastIndex = lastIndex;
  }

  public String getLastContentHash() {
    return lastContentHash;
  }

  public void setLastContentHash(String lastContentHash) {
    this.lastContentHash = lastContentHash;
  }

  public long getLastContactMillis() {
    return lastContactMillis;
  }

  public void setLastContactMillis(long lastContactMillis) {
    this.lastContactMillis = lastContactMillis;
  }

  public boolean isKnownLeader() {
    return knownLeader;
  }

  public void setKnownLeader(boolean knownLeader) {
    this.knownLeader = knownLeader;
  }

  public long getRequestTimeMillis() {
    return requestTimeMillis;
  }

  public void setRequestTimeMillis(long requestTimeMillis) {
    this.requestTimeMillis = requestTimeMillis;
  }

  public boolean isAddressTranslationEnabled() {
    return addressTranslationEnabled;
  }

  public void setAddressTranslationEnabled(boolean addressTranslationEnabled) {
    this.addressTranslationEnabled = addressTranslationEnabled;
  }

  public boolean isCacheHit() {
    return cacheHit;
  }

  public void setCacheHit(boolean cacheHit) {
    this.cacheHit = cacheHit;
  }

  public long getCacheAgeSeconds() {
    return cacheAgeSeconds;
  }

  public void setCacheAgeSeconds(long cacheAgeSeconds) {
    this.cacheAgeSeconds = cacheAgeSeconds;
  }
}
