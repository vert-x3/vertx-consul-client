package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.codegen.json.annotations.JsonGen;

/**
 * Holds the operation to apply to the service inside a transaction
 */
@DataObject
@JsonGen(publicConverter = false)
public class TxnServiceOperation implements TxnOperation {

  private TxnServiceVerb type;
  private String node;
  private ServiceOptions serviceOptions;

  /**
   * Default constructor
   */
  public
  TxnServiceOperation() {

  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public TxnServiceOperation(JsonObject json) {
    TxnServiceOperationConverter.fromJson(json, this);
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    TxnServiceOperationConverter.toJson(this, jsonObject);
    return jsonObject;
  }

  /**
   * Get the type of operation to perform
   *
   * @return the type of operation to perform
   */
  public TxnServiceVerb getType() {
    return type;
  }

  /**
   * Get the node
   *
   * @return the node name
   */
  public String getNode() { return node; }

  /**
   * Get the service
   *
   * @return the service
   */
  public ServiceOptions getServiceOptions() { return serviceOptions; }

  /**
   * Set the type of operation to perform
   *
   * @param type the type of operation to perform
   * @return reference to this, for fluency
   */
  public TxnServiceOperation setType(TxnServiceVerb type) {
    this.type = type;
    return this;
  }

  /**
   * Set the node
   *
   * @param node
   * @return reference to this, for fluency
   */
  public TxnServiceOperation setNode(String node) {
    this.node = node;
    return this;
  }

  /**
   * Set the service
   *
   * @param serviceOptions
   * @return  reference to this, for fluency
   */
  public TxnServiceOperation setServiceOptions(ServiceOptions serviceOptions) {
    this.serviceOptions = serviceOptions;
    return this;
  }

  @GenIgnore
  @Override
  public TxnOperationType getOperationType() {
    return TxnOperationType.SERVICE;
  }

}
