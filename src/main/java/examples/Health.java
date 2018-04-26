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

import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClient;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Health {

  public void checkOpts() {

    CheckOptions opts = new CheckOptions()
      .setTcp("localhost:4848")
      .setInterval("1s");

  }

  public void tcpHealth(ConsulClient consulClient, CheckOptions opts) {

    consulClient.registerCheck(opts, res -> {
      if (res.succeeded()) {
        System.out.println("check successfully registered");
      } else {
        res.cause().printStackTrace();
      }
    });

  }

}
