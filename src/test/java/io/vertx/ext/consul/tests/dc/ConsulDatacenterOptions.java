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
package io.vertx.ext.consul.tests.dc;

import java.util.UUID;

import static io.vertx.test.core.TestUtils.randomPositiveInt;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulDatacenterOptions {

  private String name;
  private String masterToken;

  public ConsulDatacenterOptions() {
    name = "dc-" + randomPositiveInt();
    masterToken = UUID.randomUUID().toString();
  }

  public String getName() {
    return name;
  }

  public String getMasterToken() {
    return masterToken;
  }

  public ConsulDatacenterOptions setName(String name) {
    this.name = name;
    return this;
  }

  public ConsulDatacenterOptions setMasterToken(String masterToken) {
    this.masterToken = masterToken;
    return this;
  }
}
