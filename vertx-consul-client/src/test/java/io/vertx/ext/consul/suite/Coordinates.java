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
package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Coordinate;
import io.vertx.ext.consul.DcCoordinates;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.sleep;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Coordinates extends ConsulTestBase {

  private static final int MAX_REQUESTS = 100;

  @Test
  public void nodes() {
    List<Coordinate> nodes = null;
    int requests = MAX_REQUESTS;
    while (requests --> 0) {
      nodes = getAsync(h -> readClient.coordinateNodes(h));
      if (nodes.size() > 0) {
        break;
      }
      sleep(vertx, 1000);
      System.out.println("waiting for node coordinates...");
    }
    if(nodes == null || nodes.size() == 0) {
      fail();
      return;
    }
    Coordinate coordinate = nodes.get(0);
    assertEquals(coordinate.getNode(), nodeName);
  }

  @Test
  public void datacenters() {
    List<DcCoordinates> datacenters = null;
    int requests = MAX_REQUESTS;
    while (requests --> 0) {
      datacenters = getAsync(h -> readClient.coordinateDatacenters(h));
      if (datacenters.size() > 0) {
        break;
      }
      sleep(vertx, 1000);
      System.out.println("waiting for datacenter coordinates...");
    }
    if(datacenters == null || datacenters.size() == 0) {
      fail();
      return;
    }
    DcCoordinates coordinate = datacenters.get(0);
    assertEquals(coordinate.getDatacenter(), dc);
  }

}
