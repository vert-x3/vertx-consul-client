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
import io.vertx.ext.consul.Service;

import java.util.Map;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.impl.Utils.listOf;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class ServiceParser {

  private static final String AGENT_SERVICE_ID = "ID";
  private static final String AGENT_SERVICE_SERVICE = "Service";
  private static final String AGENT_SERVICE_TAGS = "Tags";
  private static final String AGENT_SERVICE_ADDRESS = "Address";
  private static final String AGENT_SERVICE_PORT = "Port";

  static Service parseAgentInfo(JsonObject jsonObject) {
    JsonArray tagsArr = jsonObject.getJsonArray(AGENT_SERVICE_TAGS);
    return new Service()
      .setId(jsonObject.getString(AGENT_SERVICE_ID))
      .setName(jsonObject.getString(AGENT_SERVICE_SERVICE))
      .setTags(listOf(tagsArr))
      .setAddress(jsonObject.getString(AGENT_SERVICE_ADDRESS))
      .setPort(jsonObject.getInteger(AGENT_SERVICE_PORT));
  }

  static Service parseCatalogInfo(Map.Entry<String, Object> entry) {
    Object tags = entry.getValue();
    return new Service()
      .setName(entry.getKey())
      .setTags(((JsonArray) tags).stream().map(o -> (String) o).collect(Collectors.toList()));
  }

  static Service parseNodeInfo(String nodeName, String nodeAddress, JsonObject serviceInfo) {
    JsonArray tagsArr = serviceInfo.getJsonArray(AGENT_SERVICE_TAGS);
    return new Service()
      .setNode(nodeName)
      .setNodeAddress(nodeAddress)
      .setId(serviceInfo.getString(AGENT_SERVICE_ID))
      .setAddress(serviceInfo.getString(AGENT_SERVICE_ADDRESS))
      .setName(serviceInfo.getString(AGENT_SERVICE_SERVICE))
      .setTags(listOf(tagsArr))
      .setPort(serviceInfo.getInteger(AGENT_SERVICE_PORT));
  }

}
