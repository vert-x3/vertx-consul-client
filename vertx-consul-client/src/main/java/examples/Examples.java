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

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.*;

import java.util.Arrays;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Examples {

  public void exampleCreateDefault(Vertx vertx) {

    ConsulClient client = ConsulClient.create(vertx);

  }

  public void exampleCreateWithOptions(Vertx vertx) {

    JsonObject config = new JsonObject().put("host", "consul.example.com");

    ConsulClient client = ConsulClient.create(vertx, config);

  }

  public void tcpHealth(ConsulClient consulClient, Vertx vertx) {

    Handler<HttpServerRequest> alwaysGood = h -> h.response()

      .setStatusCode(200)

      .end();

    // create HTTP server to responce health check

    vertx.createHttpServer()

      .requestHandler(alwaysGood)

      .listen(4848);

    // check health via TCP port every 1 sec

    CheckOptions opts = new CheckOptions().setTcp("localhost:4848").setInterval("1s");

    // register TCP check

    consulClient.registerCheck(opts, res -> {

      if (res.succeeded()) {

        System.out.println("check successfully registered");

      } else {

        res.cause().printStackTrace();

      }

    });
  }

  public void kv(ConsulClient consulClient) {

    // put Key/Value pair to Consul Store
    consulClient.putValue("foo", "bar", res -> {

      if (res.succeeded()) {

        System.out.println("result of the operation: " + (res.result() ? "success" : "fail"));

      } else {

        res.cause().printStackTrace();

      }

    });

    //  get Key/Value pair from Consul Store
    consulClient.getValue("foo", res -> {

      if (res.succeeded()) {

        System.out.println("retrieved value: " + res.result().getValue());

      } else {

        res.cause().printStackTrace();

      }

    });

  }

  public void services(ConsulClient consulClient) {

    ServiceOptions opts = new ServiceOptions()
      .setId("serviceId")
      .setName("serviceName")
      .setTags(Arrays.asList("tag1", "tag2"))
      .setCheckOptions(new CheckOptions().setTtl("10s"))
      .setAddress("10.0.0.1")
      .setPort(8080);

    // Service registration

    consulClient.registerService(opts, res -> {

      if (res.succeeded()) {

        System.out.println("Service successfully registered");

      } else {

        res.cause().printStackTrace();

      }

    });

    // Discovery registered service

    consulClient.catalogServiceNodes("serviceName", res -> {

      if (res.succeeded()) {

        System.out.println("found " + res.result().getList().size() + " services");

        for (Service service : res.result().getList()) {

          System.out.println("Service node: " + service.getNode());

          System.out.println("Service address: " + service.getAddress());

          System.out.println("Service port: " + service.getPort());

        }

      } else {

        res.cause().printStackTrace();

      }

    });

    // Service deregistration

    consulClient.deregisterService("serviceId", res -> {

      if (res.succeeded()) {

        System.out.println("Service successfully deregistered");

      } else {

        res.cause().printStackTrace();

      }

    });

  }

  public void events(ConsulClient consulClient) {

    EventOptions opts = new EventOptions()
      .setTag("tag")
      .setPayload("message");

    // trigger a new user event

    consulClient.fireEventWithOptions("eventName", opts, res -> {

      if (res.succeeded()) {

        System.out.println("Event sent");

        System.out.println("id: " + res.result().getId());

      } else {

        res.cause().printStackTrace();

      }

    });

    // most recent events known by the agent

    consulClient.listEvents(res -> {

      if (res.succeeded()) {

        for(Event event: res.result()) {

          System.out.println("Event id: " + event.getId());

          System.out.println("Event name: " + event.getName());

          System.out.println("Event payload: " + event.getPayload());

        }

      } else {

        res.cause().printStackTrace();

      }

    });

  }

  public void sessions(ConsulClient consulClient, String sessionId) {

    SessionOptions opts = new SessionOptions()
      .setNode("nodeId")
      .setBehavior(SessionBehavior.RELEASE);

    // Create session

    consulClient.createSessionWithOptions(opts, res -> {

      if (res.succeeded()) {

        System.out.println("Session successfully created");

        System.out.println("id: " + res.result());

      } else {

        res.cause().printStackTrace();

      }

    });

    // Lists sessions belonging to a node

    consulClient.listNodeSessions("nodeId", res -> {

      if (res.succeeded()) {

        for(Session session: res.result()) {

          System.out.println("Session id: " + session.getId());

          System.out.println("Session node: " + session.getNode());

          System.out.println("Session create index: " + session.getCreateIndex());

        }

      } else {

        res.cause().printStackTrace();

      }

    });

    // Destroy session

    consulClient.destroySession(sessionId, res -> {

      if (res.succeeded()) {

        System.out.println("Session successfully destroyed");

      } else {

        res.cause().printStackTrace();

      }

    });

  }

}
