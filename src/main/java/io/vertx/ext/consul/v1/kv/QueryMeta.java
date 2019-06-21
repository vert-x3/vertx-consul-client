package io.vertx.ext.consul.v1.kv;

import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public interface QueryMeta {

  // LastIndex. This can be used as a WaitIndex to perform
  // a blocking query
  long lastIndex();

  QueryMeta setLastIndex(long lastIndex);

  // LastContentHash. This can be used as a WaitHash to perform a blocking query
  // for endpoints that support hash-based blocking. Endpoints that do not
  // support it will return an empty hash.
  String lastContentHash();

  QueryMeta setLastContentHash(String lastContentHash);

  // Time of last contact from the leader for the
  // server servicing the request
  long lastContactMillis();

  QueryMeta setLastContactMillis(long lastContactMillis);

  // Is there a known leader
  boolean knownLeader();

  QueryMeta setKnownLeader(boolean knownLeader);

  // How long did the request take
  long requestTimeMillis();

  QueryMeta setRequestTimeMillis(long requestTimeMillis);

  // Is address translation enabled for HTTP responses on this agent
  boolean addressTranslationEnabled();

  QueryMeta setAddressTranslationEnabled(boolean addressTranslationEnabled);

  // CacheHit is true if the result was served from agent-local cache.
  boolean cacheHit();

  QueryMeta setCacheHit(boolean cacheHit);

  // CacheAge is set if request was ?cached and indicates how stale the cached
  // response is.
  long cacheAgeSeconds();

  QueryMeta setCacheAgeSeconds(long cacheAgeSeconds);

}
