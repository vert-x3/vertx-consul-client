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

import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.consul.dc.ConsulAgent;
import io.vertx.ext.consul.dc.ConsulDatacenter;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulContext {

  private final Function<ConsulClientOptions, ConsulClient> creator;
  private final Consumer<ConsulClient> closer;

  public static final String KEY_RW_PREFIX = "foo/";

  private ConsulClient masterClient;
  private ConsulClient writeClient;
  private ConsulClient readClient;
  private ConsulClientOptions writeClientOptions;
  private ConsulClientOptions readClientOptions;

  public ConsulContext(Function<ConsulClientOptions, ConsulClient> creator, Consumer<ConsulClient> closer) {
    this.creator = creator;
    this.closer = closer;
  }

  private static ConsulClientOptions config(String token, boolean secure) {
    return new ConsulClientOptions()
      .setAclToken(token)
      .setDc(ConsulCluster.dc().getName())
      .setHost("localhost")
      .setPort(secure ? ConsulCluster.getDefaultAgent().getHttpsPort() : ConsulCluster.getDefaultAgent().getHttpPort())
      .setSsl(secure);
  }

  public void start() {
    masterClient = creator.apply(config(ConsulCluster.dc().getMasterToken(), false));
    writeClient = creator.apply(writeClientOptions = config(ConsulCluster.writeToken(), false));
    readClient = creator.apply(readClientOptions = config(ConsulCluster.readToken(), false));
  }

  public void stop() {
    closer.accept(masterClient);
    closer.accept(writeClient);
    closer.accept(readClient);
  }

  public String nodeName() {
    return ConsulCluster.getDefaultAgent().getName();
  }

  public String consulVersion() {
    return ConsulCluster.getDefaultAgent().getConsulVersion();
  }

  public ConsulDatacenter dc() {
    return ConsulCluster.dc();
  }

  public ConsulClientOptions writeClientOptions() {
    return writeClientOptions;
  }

  public ConsulClientOptions readClientOptions() {
    return readClientOptions;
  }

  public ConsulClient masterClient() {
    return masterClient;
  }

  public ConsulClient writeClient() {
    return writeClient;
  }

  public ConsulClient readClient() {
    return readClient;
  }

  public ConsulClient createClient(ConsulClientOptions opts) {
    return creator.apply(opts);
  }

  public ConsulClient createSecureClient(boolean trustAll, PemTrustOptions trustOptions) {
    ConsulClientOptions options = config(ConsulCluster.writeToken(), true)
      .setTrustAll(trustAll)
      .setPemTrustOptions(trustOptions);
    return creator.apply(options);
  }

  public void closeClient(ConsulClient client) {
    closer.accept(client);
  }

  public ConsulAgent attachAgent(String nodeName) {
    return ConsulCluster.attach(nodeName);
  }

  public void detachAgent(ConsulAgent agent) {
    ConsulCluster.detach(agent);
  }
}
