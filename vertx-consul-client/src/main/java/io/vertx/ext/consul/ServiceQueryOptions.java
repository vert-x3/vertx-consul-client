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
 * Options used to requesting list of services
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class ServiceQueryOptions {

  private static final String NEAR_KEY = "Near";
  private static final String TAG_KEY = "Tag";
  private static final String BLOCK_KEY = "Block";

  private String tag;
  private String near;
  private BlockingQueryOptions options;

  /**
   * Default constructor
   */
  public ServiceQueryOptions() {}

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public ServiceQueryOptions(JsonObject json) {
    tag = json.getString(TAG_KEY);
    near = json.getString(NEAR_KEY);
    options = json.containsKey(BLOCK_KEY) ? new BlockingQueryOptions(json.getJsonObject(BLOCK_KEY)) : null;
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (tag != null) {
      jsonObject.put(TAG_KEY, tag);
    }
    if (near != null) {
      jsonObject.put(NEAR_KEY, near);
    }
    if (options != null) {
      jsonObject.put(BLOCK_KEY, options.toJson());
    }
    return jsonObject;
  }

  /**
   * Get node name for sorting the list in ascending order based on the estimated round trip time from that node.
   *
   * @return the node name
   */
  public String getNear() {
    return near;
  }

  /**
   * Set node name for sorting the list in ascending order based on the estimated round trip time from that node.
   *
   * @param near the node name
   * @return reference to this, for fluency
   */
  public ServiceQueryOptions setNear(String near) {
    this.near = near;
    return this;
  }

  /**
   * Get tag for filtering request results
   *
   * @return the tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * Set tag for filtering request results
   *
   * @param tag the tag
   * @return reference to this, for fluency
   */
  public ServiceQueryOptions setTag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Get options for blocking query
   *
   * @return the blocking options
   */
  public BlockingQueryOptions getBlockingOptions() {
    return options;
  }

  /**
   * Set options for blocking query
   *
   * @param options the blocking options
   * @return reference to this, for fluency
   */
  public ServiceQueryOptions setBlockingOptions(BlockingQueryOptions options) {
    this.options = options;
    return this;
  }
}
