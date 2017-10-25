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

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulDatacenter {

  private final String name;
  private final String masterToken;
  private final List<ConsulAgent> agents;

  public static ConsulDatacenter create() {
    return new ConsulDatacenter(new ConsulDatacenterOptions());
  }

  public static ConsulDatacenter create(ConsulDatacenterOptions options) {
    return new ConsulDatacenter(options);
  }

  private ConsulDatacenter(ConsulDatacenterOptions options) {
    name = options.getName();
    masterToken = options.getMasterToken();
    agents = new ArrayList<>();
  }

  public ConsulAgent attachAgent() {
    return attachAgent(new ConsulAgentOptions());
  }

  public ConsulAgent attachAgent(ConsulAgentOptions options) {
    ConsulAgent agent = new ConsulAgent(this, options);
    agents.add(agent);
    return agent;
  }

  public void detachAgent(ConsulAgent agent) {
    agents.remove(agent);
    agent.stop();
  }

  public void stop() {
    agents.forEach(ConsulAgent::stop);
    agents.clear();
  }

  public List<ConsulAgent> getAgents() {
    return agents;
  }

  public String getName() {
    return name;
  }

  public String getMasterToken() {
    return masterToken;
  }
}
