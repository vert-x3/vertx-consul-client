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

import io.vertx.core.Vertx;
import io.vertx.ext.consul.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Examples {

  public void exampleCreateDefault(Vertx vertx) {

    ConsulClient client = ConsulClient.create(vertx);

  }

  public void exampleCreateWithOptions(Vertx vertx) {

    ConsulClientOptions options = new ConsulClientOptions()
      .setHost("consul.example.com");

    ConsulClient client = ConsulClient.create(vertx, options);

  }

  public void blockingOptions(long lastIndex) {

    BlockingQueryOptions opts = new BlockingQueryOptions()
      .setIndex(lastIndex)
      .setWait("1m");

  }

  public void nodes(ConsulClient consulClient, long lastIndex) {

    consulClient.catalogNodes(res -> {

      if (res.succeeded()) {

        System.out.println("found " + res.result().getList().size() + " nodes");

        System.out.println("consul state index " + res.result().getIndex());

      } else {

        res.cause().printStackTrace();

      }

    });

    // blocking request to catalog for nodes, sorted by distance from agent

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
