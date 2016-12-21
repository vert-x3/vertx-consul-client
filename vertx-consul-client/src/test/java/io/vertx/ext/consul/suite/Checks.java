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

import io.vertx.ext.consul.*;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Checks extends ChecksBase {

  @Test
  public void bindCheckToService() {
    String serviceName = "serviceName";
    ServiceOptions service = new ServiceOptions()
      .setName(serviceName)
      .setAddress("10.0.0.1")
      .setPort(8080);
    runAsync(h -> writeClient.registerService(service, h));

    List<Service> services = getAsync(h -> writeClient.localServices(h));
    Service s = services.stream().filter(i -> "serviceName".equals(i.getName())).findFirst().get();
    String serviceId = s.getId();
    assertEquals(s.getAddress(), "10.0.0.1");
    assertEquals(s.getPort(), 8080);

    CheckOptions check = new CheckOptions()
      .setId("checkId")
      .setName("checkName")
      .setNotes("checkNotes")
      .setServiceId(serviceId)
      .setStatus(CheckStatus.PASSING)
      .setTtl("10s");
    runAsync(h -> writeClient.registerCheck(check, h));

    List<Check> checks = getAsync(h -> writeClient.localChecks(h));
    Check c = checks.stream().filter(i -> "checkId".equals(i.getId())).findFirst().get();
    assertEquals(c.getServiceId(), serviceId);
    assertEquals(c.getStatus(), CheckStatus.PASSING);
    assertEquals(c.getNotes(), "checkNotes");

    runAsync(h -> writeClient.deregisterService(serviceId, h));
  }

  @Override
  String createCheck(CheckOptions opts) {
    String id = opts.getId();
    if (id == null) {
      id = "checkId";
      opts.setId(id);
    }
    runAsync(h -> writeClient.registerCheck(opts, h));
    return id;
  }

}
