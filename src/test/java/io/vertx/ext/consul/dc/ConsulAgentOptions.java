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
package io.vertx.ext.consul.dc;

import java.util.Random;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulAgentOptions {

  private static final Random random = new Random();
  private static final String DEFAULT_CONSUL_VERSION = "1.1.0";
  private static final String DEFAULT_ADDRESS = "127.0.0.1";

  private String consulVersion;
  private String address;
  private String nodeName;
  private String nodeId;
  private String keyFile;
  private String certFile;
  private String caFile;

  public ConsulAgentOptions() {
    consulVersion = DEFAULT_CONSUL_VERSION;
    address = DEFAULT_ADDRESS;
    nodeName = "node-" + randomHex(16);
    nodeId = randomNodeId();
  }

  public String getConsulVersion() {
    return consulVersion;
  }

  public String getAddress() {
    return address;
  }

  public String getNodeName() {
    return nodeName;
  }

  public String getNodeId() {
    return nodeId;
  }

  public String getKeyFile() {
    return keyFile;
  }

  public String getCertFile() {
    return certFile;
  }

  public String getCaFile() {
    return caFile;
  }

  public ConsulAgentOptions setConsulVersion(String consulVersion) {
    this.consulVersion = consulVersion;
    return this;
  }

  public ConsulAgentOptions setAddress(String address) {
    this.address = address;
    return this;
  }

  public ConsulAgentOptions setNodeName(String nodeName) {
    this.nodeName = nodeName;
    return this;
  }

  public ConsulAgentOptions setNodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  public ConsulAgentOptions setKeyFile(String keyFile) {
    this.keyFile = keyFile;
    return this;
  }

  public ConsulAgentOptions setCertFile(String certFile) {
    this.certFile = certFile;
    return this;
  }

  public ConsulAgentOptions setCaFile(String caFile) {
    this.caFile = caFile;
    return this;
  }

  private static String randomNodeId() {
    return randomHex(8) + "-" + randomHex(4) + "-" + randomHex(4) + "-" + randomHex(4) + "-" + randomHex(12);
  }

  private static String randomHex(int len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(Long.toHexString(random.nextInt(16)));
    }
    return sb.toString();
  }
}
