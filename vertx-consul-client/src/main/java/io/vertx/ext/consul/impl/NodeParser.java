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

import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.Node;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class NodeParser {

  private static final String NODE_KEY = "Node";
  private static final String ADDRESS_KEY = "Address";
  private static final String TAGGED_ADDRESSES_KEY = "TaggedAddresses";
  private static final String LAN_KEY = "lan";
  private static final String WAN_KEY = "wan";

  static Node parse(JsonObject json) {
    Node node = new Node()
      .setName(json.getString(NODE_KEY))
      .setAddress(json.getString(ADDRESS_KEY));
    JsonObject tagged = json.getJsonObject(TAGGED_ADDRESSES_KEY);
    if (tagged != null) {
      node.setLanAddress(tagged.getString(LAN_KEY)).setWanAddress(tagged.getString(WAN_KEY));
    }
    return node;
  }
}
