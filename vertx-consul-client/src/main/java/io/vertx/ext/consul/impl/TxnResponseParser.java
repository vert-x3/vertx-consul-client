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
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.TxnError;
import io.vertx.ext.consul.TxnResponse;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class TxnResponseParser {

  static TxnResponse parse(JsonObject json) {
    TxnResponse response = new TxnResponse();
    if (json.getValue("Results") instanceof JsonArray) {
      json.getJsonArray("Results").forEach(entry -> {
        JsonObject obj = (JsonObject) entry;
        if (obj.containsKey("KV")) {
          response.addResult(KVParser.parse(obj.getJsonObject("KV")));
        }
      });
    }
    if (json.getValue("Errors") instanceof JsonArray) {
      json.getJsonArray("Errors").forEach(entry -> {
        JsonObject obj = (JsonObject) entry;
        response.addError(new TxnError()
          .setOpIndex(obj.getInteger("OpIndex"))
          .setWhat(obj.getString("What")));
      });
    }
    return response;
  }
}
