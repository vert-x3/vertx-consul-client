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

import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.PreparedQueryDefinition;
import io.vertx.ext.consul.PreparedQueryExecuteResponse;

import java.util.Arrays;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class PreparedQueries {

  public void preparedQueryDefinition() {

    PreparedQueryDefinition def = new PreparedQueryDefinition()
      .setName("Query name")
      .setService("service-${match(1)}-${match(2)}")
      .setDcs(Arrays.asList("dc1", "dc42"))
      .setTemplateType("name_prefix_match")
      .setTemplateRegexp("^find_(.+?)_(.+?)$");

  }

  public void createQuery(ConsulClient consulClient, PreparedQueryDefinition def) {

    consulClient.createPreparedQuery(def, res -> {
      if (res.succeeded()) {
        String queryId = res.result();
        System.out.println("Query created: " + queryId);
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void executeQuery(ConsulClient consulClient) {

    consulClient.executePreparedQuery("find_1_2", res -> {
      // matches template regexp "^find_(.+?)_(.+?)$"
      if (res.succeeded()) {
        PreparedQueryExecuteResponse response = res.result();
        System.out.println("Found " + response.getNodes().size() + " nodes");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void executeQueryId(ConsulClient consulClient, String id) {

    consulClient.executePreparedQuery(id, res -> {
      if (res.succeeded()) {
        PreparedQueryExecuteResponse response = res.result();
        System.out.println("Found " + response.getNodes().size() + " nodes");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

  public void deleteQuery(ConsulClient consulClient, String query) {

    consulClient.deletePreparedQuery(query, res -> {
      if (res.succeeded()) {
        System.out.println("Query deleted");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

}
