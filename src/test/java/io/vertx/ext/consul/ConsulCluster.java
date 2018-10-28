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

import io.vertx.ext.consul.dc.ConsulAgent;
import io.vertx.ext.consul.dc.ConsulAgentOptions;
import io.vertx.ext.consul.dc.ConsulDatacenter;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulCluster {

  private static final ConsulDatacenter dc = ConsulDatacenter.create();
  private static final AtomicBoolean started = new AtomicBoolean();

  private static ConsulAgent defaultAgent;
  private static String writeToken;
  private static String readToken;

  private static ConsulAgentOptions sslOptions() {
    ConsulAgentOptions opts = new ConsulAgentOptions()
      .setKeyFile(copyFileFromResources("server-key.pem", "server-key"))
      .setCertFile(copyFileFromResources("server-cert.pem", "server-cert"))
      .setCaFile(copyFileFromResources("server-cert-ca-chain.pem", "server-cert-ca-chain"));
    String v = getVersion();
    if (v != null) {
      opts.setConsulVersion(v);
    }
    return opts;
  }

  private static String getVersion() {
    String result = System.getProperty("CONSUL_AGENT_VERSION");
    if (result == null || result.isEmpty()) {
      result = System.getenv("CONSUL_AGENT_VERSION");
    }
    return result;
  }

  public static void start() {
    if (started.compareAndSet(false, true)) {
      ConsulAgentOptions options = sslOptions();
      defaultAgent = dc.attachAgent(options);
      try {
        writeToken = defaultAgent.createAclToken(Utils.readResource("write_rules.hcl"));
        readToken = defaultAgent.createAclToken(Utils.readResource("read_rules.hcl"));
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
      Runtime.getRuntime().addShutdownHook(new Thread(dc::stop));
    }
  }

  static ConsulDatacenter dc() {
    return dc;
  }

  static String writeToken() {
    return writeToken;
  }

  static String readToken() {
    return readToken;
  }

  public static ConsulAgent getDefaultAgent() {
    return defaultAgent;
  }

  static ConsulAgent attach(String nodeName) {
    return dc.attachAgent(sslOptions().setNodeName(nodeName));
  }

  static void detach(ConsulAgent agent) {
    dc.detachAgent(agent);
  }

  private static String copyFileFromResources(String fName, String toName) {
    try {
      String body = Utils.readResource(fName);
      File temp = File.createTempFile(toName, ".pem");
      PrintWriter out = new PrintWriter(temp.getAbsoluteFile());
      out.print(body);
      out.close();
      return temp.getAbsolutePath();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
