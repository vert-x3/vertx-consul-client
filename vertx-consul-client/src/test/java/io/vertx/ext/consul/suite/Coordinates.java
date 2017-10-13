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

import io.vertx.ext.consul.*;
import io.vertx.ext.consul.dc.ConsulAgent;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Coordinates extends ConsulTestBase {

  private static final int MAX_REQUESTS = 100;

  @Test
  public void nodes() throws InterruptedException {
    CoordinateList nodes1 = null;
    int requests = MAX_REQUESTS;
    while (requests --> 0) {
      nodes1 = getAsync(h -> ctx.readClient().coordinateNodes(h));
      if (nodes1.getList().size() == 1) {
        break;
      }
      sleep(vertx, 1000);
      System.out.println("waiting for node coordinates...");
    }
    if(nodes1 == null || nodes1.getList().size() != 1) {
      fail();
      return;
    }
    Coordinate coordinate = nodes1.getList().get(0);
    assertEquals(coordinate.getNode(), ctx.nodeName());

    CountDownLatch latch = new CountDownLatch(1);

    waitBlockingQuery(latch, 10, nodes1.getIndex(), (idx, fut) -> {
      BlockingQueryOptions opts = new BlockingQueryOptions().setIndex(idx);
      ctx.readClient().coordinateNodesWithOptions(opts, h -> {
        boolean success = h.result().getList().size() == 2;
        waitComplete(vertx, fut, h.result().getIndex(), success);
      });
    });

    sleep(vertx, 2000);
    ConsulAgent attached = ctx.attachAgent("new_node");
    latch.await(2, TimeUnit.MINUTES);
    assertEquals(latch.getCount(), 0);

    ctx.detachAgent(attached);

    // wait until the second consul will leave
    assertTrue(waitPeers());
  }

  private boolean waitPeers() {
    int requests = MAX_REQUESTS;
    while (requests --> 0) {
      List<String> peers = getAsync(h -> ctx.readClient().peersStatus(h));
      if (peers.size() == 1) {
        return true;
      }
      sleep(vertx, 1000);
      System.out.println("waiting for peers...");
    }
    return false;
  }

  @Test
  public void datacenters() {
    List<DcCoordinates> datacenters = null;
    int requests = MAX_REQUESTS;
    while (requests --> 0) {
      datacenters = getAsync(h -> ctx.readClient().coordinateDatacenters(h));
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
    assertEquals(coordinate.getDatacenter(), ctx.dc().getName());
  }

}
