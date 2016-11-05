package io.vertx.ext.consul.impl;

import io.vertx.ext.consul.BlockingQueryOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class Query {

  private final Map<String, String> map = new HashMap<>();

  static Query of(String key, Object value) {
    return new Query().put(key, value);
  }

  Query put(String key, Object value) {
    if (value == null) {
      return this;
    }
    String str = value.toString();
    if (!str.isEmpty()) {
      map.put(key, str);
    }
    return this;
  }

  Query put(BlockingQueryOptions options) {
    if (options != null) {
      if (options.getIndex() > 0) {
        put("index", options.getIndex());
      }
      if (options.getWait() != null) {
        put("wait", options.getWait());
      }
    }
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (!map.isEmpty()) {
      sb.append('?');
    }
    map.entrySet().forEach(e -> {
      if (sb.length() > 1) {
        sb.append('&');
      }
      sb.append(e.getKey()).append('=').append(e.getValue());
    });
    return sb.toString();
  }
}
