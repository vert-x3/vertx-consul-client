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
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Options used to register service.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
@JsonGen(publicConverter = false)
public class ServiceOptions {


  private String id;
  private String name;
  private List<String> tags;
  private String address;
  private Map<String, String> meta;
  private int port;
  private CheckOptions checkOptions;
  private List<CheckOptions> checkListOptions;
  private long createIndex;
  private long modifyIndex;

  /**
   * Default constructor
   */
  public ServiceOptions() {
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public ServiceOptions(ServiceOptions options) {
    this.id = options.id;
    this.name = options.name;
    this.tags = options.tags;
    this.address = options.address;
    this.meta = options.meta;
    this.port = options.port;
    this.checkOptions = options.checkOptions;
    this.checkListOptions = options.checkListOptions;
    this.createIndex = options.createIndex;
    this.modifyIndex = options.modifyIndex;
  }

  /**
   * Constructor from JSON
   *
   * @param options the JSON
   */
  public ServiceOptions(JsonObject options) {
    ServiceOptionsConverter.fromJson(options, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ServiceOptionsConverter.toJson(this, jsonObject);
    return jsonObject;
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
   * Set the ID of session
   *
   * @param id the ID of session
   * @return reference to this, for fluency
   */
  public ServiceOptions setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get service name
   *
   * @return service name
   */
  public String getName() {
    return name;
  }

  /**
   * Set service name
   *
   * @param name service name
   * @return reference to this, for fluency
   */
  public ServiceOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get list of tags associated with service
   *
   * @return list of tags
   */
  public List<String> getTags() {
    return tags;
  }

  /**
   * Set list of tags associated with service
   *
   * @param tags list of tags
   * @return reference to this, for fluency
   */
  public ServiceOptions setTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  /**
   * Get service address
   *
   * @return service address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set service address
   *
   * @param address service address
   * @return reference to this, for fluency
   */
  public ServiceOptions setAddress(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get arbitrary KV metadata linked to the service instance.
   *
   * @return arbitrary KV metadata
   */
  public Map<String, String> getMeta() {
    return meta;
  }

  /**
   * Specifies arbitrary KV metadata linked to the service instance.
   *
   * @param meta arbitrary KV metadata
   * @return reference to this, for fluency
   */
  public ServiceOptions setMeta(Map<String, String> meta) {
    this.meta = meta;
    return this;
  }

  /**
   * Get service port
   *
   * @return service port
   */
  public int getPort() {
    return port;
  }

  /**
   * Set service port
   *
   * @param port service port
   * @return reference to this, for fluency
   */
  public ServiceOptions setPort(int port) {
    this.port = port;
    return this;
  }

  /**
   * Get check options of service
   *
   * @return check options
   */
  public CheckOptions getCheckOptions() {
    return checkOptions;
  }

  /**
   * Set check options of service
   *
   * @param checkOptions check options
   * @return reference to this, for fluency
   */
  public ServiceOptions setCheckOptions(CheckOptions checkOptions) {
    this.checkOptions = checkOptions;
    return this;
  }

  /**
   * Get checks options of service
   *
   * @return checks options
   */
  public List<CheckOptions> getCheckListOptions() {
    return checkListOptions;
  }

  /**
   * Set checks options of service
   *
   * @param checkListOptions check options
   * @return reference to this, for fluency
   */
  public ServiceOptions setCheckListOptions(List<CheckOptions> checkListOptions) {
    this.checkListOptions = checkListOptions;
    return this;
  }

  /**
   * Get the internal index value that represents when the entry was created.
   *
   * @return the internal index value that represents when the entry was created.
   */
  public long getCreateIndex() {
    return createIndex;
  }

  /**
   * Set the internal index value that represents when the entry was created.
   *
   * @param createIndex the internal index value that represents when the entry was created.
   * @return reference to this, for fluency
   */
  public ServiceOptions setCreateIndex(long createIndex) {
    this.createIndex = createIndex;
    return this;
  }

  /**
   * Get the last index that modified this key.
   *
   * @return the last index that modified this key.
   */
  public long getModifyIndex() {
    return modifyIndex;
  }

  /**
   * Set the last index that modified this key.
   *
   * @param modifyIndex the last index that modified this key.
   * @return reference to this, for fluency
   */
  public ServiceOptions setModifyIndex(long modifyIndex) {
    this.modifyIndex = modifyIndex;
    return this;
  }

}
