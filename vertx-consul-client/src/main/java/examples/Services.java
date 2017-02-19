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

import io.vertx.ext.consul.*;

import java.util.Arrays;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Services {

  public void servicesOpts() {

    ServiceOptions opts = new ServiceOptions()
      .setName("serviceName")
      .setId("serviceId")
      .setTags(Arrays.asList("tag1", "tag2"))
      .setCheckOptions(new CheckOptions().setTtl("10s"))
      .setAddress("10.0.0.1")
      .setPort(8048);

  }

  public void register(ConsulClient consulClient, ServiceOptions opts) {

    consulClient.registerService(opts, res -> {
      if (res.succeeded()) {
        System.out.println("Service successfully registered");
      } else {
        res.cause().printStackTrace();
      }

    });

  }

  public void discovery(ConsulClient consulClient) {

    consulClient.catalogServiceNodes("serviceName", res -> {
      if (res.succeeded()) {
        System.out.println("found " + res.result().getList().size() + " services");
        System.out.println("consul state index: " + res.result().getIndex());
        for (Service service : res.result().getList()) {
          System.out.println("Service node: " + service.getNode());
          System.out.println("Service address: " + service.getAddress());
          System.out.println("Service port: " + service.getPort());
        }
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void health(ConsulClient consulClient, boolean passingOnly) {

    consulClient.healthServiceNodes("serviceName", passingOnly, res -> {
      if (res.succeeded()) {
        System.out.println("found " + res.result().getList().size() + " services");
        System.out.println("consul state index: " + res.result().getIndex());
        for (ServiceEntry entry : res.result().getList()) {
          System.out.println("Service node: " + entry.getNode());
          System.out.println("Service address: " + entry.getService().getAddress());
          System.out.println("Service port: " + entry.getService().getPort());
        }
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void queryOpts(long lastIndex) {

    ServiceQueryOptions queryOpts = new ServiceQueryOptions()
      .setTag("tag1")
      .setNear("_agent")
      .setBlockingOptions(new BlockingQueryOptions().setIndex(lastIndex));

  }

  public void queryWithOptions(ConsulClient consulClient, boolean passingOnly, ServiceQueryOptions queryOpts) {

    consulClient.healthServiceNodesWithOptions("serviceName", passingOnly, queryOpts, res -> {
      if (res.succeeded()) {
        System.out.println("found " + res.result().getList().size() + " services");
      } else {
        res.cause().printStackTrace();
      }

    });
  }

  public void deregister(ConsulClient consulClient) {

    consulClient.deregisterService("serviceId", res -> {
      if (res.succeeded()) {
        System.out.println("Service successfully deregistered");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

}
