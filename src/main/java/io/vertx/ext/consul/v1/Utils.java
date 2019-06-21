package io.vertx.ext.consul.v1;

import io.vertx.core.MultiMap;
import io.vertx.ext.consul.v1.kv.QueryMeta;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
  public static <T> void setQueryOptions(HttpRequest<T> request, QueryOptions options) {
    if (options == null) {
      return;
    }
    String datacenter = options.getDatacenter();
    if (datacenter != null) {
      request.addQueryParam("dc", datacenter);
    }
    if (options.isAllowStale()) {
      request.addQueryParam("stale", "");
    }
    if (options.isRequireConsistent()) {
      request.addQueryParam("consistent", "");
    }
    long waitIndex = options.getWaitIndex();
    if (waitIndex != 0) {
      request.addQueryParam("index", Long.toUnsignedString(waitIndex));
    }
    long waitTimeMillis = options.getWaitTimeMillis();
    if (waitTimeMillis != 0) {
      request.addQueryParam("wait", String.format("%dms", waitIndex));
    }
    String waitHash = options.getWaitHash();
    if (waitHash != null) {
      request.addQueryParam("hash", waitHash);
    }
    String token = options.getToken();
    if (token != null) {
      request.putHeader("X-Consul-Token", token);
    }
    String near = options.getNear();
    if (near != null) {
      request.putHeader("near", near);
    }
    String filter = options.getFilter();
    if (filter != null) {
      request.addQueryParam("filter", filter);
    }
    Map<String, String> nodeMeta = options.getNodeMeta();
    if (nodeMeta != null && nodeMeta.size() > 0) {
      for (Map.Entry<String, String> entry : nodeMeta.entrySet()) {
        request.addQueryParam("node-meta", entry.getKey() + ":" + entry.getValue());
      }
    }
    byte relayFactor = options.getRelayFactor();
    if (relayFactor != 0) {
      request.addQueryParam("relay-factor", Long.toUnsignedString(Byte.toUnsignedLong(relayFactor)));
    }
    if (options.isConnect()) {
      request.addQueryParam("connect", Boolean.toString(true));
    }
    if (options.isUseCache() && !options.isRequireConsistent()) {
      request.setQueryParam("cached", "");
    }
    ArrayList<String> cc = new ArrayList<>();
    if (options.getMaxAgeSeconds() > 0) {
      cc.add(String.format("max-age=%d", options.getMaxAgeSeconds()));
    }
    if (options.getStaleIfErrorSeconds() > 0) {
      cc.add(String.format("stale-if-error=%d", options.getStaleIfErrorSeconds()));
    }
    if (cc.size() > 0) {
      request.headers().set("Cache-Control", cc.stream().collect(Collectors.joining(", ")));
    }
  }

  public static <T> void parseQueryMeta(HttpResponse<T> response, QueryMeta queryMeta) {
    MultiMap headers = response.headers();
    String indexStr = headers.get("X-Consul-Index");
    if (indexStr != null && !indexStr.equals("")) {
      long index = Long.parseUnsignedLong(indexStr, 10);
      queryMeta.setLastIndex(index);
    }
    queryMeta.setLastContentHash(headers.get("X-Consul-ContentHash"));
    long last = Long.parseUnsignedLong(headers.get("X-Consul-LastContact"), 10);
    queryMeta.setLastContactMillis(last);
    if (headers.get("X-Consul-KnownLeader").equals("true")) {
      queryMeta.setKnownLeader(true);
    } else {
      queryMeta.setKnownLeader(false);
    }
    if (headers.get("X-Consul-Translate-Addresses").equals("true")) {
      queryMeta.setAddressTranslationEnabled(true);
    } else {
      queryMeta.setAddressTranslationEnabled(false);
    }
    String cacheStr = headers.get("X-Cache");
    if (cacheStr != null && !cacheStr.equals("")) {
      queryMeta.setCacheHit(cacheStr.equalsIgnoreCase("HIT"));
    }
    String ageStr = headers.get("Age");
    if (ageStr != null && !ageStr.equals("")) {
      long age = Long.parseUnsignedLong(ageStr, 10);
      queryMeta.setCacheAgeSeconds(age);
    }
  }
}
