package io.vertx.ext.consul.v1.kv.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.BlockingQueryOptions;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.KeyValueList;
import io.vertx.ext.consul.impl.KVParser;
import io.vertx.ext.consul.impl.Query;
import io.vertx.ext.consul.v1.ConsistencyMode;
import io.vertx.ext.consul.v1.ConsulRequester;
import io.vertx.ext.consul.v1.kv.KV;
import io.vertx.ext.consul.v1.kv.KvDeleteOptions;
import io.vertx.ext.consul.v1.kv.KvGetOptions;
import io.vertx.ext.consul.v1.kv.KvPutOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.impl.Utils.urlEncode;
import static io.vertx.ext.consul.v1.ConsulRequester.INDEX_HEADER;

@SuppressWarnings("ALL")
public class KVImpl implements KV {

  private static final List<Integer> KV_VALID_CODES = Arrays.asList(HttpResponseStatus.OK.code(), HttpResponseStatus.NOT_FOUND.code());
  private final static String V1_KV = "/v1/kv/";

  private ConsulRequester requester;

  public KVImpl(ConsulRequester requester) {
    this.requester = requester;
  }

  @Override
  public KV get(String key, Handler<AsyncResult<KeyValueList>> resultHandler) {
    return get(key, new KvGetOptions(), resultHandler);
  }

  @Override
  public KV get(String key, KvGetOptions kvGetOptions, Handler<AsyncResult<KeyValueList>> resultHandler) {
    Query query = keyGetQuery(kvGetOptions);
    requester.request(KV_VALID_CODES, HttpMethod.GET, V1_KV + urlEncode(key), query, null, resultHandler, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new KeyValueList();
      } else {
        List<KeyValue> list = resp.bodyAsJsonArray().stream().map(obj -> KVParser.parse((JsonObject) obj)).collect(Collectors.toList());
        return new KeyValueList().setList(list).setIndex(Long.parseLong(resp.headers().get(INDEX_HEADER)));
      }
    });
    return this;
  }

  @Override
  public KV keys(String key, Handler<AsyncResult<List<String>>> resultHandler) {
    return keys(key, resultHandler);
  }

  @Override
  public KV keys(String key, KvGetOptions kvGetOptions, Handler<AsyncResult<List<String>>> resultHandler) {
    Query query = keyGetQuery(kvGetOptions).put("keys", true);
    requester.request(KV_VALID_CODES, HttpMethod.GET, V1_KV + urlEncode(key), query, null, resultHandler, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return new ArrayList<>();
      } else {
        return resp.bodyAsJsonArray().stream().map(Object::toString).collect(Collectors.toList());
      }
    });
    return this;
  }

  @Override
  public KV raw(String key, Handler<AsyncResult<Buffer>> resultHandler) {
    return raw(key, new KvGetOptions(), resultHandler);
  }

  @Override
  public KV raw(String key, KvGetOptions options, Handler<AsyncResult<Buffer>> resultHandler) {
    Query query = keyGetQuery(options).put("raw", true);
    requester.request(KV_VALID_CODES, HttpMethod.GET, V1_KV + urlEncode(key), query, null, resultHandler, resp -> {
      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
        return Buffer.buffer();
      } else {
        return resp.body();
      }
    });
    return this;
  }


  private Query keyGetQuery(KvGetOptions kvGetOptions) {
    Query query = new Query();
    if (kvGetOptions != null) {
      if (kvGetOptions.isRecurse()) {
        query.put("recurse", true);
      }
      BlockingQueryOptions blockingQueryOptions = kvGetOptions.getBlockingQueryOptions();
      if (blockingQueryOptions != null) {
        query.put(blockingQueryOptions);
      }
      ConsistencyMode consistencyMode = kvGetOptions.getConsistencyMode();
      if (consistencyMode != null) {
        query.put(consistencyMode);
      }
      String dc = kvGetOptions.getDc();
      if (dc != null) {
        query.put("dc", dc);
      }
      String separator = kvGetOptions.getSeparator();
      if (separator != null) {
        query.put("separator", separator);
      }
    }
    return query;
  }

  @Override
  public KV put(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) {
    return put(key, value, new KvPutOptions(), resultHandler);
  }

  @Override
  public KV put(String key, String value, KvPutOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
    Query query = kvPutQuery(options);
    requester.requestString(HttpMethod.PUT, V1_KV + urlEncode(key), query, value, resultHandler,
      (bool, headers) -> Boolean.valueOf(bool));
    return this;
  }

  private Query kvPutQuery(KvPutOptions options) {
    Query query = new Query();
    if (options != null) {
      String dc = options.getDc();
      if (dc != null) {
        query.put("dc", dc);
      }
      int flags = options.getFlags();
      if (flags != 0) {
        query.put("flags", Long.toUnsignedString(flags));
      }
      int cas = options.getCas();
      if (cas != 0) {
        query.put("cas", cas);
      }
      String acquire = options.getAcquire();
      if (acquire != null) {
        query.put("acquire", acquire);
      }
      String release = options.getRelease();
      if (release != null) {
        query.put("release", release);
      }
    }
    return query;
  }

  @Override
  public KV delete(String key, Handler<AsyncResult<Boolean>> resultHandler) {
    return delete(key, new KvDeleteOptions(), resultHandler);
  }

  @Override
  public KV delete(String key, KvDeleteOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
    Query query = kvDeleteQuery(options);
    requester.requestString(HttpMethod.DELETE, V1_KV + urlEncode(key), query, null, resultHandler,
      (bool, headers) -> Boolean.valueOf(bool));
    return this;
  }

  private Query kvDeleteQuery(KvDeleteOptions options) {
    Query query = new Query();
    if (options != null) {
      int cas = options.getCas();
      if (cas != 0) {
        query.put("cas", cas);
      }
      if (options.isRecurse()) {
        query.put("recurse", true);
      }
    }
    return query;
  }
}
