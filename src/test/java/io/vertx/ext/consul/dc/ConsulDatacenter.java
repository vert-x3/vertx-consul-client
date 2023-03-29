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

import io.vertx.ext.consul.instance.ConsulInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class ConsulDatacenter {

  private final String name;
  private final String masterToken;
  private String writeToken;
  private String readToken;


  public static ConsulDatacenter create() {
    return new ConsulDatacenter(new ConsulDatacenterOptions());
  }

  public static ConsulDatacenter create(ConsulDatacenterOptions options) {
    return new ConsulDatacenter(options);
  }

  private ConsulDatacenter(ConsulDatacenterOptions options) {
    name = options.getName();
    masterToken = options.getMasterToken();
  }

  public String getName() {
    return name;
  }

  public String getMasterToken() {
    return masterToken;
  }

  public void setWriteToken(String token) {
    writeToken = token;
  }

  public void setReadToken(String token) {
    readToken = token;
  }

  public String writeToken() {
    return writeToken;
  }

  public String readToken() {
    return readToken;
  }
}
