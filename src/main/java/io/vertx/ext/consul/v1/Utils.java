package io.vertx.ext.consul.v1;

import io.vertx.ext.web.client.HttpRequest;

import java.util.Map;

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
  }
}
