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
import io.vertx.ext.consul.Coordinate;
import io.vertx.ext.consul.DcCoordinates;

import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class CoordinateParser {

  private static final String NODE_KEY = "Node";
  private static final String COORD_KEY = "Coord";
  private static final String ADJ_KEY = "Adjustment";
  private static final String ERR_KEY = "Error";
  private static final String HEIGHT_KEY = "Height";
  private static final String VEC_KEY = "Vec";

  private static final String DATACENTER_KEY = "Datacenter";
  private static final String COORDS_KEY = "Coordinates";

  static Coordinate parse(JsonObject json) {
    Coordinate coordinate = new Coordinate()
      .setNode(json.getString(NODE_KEY));
    JsonObject coord = json.getJsonObject(COORD_KEY);
    if (coord != null) {
      coordinate
        .setAdj(coord.getFloat(ADJ_KEY, 0f))
        .setErr(coord.getFloat(ERR_KEY, 0f))
        .setHeight(coord.getFloat(HEIGHT_KEY, 0f));
      JsonArray arr = coord.getJsonArray(VEC_KEY);
      coordinate.setVec(arr == null ? null : arr.stream()
        .map(o -> o instanceof Number ? ((Number) o).floatValue() : 0f)
        .collect(Collectors.toList()));
    }
    return coordinate;
  }

  static DcCoordinates parseDc(JsonObject json) {
    return new DcCoordinates()
      .setDatacenter(json.getString(DATACENTER_KEY))
      .setServers(json.getJsonArray(COORDS_KEY).stream()
        .map(obj -> parse((JsonObject) obj))
        .collect(Collectors.toList()));
  }
}
