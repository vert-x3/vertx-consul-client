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
import io.vertx.ext.consul.ServiceEntry;

import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class ServiceEntryParser {

  private static final String NODE_KEY = "Node";
  private static final String SERVICE_KEY = "Service";
  private static final String CHECKS_KEY = "Checks";

  static ServiceEntry parse(JsonObject json) {
    return new ServiceEntry()
      .setNode(NodeParser.parse(json.getJsonObject(NODE_KEY)))
      .setService(ServiceParser.parseAgentInfo(json.getJsonObject(SERVICE_KEY)))
      .setChecks(json.getJsonArray(CHECKS_KEY).stream().map(obj -> CheckParser.parse((JsonObject) obj)).collect(Collectors.toList()));
  }
}
