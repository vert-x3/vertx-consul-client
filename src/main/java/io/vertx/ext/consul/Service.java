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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vertx.ext.consul.impl.Utils.listOf;
import static io.vertx.ext.consul.impl.Utils.mapStringString;

/**
 * Holds properties of service and node that its containing
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Service implements TxnResult {

  private static final String NODE = "Node";
  private static final String ADDRESS = "Address";
  private static final String SERVICE_ID = "ServiceID";
  private static final String SERVICE_NAME = "ServiceName";
  private static final String SERVICE_TAGS = "ServiceTags";
  private static final String SERVICE_ADDRESS = "ServiceAddress";
  private static final String SERVICE_META = "ServiceMeta";
  private static final String SERVICE_PORT = "ServicePort";
  private static final String CREATE_INDEX = "CreateIndex";
  private static final String MODIFY_INDEX = "ModifyIndex";

  private String node;
  private String nodeAddress;
  private String id;
  private String name;
  private List<String> tags;
  private String address;
  private Map<String, String> meta;
  private int port;
  private long createIndex;
  private long modifyIndex;

  /**
   * Default constructor
   */
  public Service() {
  }

  /**
   * Copy constructor
   *
   * @param other the one to copy
   */
  public Service(Service other) {
    this.node = other.node;
    this.nodeAddress = other.nodeAddress;
    this.id = other.id;
    this.name = other.name;
    this.tags = other.tags;
    this.address = other.address;
    this.meta = other.meta;
    this.port = other.port;
    this.createIndex = other.createIndex;
    this.modifyIndex = other.modifyIndex;
  }

  /**
   * Constructor from JSON
   *
   * @param service the JSON
   */
  public Service(JsonObject service) {
    this.node = service.getString(NODE);
    this.nodeAddress = service.getString(ADDRESS);
    this.id = service.getString(SERVICE_ID);
    this.name = service.getString(SERVICE_NAME);
    this.tags = listOf(service.getJsonArray(SERVICE_TAGS));
    this.address = service.getString(SERVICE_ADDRESS);
    this.meta = mapStringString(service.getJsonObject(SERVICE_META));
    this.port = service.getInteger(SERVICE_PORT, 0);
    this.createIndex = service.getLong(CREATE_INDEX, 0l);
    this.modifyIndex = service.getLong(MODIFY_INDEX, 0l);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (node != null) {
      jsonObject.put(NODE, node);
    }
    if (nodeAddress != null) {
      jsonObject.put(ADDRESS, nodeAddress);
    }
    if (id != null) {
      jsonObject.put(SERVICE_ID, id);
    }
    if (name != null) {
      jsonObject.put(SERVICE_NAME, name);
    }
    if (tags != null) {
      jsonObject.put(SERVICE_TAGS, new JsonArray(tags));
    }
    if (address != null) {
      jsonObject.put(SERVICE_ADDRESS, address);
    }
    if (meta != null && !meta.isEmpty()) {
      jsonObject.put(SERVICE_META, meta);
    }
    if (port != 0) {
      jsonObject.put(SERVICE_PORT, port);
    }
    if (createIndex != 0l) {
      jsonObject.put(CREATE_INDEX, createIndex);
    }
    if (modifyIndex != 0l) {
      jsonObject.put(MODIFY_INDEX, modifyIndex);
    }
    return jsonObject;
  }

  /**
   * Get none name
   * @return node name
   */
  public String getNode() {
    return node;
  }

  /**
   * Set node name
   *
   * @param node node name
   * @return reference to this, for fluency
   */
  public Service setNode(String node) {
    this.node = node;
    return this;
  }

  /**
   * Get node address
   *
   * @return node address
   */
  public String getNodeAddress() {
    return nodeAddress;
  }

  /**
   * Set node address
   *
   * @param nodeAddress node address
   * @return reference to this, for fluency
   */
  public Service setNodeAddress(String nodeAddress) {
    this.nodeAddress = nodeAddress;
    return this;
  }

  /**
   * Get ID of service
   *
   * @return ID of service
   */
  public String getId() {
    return id;
  }

  /**
   * Set ID of service
   *
   * @param id ID of service
   * @return reference to this, for fluency
   */
  public Service setId(String id) {
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
  public Service setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Set list of service tags
   *
   * @return list of service tags
   */
  public List<String> getTags() {
    return tags;
  }

  /**
   * Set list of service tags
   *
   * @param tags list of service tags
   * @return reference to this, for fluency
   */
  public Service setTags(List<String> tags) {
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
  public Service setAddress(String address) {
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
  public Service setMeta(Map<String, String> meta) {
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
  public Service setPort(int port) {
    this.port = port;
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
  public Service setCreateIndex(long createIndex) {
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
  public Service setModifyIndex(long modifyIndex) {
    this.modifyIndex = modifyIndex;
    return this;
  }

  @Override
  public TxnOperationType getOperationType() {
    return TxnOperationType.SERVICE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Service service = (Service) o;

    if (createIndex != service.createIndex) return false;
    if (modifyIndex != service.modifyIndex) return false;
    if (port != service.port) return false;
    if (node != null ? !node.equals(service.node) : service.node != null) return false;
    if (nodeAddress != null ? !nodeAddress.equals(service.nodeAddress) : service.nodeAddress != null) return false;
    if (id != null ? !id.equals(service.id) : service.id != null) return false;
    if (name != null ? !name.equals(service.name) : service.name != null) return false;
    if (meta != null ? !meta.equals(service.meta) : service.meta != null) return false;
    if (tags != null ? !sortedTags().equals(service.sortedTags()) : service.tags != null) return false;
    return address != null ? address.equals(service.address) : service.address == null;
  }

  @Override
  public int hashCode() {
    int result = node != null ? node.hashCode() : 0;
    result = 31 * result + (nodeAddress != null ? nodeAddress.hashCode() : 0);
    result = 31 * result + (id != null ? id.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (tags != null ? sortedTags().hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (meta != null ? meta.hashCode() : 0);
    result = 31 * result + port;
    result = 31 * result + (int) (createIndex ^ (createIndex >>> 32));
    result = 31 * result + (int) (modifyIndex ^ (modifyIndex >>> 32));
    return result;
  }

  private List<String> sortedTags() {
    List<String> sorted = null;
    if (tags != null) {
      sorted = new ArrayList<>(tags);
      sorted.sort(String::compareTo);
    }
    return sorted;
  }

}
