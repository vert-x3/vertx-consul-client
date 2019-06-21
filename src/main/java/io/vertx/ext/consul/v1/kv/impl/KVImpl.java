package io.vertx.ext.consul.v1.kv.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.consul.v1.QueryOptions;
import io.vertx.ext.consul.v1.Utils;
import io.vertx.ext.consul.v1.WriteOptions;
import io.vertx.ext.consul.v1.kv.*;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.impl.Utils.urlEncode;

public class KVImpl implements KV {

  private final static String V1_KV = "/v1/kv/";

  private WebClient webClient;

  public KVImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public KV get(String key, QueryOptions options, Handler<AsyncResult<QueryMetaWith<KVPair>>> handler) {
    Promise<QueryMetaWith<Buffer>> promise = Promise.promise();
    getInternal(key, new HashMap<>(0), options, promise);
    promise.future().<QueryMetaWith<KVPair>>map(res -> {
      if (res == null) {
        return new QueryMetaWithImpl<>(res.queryMeta(), null);
      } else {
        KVPairImpl kvPair = res.with().toJsonObject().mapTo(KVPairImpl.class);
        return new QueryMetaWithImpl<>(res.queryMeta(), kvPair);
      }
    }).setHandler(handler);
    return this;
  }

  private void getInternal(String key,
                               Map<String, String> params,
                               QueryOptions options,
                               Handler<AsyncResult<QueryMetaWith<Buffer>>> handler) {
    HttpRequest<Buffer> r = webClient.get(V1_KV + urlEncode(key));
    Utils.setQueryOptions(r, options);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      r.setQueryParam(entry.getKey(), entry.getValue());
    }
    Promise<HttpResponse<Buffer>> promise = Promise.promise();
    long tick = System.nanoTime();
    r.send(promise);
    promise.future().setHandler(result -> {
      if (result.succeeded()) {
        HttpResponse<Buffer> response = result.result();
        QueryMeta queryMeta = new QueryMetaImpl();
        Utils.parseQueryMeta(response, queryMeta);
        long tok = System.nanoTime();
        long requestTime = tok - tick;
        long requestTimeMillis = TimeUnit.NANOSECONDS.toMillis(requestTime);
        queryMeta.setRequestTimeMillis(requestTimeMillis);
        int statusCode = response.statusCode();
        if (statusCode == 404) {
          handler.handle(Future.succeededFuture(new QueryMetaWithImpl<>(queryMeta, null)));
        } else if (statusCode != 200) {
          handler.handle(Future.failedFuture(String.format("Unexpected response code: %d", statusCode)));
        } else {
          handler.handle(Future.succeededFuture(new QueryMetaWithImpl<>(queryMeta, response.body())));
        }
      } else {
        handler.handle(Future.failedFuture(result.cause()));
      }
    });
  }

  @Override
  public KV acquire(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler) {
    return null;
  }

  @Override
  public KV cas(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler) {
    return null;
  }

  @Override
  public KV delete(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Void>>> handler) {
    return null;
  }

  @Override
  public KV deleteCas(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler) {
    return null;
  }

  @Override
  public KV deleteTree(String prefix, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Void>>> handler) {
    return null;
  }

  @Override
  public KV keys(String prefix, String separator, QueryOptions options, Handler<AsyncResult<QueryMetaWithList<String>>> handler) {
    return null;
  }

  @Override
  public KV keys(String prefix, QueryOptions options, Handler<AsyncResult<QueryMetaWithList<KVPair>>> handler) {
    return null;
  }

  @Override
  public KV put(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Void>>> handler) {
    return null;
  }

  @Override
  public KV release(KVPair kvPair, WriteOptions writeOptions, Handler<AsyncResult<QueryMetaWith<Boolean>>> handler) {
    return null;
  }


//  @Override
//  public KV get(String key, Handler<AsyncResult<KeyValueList>> resultHandler) {
//    return get(key, new KvGetOptions(), resultHandler);
//  }
//
//  @Override
//  public KV get(String key, KvGetOptions kvGetOptions, Handler<AsyncResult<KeyValueList>> resultHandler) {
//    Query query = keyGetQuery(kvGetOptions);
//    requester.request(KV_VALID_CODES, HttpMethod.GET, V1_KV + urlEncode(key), query, null, resultHandler, resp -> {
//      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
//        return new KeyValueList();
//      } else {
//        List<KeyValue> list = resp.bodyAsJsonArray().stream().map(obj -> KVParser.parse((JsonObject) obj)).collect(Collectors.toList());
//        return new KeyValueList().setList(list).setIndex(Long.parseLong(resp.headers().get(INDEX_HEADER)));
//      }
//    });
//    return this;
//  }
//
//  @Override
//  public KV keys(String key, Handler<AsyncResult<List<String>>> resultHandler) {
//    return keys(key, resultHandler);
//  }
//
//  @Override
//  public KV keys(String key, KvGetOptions kvGetOptions, Handler<AsyncResult<List<String>>> resultHandler) {
//    Query query = keyGetQuery(kvGetOptions).put("keys", true);
//    requester.request(KV_VALID_CODES, HttpMethod.GET, V1_KV + urlEncode(key), query, null, resultHandler, resp -> {
//      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
//        return new ArrayList<>();
//      } else {
//        return resp.bodyAsJsonArray().stream().map(Object::toString).collect(Collectors.toList());
//      }
//    });
//    return this;
//  }
//
//  @Override
//  public KV raw(String key, Handler<AsyncResult<Buffer>> resultHandler) {
//    return raw(key, new KvGetOptions(), resultHandler);
//  }
//
//  @Override
//  public KV raw(String key, KvGetOptions options, Handler<AsyncResult<Buffer>> resultHandler) {
//    Query query = keyGetQuery(options).put("raw", true);
//    requester.request(KV_VALID_CODES, HttpMethod.GET, V1_KV + urlEncode(key), query, null, resultHandler, resp -> {
//      if (resp.statusCode() == HttpResponseStatus.NOT_FOUND.code()) {
//        return Buffer.buffer();
//      } else {
//        return resp.body();
//      }
//    });
//    return this;
//  }
//
//
//  private Query keyGetQuery(KvGetOptions kvGetOptions) {
//    Query query = new Query();
//    if (kvGetOptions != null) {
//      if (kvGetOptions.isRecurse()) {
//        query.put("recurse", true);
//      }
//      BlockingQueryOptions blockingQueryOptions = kvGetOptions.getBlockingQueryOptions();
//      if (blockingQueryOptions != null) {
//        query.put(blockingQueryOptions);
//      }
//      ConsistencyMode consistencyMode = kvGetOptions.getConsistencyMode();
//      if (consistencyMode != null) {
//        query.put(consistencyMode);
//      }
//      String dc = kvGetOptions.getDc();
//      if (dc != null) {
//        query.put("dc", dc);
//      }
//      String separator = kvGetOptions.getSeparator();
//      if (separator != null) {
//        query.put("separator", separator);
//      }
//    }
//    return query;
//  }
//
//  @Override
//  public KV put(String key, String value, Handler<AsyncResult<Boolean>> resultHandler) {
//    return put(key, value, new KvPutOptions(), resultHandler);
//  }
//
//  @Override
//  public KV put(String key, String value, KvPutOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
//    Query query = kvPutQuery(options);
//    requester.requestString(HttpMethod.PUT, V1_KV + urlEncode(key), query, value, resultHandler,
//      (bool, headers) -> Boolean.valueOf(bool));
//    return this;
//  }
//
//  private Query kvPutQuery(KvPutOptions options) {
//    Query query = new Query();
//    if (options != null) {
//      String dc = options.getDc();
//      if (dc != null) {
//        query.put("dc", dc);
//      }
//      int flags = options.getFlags();
//      if (flags != 0) {
//        query.put("flags", Long.toUnsignedString(flags));
//      }
//      int cas = options.getCas();
//      if (cas != 0) {
//        query.put("cas", cas);
//      }
//      String acquire = options.getAcquire();
//      if (acquire != null) {
//        query.put("acquire", acquire);
//      }
//      String release = options.getRelease();
//      if (release != null) {
//        query.put("release", release);
//      }
//    }
//    return query;
//  }
//
//  @Override
//  public KV delete(String key, Handler<AsyncResult<Boolean>> resultHandler) {
//    return delete(key, new KvDeleteOptions(), resultHandler);
//  }
//
//  @Override
//  public KV delete(String key, KvDeleteOptions options, Handler<AsyncResult<Boolean>> resultHandler) {
//    Query query = kvDeleteQuery(options);
//    requester.requestString(HttpMethod.DELETE, V1_KV + urlEncode(key), query, null, resultHandler,
//      (bool, headers) -> Boolean.valueOf(bool));
//    return this;
//  }
//
//  private Query kvDeleteQuery(KvDeleteOptions options) {
//    Query query = new Query();
//    if (options != null) {
//      int cas = options.getCas();
//      if (cas != 0) {
//        query.put("cas", cas);
//      }
//      if (options.isRecurse()) {
//        query.put("recurse", true);
//      }
//    }
//    return query;
//  }
}
