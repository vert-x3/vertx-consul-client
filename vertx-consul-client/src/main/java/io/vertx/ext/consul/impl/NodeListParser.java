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
import io.vertx.ext.consul.Node;
import io.vertx.ext.consul.NodeList;

import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class NodeListParser {

  private static final String INDEX_KEY = "Index";
  private static final String LIST_KEY = "List";

  static NodeList parse(JsonObject json) {
    return new NodeList()
      .setIndex(json.getLong(INDEX_KEY, 0L))
      .setList(json.getJsonArray(LIST_KEY, new JsonArray()).stream()
        .map(obj -> new Node((JsonObject) obj)).collect(Collectors.toList()));
  }
}
