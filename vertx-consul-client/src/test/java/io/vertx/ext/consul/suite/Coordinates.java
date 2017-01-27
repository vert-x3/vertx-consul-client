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

import com.pszymczyk.consul.ConsulProcess;
import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.sleep;

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
      nodes1 = getAsync(h -> readClient.coordinateNodes(h));
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
    assertEquals(coordinate.getNode(), nodeName);

    BlockingQueryOptions blockingQueryOptions1 = new BlockingQueryOptions().setIndex(nodes1.getIndex());
    CountDownLatch latch1 = new CountDownLatch(1);
    readClient.coordinateNodesWithOptions(blockingQueryOptions1, h -> {
      // with first update we can obtain changed coordinates only, without second node
      assertTrue(h.result().getIndex() > blockingQueryOptions1.getIndex());
      latch1.countDown();
    });
    sleep(vertx, 2000);
    assertEquals(latch1.getCount(), 1);
    ConsulProcess attached = attachConsul("new_node");
    latch1.await(2, TimeUnit.MINUTES);
    assertEquals(latch1.getCount(), 0);

    // wait until the second consul will attached
    assertTrue(waitPeers(2));

    attached.close();

    // wait until the second consul will leave
    assertTrue(waitPeers(1));
  }

  private boolean waitPeers(int expected) {
    int requests = MAX_REQUESTS;
    while (requests --> 0) {
      List<String> peers = getAsync(h -> readClient.peersStatus(h));
      if (peers.size() == expected) {
        System.out.println("Number of peers: " + expected);
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
