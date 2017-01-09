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

import java.util.List;

/**
 * Holds coordinates of servers in datacenter
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class DcCoordinates {

  private String dc;
  private List<Coordinate> servers;

  /**
   * Default constructor
   */
  public DcCoordinates() {}

  /**
   * Constructor from JSON
   *
   * @param coords the JSON
   */
  public DcCoordinates(JsonObject coords) {
    DcCoordinatesConverter.fromJson(coords, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    DcCoordinatesConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get datacenter
   *
   * @return datacenter
   */
  public String getDatacenter() {
    return dc;
  }

  /**
   * Get list of servers in datacenter
   *
   * @return list of servers in datacenter
   */
  public List<Coordinate> getServers() {
    return servers;
  }

  /**
   * Set datacenter
   *
   * @param dc the datacenter
   * @return reference to this, for fluency
   */
  public DcCoordinates setDatacenter(String dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Set list of servers in datacenter
   *
   * @param servers list of servers in datacenter
   * @return reference to this, for fluency
   */
  public DcCoordinates setServers(List<Coordinate> servers) {
    this.servers = servers;
    return this;
  }
}
