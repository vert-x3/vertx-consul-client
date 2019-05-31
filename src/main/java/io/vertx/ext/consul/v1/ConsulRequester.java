package io.vertx.ext.consul.v1;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.impl.Query;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class ConsulRequester {

  public static final String INDEX_HEADER = "X-Consul-Index";
  public static final String TOKEN_HEADER = "X-Consul-Token";

  private static final List<Integer> DEFAULT_VALID_CODES = Collections.singletonList(HttpResponseStatus.OK.code());
  private static final List<Integer> TXN_VALID_CODES = Arrays.asList(HttpResponseStatus.OK.code(), HttpResponseStatus.CONFLICT.code());
  private final WebClient webClient;
  private final Context ctx;
  private final String aclToken;
  private final String dc;
  private final long timeoutMs;

  public ConsulRequester(Vertx vertx, ConsulClientOptions options) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(options);
    webClient = WebClient.create(vertx, options);
    ctx = vertx.getOrCreateContext();
    aclToken = options.getAclToken();
    dc = options.getDc();
    timeoutMs = options.getTimeout();
  }


  public  <T> void requestArray(HttpMethod method, String path, Query query, String body,
                                Handler<AsyncResult<T>> resultHandler,
                                BiFunction<JsonArray, MultiMap, T> mapper) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> mapper.apply(resp.bodyAsJsonArray(), resp.headers()));
  }

  public <T> void requestObject(HttpMethod method, String path, Query query, String body,
                                 Handler<AsyncResult<T>> resultHandler,
                                 BiFunction<JsonObject, MultiMap, T> mapper) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> mapper.apply(resp.bodyAsJsonObject(), resp.headers()));
  }

  public <T> void requestString(HttpMethod method, String path, Query query, String body,
                                 Handler<AsyncResult<T>> resultHandler,
                                 BiFunction<String, MultiMap, T> mapper) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> mapper.apply(resp.bodyAsString().trim(), resp.headers()));
  }

  public <T> void requestVoid(HttpMethod method, String path, Query query, String body,
                               Handler<AsyncResult<T>> resultHandler) {
    request(DEFAULT_VALID_CODES, method, path, query, body, resultHandler, resp -> null);
  }

  public <T> void request(List<Integer> validCodes, HttpMethod method, String path, Query query, String body,
                           Handler<AsyncResult<T>> resultHandler,
                           Function<HttpResponse<Buffer>, T> mapper) {
    if (Vertx.currentContext() == ctx) {
      reqOnContext(validCodes, method, path, query, body, resultHandler, mapper);
    } else {
      ctx.runOnContext(v -> reqOnContext(validCodes, method, path, query, body, resultHandler, mapper));
    }
  }

  public <T> void reqOnContext(List<Integer> validCodes, HttpMethod method, String path, io.vertx.ext.consul.impl.Query query, String body,
                                Handler<AsyncResult<T>> resultHandler,
                                Function<HttpResponse<Buffer>, T> mapper) {
    if (query == null) {
      query = new io.vertx.ext.consul.impl.Query();
    }
    if (dc != null) {
      query.put("dc", dc);
    }
    HttpRequest<Buffer> rq = webClient.request(method, path);
    query.entrySet().forEach(e -> rq.addQueryParam(e.getKey(), e.getValue()));
    if (aclToken != null) {
      rq.putHeader(TOKEN_HEADER, aclToken);
    }
    if (timeoutMs > 0) {
      rq.timeout(timeoutMs);
    }
    rq.sendBuffer(body == null ? Buffer.buffer() : Buffer.buffer(body), h -> {
      if (h.succeeded()) {
        HttpResponse<Buffer> resp = h.result();
        if (validCodes.contains(resp.statusCode())) {
          T mapped;
          try {
            mapped = mapper.apply(resp);
          } catch (Throwable throwable) {
            resultHandler.handle(Future.failedFuture(throwable));
            return;
          }
          resultHandler.handle(Future.succeededFuture(mapped));
        } else {
          resultHandler.handle(Future.failedFuture(resp.statusMessage()));
        }
      } else {
        resultHandler.handle(Future.failedFuture(h.cause()));
      }
    });
  }
}
