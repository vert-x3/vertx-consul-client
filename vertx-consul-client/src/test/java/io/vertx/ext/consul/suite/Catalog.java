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
import io.vertx.ext.consul.Node;
import io.vertx.ext.consul.NodeList;
import io.vertx.ext.consul.Utils;
import org.junit.Test;

import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Catalog extends ConsulTestBase {

  @Test
  public void datacenters() {
    List<String> datacenters = Utils.getAsync(h -> readClient.catalogDatacenters(h));
    assertEquals(datacenters.size(), 1);
    assertEquals(datacenters.get(0), dc);
  }

  @Test
  public void nodes() {
    List<Node> nodes = Utils.<NodeList>getAsync(h -> readClient.catalogNodes(h)).getList();
    assertEquals(nodes.size(), 1);
    Node node = nodes.get(0);
    assertEquals(node.getNode(), nodeName);
  }

}
