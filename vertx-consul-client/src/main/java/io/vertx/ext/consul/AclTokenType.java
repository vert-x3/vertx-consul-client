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

/**
 * Acl token type is either "client" (meaning the token cannot modify ACL rules) or "management"
 * (meaning the token is allowed to perform all actions).
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/internals/acl.html">Acl Consul system</a>
 */
public enum AclTokenType {

  CLIENT("client"),
  MANAGEMENT("management");

  public final String key;

  public static AclTokenType of(String name) {
    if (name == null) {
      return null;
    } else {
      return CLIENT.key.equals(name) ? CLIENT : MANAGEMENT;
    }
  }

  AclTokenType(String key) {
    this.key = key;
  }
}
