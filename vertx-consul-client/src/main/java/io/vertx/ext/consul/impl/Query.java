/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul.impl;

import io.vertx.ext.consul.BlockingQueryOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class Query {

  private final Map<String, String> map = new HashMap<>();

  static Query of(BlockingQueryOptions options) {
    return new Query().put(options);
  }

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
      put("index", Long.toUnsignedString(options.getIndex()));
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
      sb.append(e.getKey()).append('=').append(Utils.urlEncode(e.getValue()));
    });
    return sb.toString();
  }
}
