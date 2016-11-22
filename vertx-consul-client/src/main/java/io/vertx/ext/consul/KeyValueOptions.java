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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Options used to put key/value pair to Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class KeyValueOptions {

  private static final String ACQUIRE_SESSION_KEY = "AcquireSession";
  private static final String RELEASE_SESSION_KEY = "ReleaseSession";
  private static final String FLAGS_KEY = "Flags";
  private static final String CAS_KEY = "Cas";

  private String acquireSession;
  private String releaseSession;
  private long flags;
  private long cas;

  /**
   * Default constructor
   */
  public KeyValueOptions() {
    this.cas = -1;
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public KeyValueOptions(KeyValueOptions options) {
    this.acquireSession = options.acquireSession;
    this.releaseSession = options.releaseSession;
    this.flags = options.flags;
    this.cas = options.cas;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public KeyValueOptions(JsonObject options) {
    acquireSession = options.getString(ACQUIRE_SESSION_KEY);
    releaseSession = options.getString(RELEASE_SESSION_KEY);
    String flagsStr = options.getString(FLAGS_KEY);
    flags = flagsStr == null ? 0L : Long.parseUnsignedLong(flagsStr);
    String casStr = options.getString(CAS_KEY);
    cas = casStr == null ? -1L : Long.parseUnsignedLong(casStr);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (acquireSession != null) {
      jsonObject.put(ACQUIRE_SESSION_KEY, acquireSession);
    }
    if (releaseSession != null) {
      jsonObject.put(RELEASE_SESSION_KEY, releaseSession);
    }
    if (flags != 0) {
      jsonObject.put(FLAGS_KEY, Long.toUnsignedString(flags));
    }
    if (cas >= 0) {
      jsonObject.put(CAS_KEY, Long.toUnsignedString(cas));
    }
    return jsonObject;
  }

  /**
   * Get session ID for lock acquisition operation.
   *
   * @return the ID of session
   */
  public String getAcquireSession() {
    return acquireSession;
  }

  /**
   * Set session ID for lock acquisition operation.
   *
   * @param acquireSession the ID of session
   * @return reference to this, for fluency
   */
  public KeyValueOptions setAcquireSession(String acquireSession) {
    this.acquireSession = acquireSession;
    return this;
  }

  /**
   * Get session ID for lock release operation.
   *
   * @return the ID of session
   */
  public String getReleaseSession() {
    return releaseSession;
  }

  /**
   * Set session ID for lock release operation.
   *
   * @param releaseSession the ID of session
   * @return reference to this, for fluency
   */
  public KeyValueOptions setReleaseSession(String releaseSession) {
    this.releaseSession = releaseSession;
    return this;
  }

  /**
   * Get the flags. Flags is an value between {@code 0} and 2<sup>64</sup>-1 that can be attached to each entry.
   * Clients can choose to use this however makes sense for their application.
   *
   * @return the flags
   */
  public long getFlags() {
    return flags;
  }


  /**
   * Set the flags. Flags is an value between {@code 0} and 2<sup>64</sup>-1 that can be attached to each entry.
   * Clients can choose to use this however makes sense for their application.
   *
   * @param flags the flags
   * @return reference to this, for fluency
   */
  public KeyValueOptions setFlags(long flags) {
    this.flags = flags;
    return this;
  }

  /**
   * Get  the Check-And-Set index. If the index is {@code 0}, Consul will only put the key if it does not already exist.
   * If the index is non-zero, the key is only set if the index matches the ModifyIndex of that key.
   *
   * @return the Check-And-Set index
   * @see KeyValue#getModifyIndex()
   */
  public long getCasIndex() {
    return cas;
  }

  /**
   * Set the Check-And-Set index. If the index is {@code 0}, Consul will only put the key if it does not already exist.
   * If the index is non-zero, the key is only set if the index matches the ModifyIndex of that key.
   *
   * @param index the Check-And-Set index
   * @return reference to this, for fluency
   * @see KeyValue#getModifyIndex()
   */
  public KeyValueOptions setCasIndex(long index) {
    this.cas = index;
    return this;
  }
}
