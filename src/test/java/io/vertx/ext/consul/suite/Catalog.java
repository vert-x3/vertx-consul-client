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

import io.vertx.ext.consul.BlockingQueryOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Node;
import io.vertx.ext.consul.NodeQueryOptions;
import io.vertx.ext.consul.Service;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.ext.consul.dc.ConsulAgent;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.RandomObjects.randomNode;
import static io.vertx.ext.consul.RandomObjects.randomServiceOptions;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class Catalog extends ConsulTestBase {

  @Test
  public void datacenters(TestContext tc) {
    ctx.readClient().catalogDatacenters(tc.asyncAssertSuccess(datacenters -> {
      tc.assertEquals(datacenters.size(), 1);
      tc.assertEquals(datacenters.get(0), ctx.dc().getName());
    }));
  }

  @Test
  public void nodes(TestContext tc) {
    ctx.readClient().catalogNodes(tc.asyncAssertSuccess(nodes -> {
      tc.assertEquals(nodes.getList().size(), 1);
      Node node = nodes.getList().get(0);
      tc.assertEquals(node.getName(), ctx.nodeName());
    }));
  }

  @Test
  public void blockingQuery(TestContext tc) throws InterruptedException {
    ctx.readClient().catalogNodes(tc.asyncAssertSuccess(nodes1 -> {
      Async async1 = tc.async();

      System.out.println(">>>>>>> wait for new node");
      NodeQueryOptions blockingQueryOptions1 = new NodeQueryOptions()
        .setBlockingOptions(new BlockingQueryOptions().setIndex(nodes1.getIndex()));
      ctx.readClient().catalogNodesWithOptions(blockingQueryOptions1, h -> {
        System.out.println(">>>>>>> new node event received");
        List<String> names = h.result().getList().stream().map(Node::getName).collect(Collectors.toList());
        tc.assertEquals(names.size(), 2);
        tc.assertTrue(names.contains("attached_node"));
        async1.countDown();
      });

      vertx.setTimer(1000, l -> {
        System.out.println(">>>>>>> new node is still not ready");
        tc.assertEquals(async1.count(), 1);
        vertx.<ConsulAgent>executeBlocking(b1 -> b1.complete(ctx.attachAgent("attached_node")), tc.asyncAssertSuccess(attached -> {
          System.out.println(">>>>>>> new node attached");
          async1.handler(v -> {
            ctx.readClient().catalogNodes(tc.asyncAssertSuccess(nodes2 -> {
              NodeQueryOptions blockingQueryOptions2 = new NodeQueryOptions()
                .setBlockingOptions(new BlockingQueryOptions().setIndex(nodes2.getIndex()));
              System.out.println(">>>>>>> wait for new node detaching");
              ctx.readClient().catalogNodesWithOptions(blockingQueryOptions2, tc.asyncAssertSuccess());
              vertx.executeBlocking(b2 -> ctx.detachAgent(attached), detached -> System.out.println(">>>>>>> new node detached"));
            }));
          });
        }));
      });
    }));
  }

  @Test
  public void testRegisterAndDeregisterCatalogService(TestContext tc) {

    ConsulClient consulWriteClient = ctx.writeClient();
    Node node = randomNode(UUID.randomUUID(), ctx.dc().getName());
    String nodeName = node.getName();
    ServiceOptions serviceOptions = randomServiceOptions();
    serviceOptions.setCheckOptions(null);
    serviceOptions.setCheckListOptions(null);

    consulWriteClient.registerCatalogService(node, serviceOptions, tc.asyncAssertSuccess(unused -> {
      consulWriteClient.catalogNodesWithOptions(null, tc.asyncAssertSuccess(nodes -> {
        Optional<Node> nodeOptional = nodes.getList().stream().filter(n -> nodeName.equals(n.getName())).findFirst();
        tc.assertTrue(nodeOptional.isPresent());
        tc.assertEquals(node, nodeOptional.get());

        consulWriteClient.catalogNodeServices(node.getName(), tc.asyncAssertSuccess(serviceList -> {
          tc.assertEquals(serviceList.getList().size(), 1);
          Service service = serviceList.getList().get(0);
          tc.assertEquals(service.getNodeAddress(), node.getAddress());
          tc.assertEquals(service.getNode(), nodeName);
          tc.assertEquals(service.getId(), serviceOptions.getId());
          tc.assertEquals(service.getName(), serviceOptions.getName());
          tc.assertEquals(service.getTags(), serviceOptions.getTags());
          tc.assertEquals(service.getAddress(), serviceOptions.getAddress());
          tc.assertEquals(service.getMeta(), serviceOptions.getMeta());
          tc.assertEquals(service.getPort(), serviceOptions.getPort());

          Async async = tc.async(2);

          consulWriteClient.deregisterCatalogService(node.getName(), service.getId(), tc.asyncAssertSuccess(deregistered -> {

            consulWriteClient.catalogNodesWithOptions(null, tc.asyncAssertSuccess(nodes1 -> {
              Optional<Node> nodeOptional1 = nodes1.getList().stream().filter(n -> nodeName.equals(n.getName())).findFirst();
              tc.assertTrue(nodeOptional1.isPresent());
              async.countDown();
            }));
            consulWriteClient.catalogNodeServices(node.getName(), tc.asyncAssertSuccess(serviceList1 -> {
              tc.assertTrue(serviceList1.getList().isEmpty());
              async.countDown();
            }));
          }));

          async.handler(v -> consulWriteClient.deregisterCatalogService(node.getName(), null, tc.asyncAssertSuccess(deregistered -> consulWriteClient.catalogNodesWithOptions(null, tc.asyncAssertSuccess(nodes1 -> {
            Optional<Node> nodeOptional1 = nodes1.getList().stream().filter(n -> nodeName.equals(n.getName())).findFirst();
            tc.assertFalse(nodeOptional1.isPresent());
          })))));
        }));
      }));
    }));
  }
}
