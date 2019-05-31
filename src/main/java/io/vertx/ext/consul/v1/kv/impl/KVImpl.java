package io.vertx.ext.consul.v1.kv.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.BlockingQueryOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.KeyValueList;
import io.vertx.ext.consul.impl.KVParser;
import io.vertx.ext.consul.impl.Query;
import io.vertx.ext.consul.impl.Utils;
import io.vertx.ext.consul.v1.ConsistencyMode;
import io.vertx.ext.consul.v1.ConsulRequester;
import io.vertx.ext.consul.v1.kv.KV;
import io.vertx.ext.consul.v1.kv.KvDeleteOptions;
import io.vertx.ext.consul.v1.kv.KvGetOptions;
import io.vertx.ext.consul.v1.kv.KvPutOptions;
import io.vertx.ext.web.client.WebClient;

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

  private Query keyGetQuery(KvGetOptions kvGetOptions) {
    Query query = new Query();
    query.put("recurse", kvGetOptions.isRecurse());
    BlockingQueryOptions blockingQueryOptions = kvGetOptions.getBlockingQueryOptions();
    if (blockingQueryOptions != null) {
      query.put(blockingQueryOptions);
    }
    ConsistencyMode consistencyMode = kvGetOptions.getConsistencyMode();
    if (consistencyMode != null) {
      query.put(consistencyMode);
    }
    return query;
  }

  @Override
  public KV put(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) {
    return put(key, value, null, resultHandler);
  }

  @Override
  public KV put(String key, String value, KvPutOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
    requester.requestString(HttpMethod.PUT, "/v1/kv/" + urlEncode(key), new Query(), value, resultHandler,
      (bool, headers) -> Boolean.valueOf(bool));
    return this;
  }

  @Override
  public KV delete(String key, Handler<AsyncResult<Boolean>> resultHandler) {
    return delete(key, new KvDeleteOptions(), resultHandler);
  }

  @Override
  public KV delete(String key, KvDeleteOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
    requester.requestVoid(HttpMethod.DELETE, "/v1/kv/" + urlEncode(key), null, null, resultHandler);
    return this;
  }
}
