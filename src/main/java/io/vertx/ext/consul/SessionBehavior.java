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
 * When a session is invalidated, it is destroyed and can no longer be used. What happens to the associated locks
 * depends on the behavior specified at creation time. Consul supports a release and delete behavior.
 * The release behavior is the default if none is specified.
 * <p>
 * If the release behavior is being used, any of the locks held in association with the session are released,
 * and the ModifyIndex of the key is incremented. Alternatively, if the delete behavior is used,
 * the key corresponding to any of the held locks is simply deleted. This can be used to create ephemeral
 * entries that are automatically deleted by Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/internals/sessions.html">Consul sessions documentation</a>
 */
public enum SessionBehavior {

  RELEASE("release"),
  DELETE("delete");

  public final String key;

  public static SessionBehavior of(String key) {
    for (SessionBehavior sessionBehavior : values()) {
      if (sessionBehavior.key.equals(key)) {
        return sessionBehavior;
      }
    }
    return null;
  }

  SessionBehavior(String key) {
    this.key = key;
  }
}
