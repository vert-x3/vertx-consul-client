package io.vertx.ext.consul.v1.kv.impl;

import io.vertx.ext.consul.v1.kv.QueryMeta;

public class QueryMetaImpl implements QueryMeta {
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

  @Override
  public long lastIndex() {
    return lastIndex;
  }

  @Override
  public QueryMeta setLastIndex(long lastIndex) {
    this.lastIndex = lastIndex;
    return this;
  }

  @Override
  public String lastContentHash() {
    return lastContentHash;
  }

  @Override
  public QueryMeta setLastContentHash(String lastContentHash) {
    this.lastContentHash = lastContentHash;
    return this;
  }

  @Override
  public long lastContactMillis() {
    return lastContactMillis;
  }

  @Override
  public QueryMeta setLastContactMillis(long lastContactMillis) {
    this.lastContactMillis = lastContactMillis;
    return this;
  }

  @Override
  public boolean knownLeader() {
    return knownLeader;
  }

  @Override
  public QueryMeta setKnownLeader(boolean knownLeader) {
    this.knownLeader = knownLeader;
    return this;
  }

  @Override
  public long requestTimeMillis() {
    return requestTimeMillis;
  }

  @Override
  public QueryMeta setRequestTimeMillis(long requestTimeMillis) {
    this.requestTimeMillis = requestTimeMillis;
    return this;
  }

  @Override
  public boolean addressTranslationEnabled() {
    return addressTranslationEnabled;
  }

  @Override
  public QueryMeta setAddressTranslationEnabled(boolean addressTranslationEnabled) {
    this.addressTranslationEnabled = addressTranslationEnabled;
    return this;
  }

  @Override
  public boolean cacheHit() {
    return cacheHit;
  }

  @Override
  public QueryMeta setCacheHit(boolean cacheHit) {
    this.cacheHit = cacheHit;
    return this;
  }

  @Override
  public long cacheAgeSeconds() {
    return cacheAgeSeconds;
  }

  @Override
  public QueryMeta setCacheAgeSeconds(long cacheAgeSeconds) {
    this.cacheAgeSeconds = cacheAgeSeconds;
    return this;
  }
}
