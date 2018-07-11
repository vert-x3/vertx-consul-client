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

import io.vertx.core.json.JsonArray;
import io.vertx.ext.consul.CheckStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Utils {

  public static CheckStatus aggregateCheckStatus(List<CheckStatus> checks) {
    boolean warning = false, critical = false;
    for (CheckStatus status: checks) {
      switch (status) {
        case WARNING:
          warning = true;
          break;
        case CRITICAL:
          critical = true;
          break;
      }
    }
    if (critical) {
      return CheckStatus.CRITICAL;
    } else  if (warning) {
      return CheckStatus.WARNING;
    } else {
      return CheckStatus.PASSING;
    }
  }

  public static String urlEncode(String str) {
    try {
      return URLEncoder.encode(str, "UTF-8");
    } catch (UnsupportedEncodingException ignore) {
    }
    return "";
  }

  public static String encode64(String src) {
    if (src == null || src.isEmpty()) {
      return "";
    } else {
      return new String(Base64.getEncoder().encode(src.getBytes()));
    }
  }

  public static String decode64(String src) {
    if (src == null || src.isEmpty()) {
      return "";
    } else {
      return new String(Base64.getDecoder().decode(src));
    }
  }

  public static <T> List<T> listOf(List<T> list) {
    if (list == null) {
      return null;
    } else {
      return new ArrayList<>(list);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> listOf(JsonArray arr) {
    if (arr == null) {
      return null;
    } else {
      return (List<T>) arr.getList();
    }
  }
}
