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
package examples;

import io.vertx.ext.consul.BlockingQueryOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.NodeQueryOptions;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Nodes {

  public void catalogNodes(ConsulClient consulClient) {

    consulClient.catalogNodes(res -> {
      if (res.succeeded()) {
        System.out.println("found " + res.result().getList().size() + " nodes");
        System.out.println("consul state index " + res.result().getIndex());
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void blockingQuery(ConsulClient consulClient, long lastIndex) {

    NodeQueryOptions opts = new NodeQueryOptions()
      .setNear("_agent")
      .setBlockingOptions(new BlockingQueryOptions().setIndex(lastIndex));

    consulClient.catalogNodesWithOptions(opts, res -> {
      if (res.succeeded()) {
        System.out.println("found " + res.result().getList().size() + " nodes");
      } else {
        res.cause().printStackTrace();
      }
    });

  }
}
