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

import java.util.ArrayList;
import java.util.List;

/**
 * Holds properties of Consul sessions
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class Session {

  private long lockDelay;
  private String node;
  private List<String> checks;
  private long createIndex;
  private long index;
  private String id;

  /**
   * Default constructor
   */
  public Session() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public Session(Session other) {
    this.lockDelay = other.lockDelay;
    this.node = other.node;
    this.checks = other.checks;
    this.createIndex = other.createIndex;
    this.index = other.index;
    this.id = other.id;
  }

  /**
   * Constructor from JSON
   *
   * @param session the JSON
   */
  public Session(JsonObject session) {
    SessionConverter.fromJson(session, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    SessionConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Lock delay is a time duration, between <code>0</code> and <code>60</code> seconds. When a session invalidation
   * takes place, Consul prevents any of the previously held locks from being re-acquired
   * for the <code>lock-delay</code> interval. The default is <code>15s</code>.
   *
   * @return the lock delay in seconds
   */
  public long getLockDelay() {
    return lockDelay;
  }

  /**
   * Get the ID of node
   *
   * @return the ID of node
   */
  public String getNode() {
    return node;
  }

  /**
   * Get the list of associated health checks
   *
   * @return the list of associated health checks
   */
  public List<String> getChecks() {
    return checks;
  }

  /**
   * Get the ID of session
   *
   * @return the ID of session
   */
  public String getId() {
    return id;
  }

  /**
   * Get the create index of session
   *
   * @return the create index of session
   */
  public long getCreateIndex() {
    return createIndex;
  }

  /**
   * Get Consul index
   *
   * @return the consul index
   */
  public long getIndex() {
    return index;
  }

  /**
   * Set the Lock delay of session
   *
   * @param lockDelay the Lock delay of session
   * @return reference to this, for fluency
   */
  public Session setLockDelay(long lockDelay) {
    this.lockDelay = lockDelay;
    return this;
  }

  /**
   * Set the ID of node
   *
   * @param node the ID of node
   * @return reference to this, for fluency
   */
  public Session setNode(String node) {
    this.node = node;
    return this;
  }

  /**
   * Set the list of associated health checks
   *
   * @param checks the list of associated health checks
   * @return reference to this, for fluency
   */
  public Session setChecks(List<String> checks) {
    this.checks = checks;
    return this;
  }

  /**
   * Set the create index of session
   *
   * @param createIndex the create index of session
   * @return reference to this, for fluency
   */
  public Session setCreateIndex(long createIndex) {
    this.createIndex = createIndex;
    return this;
  }

  /**
   * Set the ID of node
   *
   * @param id the ID of node
   * @return reference to this, for fluency
   */
  public Session setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Set Consul index
   *
   * @param index the consul index
   * @return reference to this, for fluency
   */
  public Session setIndex(long index) {
    this.index = index;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Session session = (Session) o;

    if (lockDelay != session.lockDelay) return false;
    if (createIndex != session.createIndex) return false;
    if (index != session.index) return false;
    if (node != null ? !node.equals(session.node) : session.node != null) return false;
    if (checks != null ? !sorted().equals(session.sorted()) : session.checks != null) return false;
    return id != null ? id.equals(session.id) : session.id == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (lockDelay ^ (lockDelay >>> 32));
    result = 31 * result + (node != null ? node.hashCode() : 0);
    result = 31 * result + (checks != null ? sorted().hashCode() : 0);
    result = 31 * result + (int) (createIndex ^ (createIndex >>> 32));
    result = 31 * result + (int) (index ^ (index >>> 32));
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }

  private List<String> sorted() {
    List<String> sorted = null;
    if (checks != null) {
      sorted = new ArrayList<>(checks);
      sorted.sort(String::compareTo);
    }
    return sorted;
  }
}
