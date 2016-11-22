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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Holds properties of Consul event
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Event {

  private static final String ID_KEY = "ID";
  private static final String NAME_KEY = "Name";
  private static final String PAYLOAD_KEY = "Payload";
  private static final String NODE_FILTER_KEY = "NodeFilter";
  private static final String SERVICE_FILTER_KEY = "ServiceFilter";
  private static final String TAG_FILTER_KEY = "TagFilter";
  private static final String VERSION_KEY = "Version";
  private static final String LTIME_KEY = "LTime";

  private String id;
  private String name;
  private String payload;
  private String node;
  private String service;
  private String tag;
  private int version;
  private int lTime;

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public Event(JsonObject json) {
    this.id = json.getString(ID_KEY);
    this.name = json.getString(NAME_KEY);
    this.payload = json.getString(PAYLOAD_KEY);
    this.node = json.getString(NODE_FILTER_KEY);
    this.service = json.getString(SERVICE_FILTER_KEY);
    this.tag = json.getString(TAG_FILTER_KEY);
    this.version = json.getInteger(VERSION_KEY, 0);
    this.lTime = json.getInteger(LTIME_KEY, 0);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (id != null) {
      jsonObject.put(ID_KEY, id);
    }
    if (name != null) {
      jsonObject.put(NAME_KEY, name);
    }
    if (payload != null) {
      jsonObject.put(PAYLOAD_KEY, payload);
    }
    if (node != null) {
      jsonObject.put(NODE_FILTER_KEY, node);
    }
    if (service != null) {
      jsonObject.put(SERVICE_FILTER_KEY, service);
    }
    if (tag != null) {
      jsonObject.put(TAG_FILTER_KEY, tag);
    }
    if (version != 0) {
      jsonObject.put(VERSION_KEY, version);
    }
    if (lTime != 0) {
      jsonObject.put(LTIME_KEY, lTime);
    }
    return jsonObject;
  }

  /**
   * Get ID of event
   *
   * @return ID of event
   */
  public String getId() {
    return id;
  }

  /**
   * Get name of event
   *
   * @return event name
   */
  public String getName() {
    return name;
  }

  /**
   * Get payload of event
   *
   * @return payload
   */
  public String getPayload() {
    return payload;
  }

  /**
   * Get regular expression to filter by node name
   *
   * @return regular expression to filter by node name
   */
  public String getNode() {
    return node;
  }

  /**
   * Get regular expression to filter by service
   *
   * @return regular expression to filter by service
   */
  public String getService() {
    return service;
  }

  /**
   * Get regular expression to filter by tag
   *
   * @return regular expression to filter by tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * Get version
   *
   * @return version
   */
  public int getVersion() {
    return version;
  }

  /**
   * Get Lamport clock time
   *
   * @return Lamport clock time
   */
  public int getlTime() {
    return lTime;
  }
}
