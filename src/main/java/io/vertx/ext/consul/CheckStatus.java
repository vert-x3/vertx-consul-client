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
package io.vertx.ext.consul;

/**
 * Represents an check status.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/agent/checks.html">Consul checks documentation</a>
 */
public enum CheckStatus {

  PASSING("passing"),
  WARNING("warning"),
  CRITICAL("critical");

  public final String key;

  public static CheckStatus of(String key) {
    for (CheckStatus checkStatus : values()) {
      if (checkStatus.key.equals(key)) {
        return checkStatus;
      }
    }
    return null;
  }

  CheckStatus(String key) {
    this.key = key;
  }
}
