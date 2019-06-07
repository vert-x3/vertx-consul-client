package io.vertx.ext.consul.v1.kv;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.v1.QueryOptions;
import io.vertx.ext.consul.v1.WriteOptions;


/**
 * An object to consul /kv endpoint functionality interaction.
 */
@VertxGen
public interface KV {

  @Fluent
  KV get(String key, QueryOptions options, Handler<AsyncResult<QueryMetaWith<KVPair>>> handler);

  @Fluent
  KV acquire(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler);

  @Fluent
  KV cas(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler);

  @Fluent
  KV delete(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Void>>> handler);

  @Fluent
  KV deleteCas(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler);

  @Fluent
  KV deleteTree(String prefix, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Void>>> handler);

  @Fluent
  KV keys(String prefix, String separator, QueryOptions options, Handler<AsyncResult<QueryMetaWithList<String>>> handler);

  @Fluent
  KV keys(String prefix, QueryOptions options, Handler<AsyncResult<QueryMetaWithList<KVPair>>> handler);

  @Fluent
  KV put(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Void>>> handler);

  @Fluent
  KV release(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler);
}
