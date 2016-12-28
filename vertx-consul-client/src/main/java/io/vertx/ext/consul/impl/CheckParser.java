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
import io.vertx.ext.consul.Check;
import io.vertx.ext.consul.CheckStatus;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class CheckParser {

  private static final String SERVICE_ID_KEY = "ServiceID";
  private static final String SERVICE_NAME_KEY = "ServiceName";
  private static final String ID_KEY = "CheckID";
  private static final String NAME_KEY = "Name";
  private static final String NODE_KEY = "Node";
  private static final String STATUS_KEY = "Status";
  private static final String NOTES_KEY = "Notes";
  private static final String OUTPUT_KEY = "Output";

  static Check parse(JsonObject check) {
    return new Check()
      .setId(check.getString(ID_KEY))
      .setName(check.getString(NAME_KEY))
      .setNodeName(check.getString(NODE_KEY))
      .setStatus(CheckStatus.of(check.getString(STATUS_KEY)))
      .setNotes(check.getString(NOTES_KEY))
      .setOutput(check.getString(OUTPUT_KEY))
      .setServiceId(check.getString(SERVICE_ID_KEY))
      .setServiceName(check.getString(SERVICE_NAME_KEY));
  }
}
