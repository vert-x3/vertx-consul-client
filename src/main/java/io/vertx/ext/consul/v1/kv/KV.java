package io.vertx.ext.consul.v1.kv;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.v1.KeyValueList;

import java.util.List;


/**
 * An object to consul /kv endpoint functionality interaction.
 */
public interface KV {

  KV get(String key, Handler<AsyncResult<KeyValueList>> resultHandler);

  KV get(String key, KvGetOptions options, Handler<AsyncResult<KeyValueList>> resultHandler);

  KV keys(String key, Handler<AsyncResult<List<String>>> resultHandler);

  KV keys(String key, KvGetOptions options, Handler<AsyncResult<List<String>>> resultHandler);

  KV put(String key, String value, Handler<AsyncResult<Boolean>> resultHandler);

  KV put(String key, String value, KvPutOptions options, Handler<AsyncResult<Boolean>> resultHandler);

  KV delete(String key, Handler<AsyncResult<Boolean>> resultHandler);

  KV delete(String key, KvDeleteOptions options, Handler<AsyncResult<Boolean>> resultHandler);
}
