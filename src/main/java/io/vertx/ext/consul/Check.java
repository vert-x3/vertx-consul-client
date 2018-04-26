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
 * Holds check properties
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class Check {

  private String id;
  private String name;
  private CheckStatus status;
  private String notes;
  private String output;
  private String serviceId;
  private String serviceName;
  private String nodeName;

  /**
   * Default constructor
   */
  public Check() {}

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public Check(Check other) {
    this.id = other.id;
    this.name = other.name;
    this.status = other.status;
    this.notes = other.notes;
    this.output = other.output;
    this.serviceId = other.serviceId;
    this.serviceName = other.serviceName;
    this.nodeName = other.nodeName;
  }

  /**
   * Constructor from JSON
   *
   * @param check the JSON
   */
  public Check(JsonObject check) {
    CheckConverter.fromJson(check, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    CheckConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get the ID of check
   *
   * @return the ID of check
   */
  public String getId() {
    return id;
  }

  /**
   * Get the name of check
   *
   * @return the name of check
   */
  public String getName() {
    return name;
  }

  /**
   * Get the name of node
   *
   * @return name of node
   */
  public String getNodeName() {
    return nodeName;
  }

  /**
   * Get the status of check
   *
   * @return the status of check
   */
  public CheckStatus getStatus() {
    return status;
  }

  /**
   * Get the human-readable note of check
   *
   * @return the human-readable note of check
   */
  public String getNotes() {
    return notes;
  }

  /**
   * Get the output of check
   *
   * @return the output of check
   */
  public String getOutput() {
    return output;
  }

  /**
   * Get the ID of service with which this check associated
   *
   * @return the ID of service with which this check associated
   */
  public String getServiceId() {
    return serviceId;
  }

  /**
   * Get the name of service with which this check associated
   *
   * @return the name of service with which this check associated
   */
  public String getServiceName() {
    return serviceName;
  }

  /**
   * Set the ID of check
   *
   * @param id the ID of check
   * @return reference to this, for fluency
   */
  public Check setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Set the name of check
   *
   * @param name the name of check
   * @return reference to this, for fluency
   */
  public Check setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Set the status of check
   *
   * @param status the status of check
   * @return reference to this, for fluency
   */
  public Check setStatus(CheckStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Set the human-readable note of check
   *
   * @param notes the human-readable note of check
   * @return reference to this, for fluency
   */
  public Check setNotes(String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Set the output of check
   *
   * @param output the output of check
   * @return reference to this, for fluency
   */
  public Check setOutput(String output) {
    this.output = output;
    return this;
  }

  /**
   * Set the ID of service with which this check associated
   *
   * @param serviceId the ID of service with which this check associated
   * @return reference to this, for fluency
   */
  public Check setServiceId(String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  /**
   * Set the name of service with which this check associated
   *
   * @param serviceName the name of service with which this check associated
   * @return reference to this, for fluency
   */
  public Check setServiceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * Set the name of node
   *
   * @param nodeName the name of node
   * @return reference to this, for fluency
   */
  public Check setNodeName(String nodeName) {
    this.nodeName = nodeName;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Check check = (Check) o;

    if (id != null ? !id.equals(check.id) : check.id != null) return false;
    if (name != null ? !name.equals(check.name) : check.name != null) return false;
    if (status != check.status) return false;
    if (notes != null ? !notes.equals(check.notes) : check.notes != null) return false;
    if (output != null ? !output.equals(check.output) : check.output != null) return false;
    if (serviceId != null ? !serviceId.equals(check.serviceId) : check.serviceId != null) return false;
    if (serviceName != null ? !serviceName.equals(check.serviceName) : check.serviceName != null) return false;
    return nodeName != null ? nodeName.equals(check.nodeName) : check.nodeName == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + (output != null ? output.hashCode() : 0);
    result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
    result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
    result = 31 * result + (nodeName != null ? nodeName.hashCode() : 0);
    return result;
  }
}
