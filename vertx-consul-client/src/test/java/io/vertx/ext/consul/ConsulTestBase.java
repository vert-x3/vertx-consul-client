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
package io.vertx.ext.consul;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.VertxTestBase;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulTestBase extends VertxTestBase {

  protected static BiFunction<Vertx, JsonObject, ConsulClient> clientCreator;
  protected static Consumer<ConsulClient> clientCloser;

  protected ConsulClient masterClient;
  protected ConsulClient writeClient;
  protected ConsulClient readClient;
  protected String nodeName;
  protected String dc;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    masterClient = clientCreator.apply(vertx, config(ConsulCluster.masterToken()));
    writeClient = clientCreator.apply(vertx, config(ConsulCluster.writeToken()));
    readClient = clientCreator.apply(vertx, config(ConsulCluster.readToken()));
    nodeName = ConsulCluster.nodeName();
    dc = ConsulCluster.dc();
  }

  @Override
  public void tearDown() throws Exception {
    clientCloser.accept(masterClient);
    clientCloser.accept(writeClient);
    clientCloser.accept(readClient);
    super.tearDown();
  }

  private JsonObject config(String token) {
    return new JsonObject()
      .put("acl_token", token)
      .put("dc", ConsulCluster.dc())
      .put("host", "localhost")
      .put("port", ConsulCluster.consul().getHttpPort());
  }

}
