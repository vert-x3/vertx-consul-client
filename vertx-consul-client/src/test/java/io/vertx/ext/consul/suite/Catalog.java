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
import java.util.stream.Collectors;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.sleep;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Catalog extends ConsulTestBase {

  @Test
  public void datacenters() {
    List<String> datacenters = Utils.getAsync(h -> ctx.readClient().catalogDatacenters(h));
    assertEquals(datacenters.size(), 1);
    assertEquals(datacenters.get(0), ctx.dc().getName());
  }

  @Test
  public void nodes() {
    List<Node> nodes = Utils.<NodeList>getAsync(h -> ctx.readClient().catalogNodes(h)).getList();
    assertEquals(nodes.size(), 1);
    Node node = nodes.get(0);
    assertEquals(node.getName(), ctx.nodeName());
  }

  @Test
  public void blockingQuery() throws InterruptedException {
    NodeList nodes1 = getAsync(h -> ctx.readClient().catalogNodes(h));
    CountDownLatch latch1 = new CountDownLatch(1);
    BlockingQueryOptions blockingQueryOptions1 = new BlockingQueryOptions().setIndex(nodes1.getIndex());
    ctx.readClient().catalogNodesWithOptions(new NodeQueryOptions().setBlockingOptions(blockingQueryOptions1), h -> {
      List<String> names = h.result().getList().stream().map(Node::getName).collect(Collectors.toList());
      assertEquals(names.size(), 2);
      assertTrue(names.contains("attached_node"));
      latch1.countDown();
    });
    sleep(vertx, 2000);
    assertEquals(latch1.getCount(), 1);
    ConsulAgent attached = ctx.attachAgent("attached_node");
    latch1.await(2, TimeUnit.MINUTES);
    assertEquals(latch1.getCount(), 0);

    // wait until second consul closes
    CountDownLatch latch2 = new CountDownLatch(1);
    NodeList nodes2 = getAsync(h -> ctx.readClient().catalogNodes(h));
    BlockingQueryOptions blockingQueryOptions2 = new BlockingQueryOptions().setIndex(nodes2.getIndex());
    ctx.readClient().catalogNodesWithOptions(new NodeQueryOptions().setBlockingOptions(blockingQueryOptions2), h -> {
      latch2.countDown();
    });
    ctx.detachAgent(attached);
    latch2.await(2, TimeUnit.MINUTES);
    assertEquals(latch2.getCount(), 0);
  }
}
