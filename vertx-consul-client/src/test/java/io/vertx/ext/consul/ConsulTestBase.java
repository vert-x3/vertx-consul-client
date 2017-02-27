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

import com.pszymczyk.consul.ConsulProcess;
import io.vertx.core.Vertx;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.test.core.VertxTestBase;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulTestBase extends VertxTestBase {

  protected static BiFunction<Vertx, ConsulClientOptions, ConsulClient> clientCreator;
  protected static Consumer<ConsulClient> clientCloser;

  protected ConsulClient masterClient;
  protected ConsulClient writeClient;
  protected ConsulClient readClient;
  protected ConsulClientOptions writeClientOptions;
  protected ConsulClientOptions readClientOptions;
  protected String nodeName;
  protected String dc;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    masterClient = clientCreator.apply(vertx, config(ConsulCluster.masterToken(), false));
    writeClient = clientCreator.apply(vertx, writeClientOptions = config(ConsulCluster.writeToken(), false));
    readClient = clientCreator.apply(vertx, readClientOptions = config(ConsulCluster.readToken(), false));
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

  protected ConsulClient createSecureClient(boolean trustAll, PemTrustOptions trustOptions) {
    ConsulClientOptions options = config(ConsulCluster.writeToken(), true)
      .setTrustAll(trustAll)
      .setPemTrustOptions(trustOptions);
    return clientCreator.apply(vertx, options);
  }

  protected ConsulProcess attachConsul(String nodeName) {
    return ConsulCluster.attach(nodeName);
  }

  private ConsulClientOptions config(String token, boolean secure) {
    return new ConsulClientOptions()
      .setAclToken(token)
      .setDc(ConsulCluster.dc())
      .setHost("localhost")
      .setPort(secure ? ConsulCluster.httpsPort() : ConsulCluster.consul().getHttpPort())
      .setSsl(secure);
  }

}
