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
import io.vertx.ext.consul.Event;

import java.util.Base64;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class EventParser {

  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  private static final String PAYLOAD_KEY = "Payload";
  private static final String NODE_FILTER_KEY = "NodeFilter";
  private static final String SERVICE_FILTER_KEY = "ServiceFilter";
  private static final String TAG_FILTER_KEY = "TagFilter";
  private static final String VERSION_KEY = "Version";
  private static final String LTIME_KEY = "LTime";

  static Event parse(JsonObject json) {
    Event ev = new Event()
      .setId(json.getString(ID_KEY))
      .setName(json.getString(NAME_KEY))
      .setNode(json.getString(NODE_FILTER_KEY))
      .setService(json.getString(SERVICE_FILTER_KEY))
      .setTag(json.getString(TAG_FILTER_KEY))
      .setVersion(json.getInteger(VERSION_KEY, 0))
      .setLTime(json.getInteger(LTIME_KEY, 0));
    String payload = json.getString(PAYLOAD_KEY);
    if (payload != null) {
      ev.setPayload(Utils.decode64(payload));
    }
    return ev;
  }
}
