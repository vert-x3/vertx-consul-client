package io.vertx.ext.consul;

/**
 * Holds the type of Service operation in transaction
 */
public enum TxnServiceVerb {

  /**
   * Sets the service to the given state
   */
  SET("set"),

  /**
   * Sets, but with CAS semantics using the given ModifyIndex
   */
  CAS("cas"),

  /**
   * 	Get the service, fails if it does not exist
   */
  GET("get"),

  /**
   * Delete the service
   */
  DELETE("delete"),

  /**
   * Delete, but with CAS semantics
   */
  DELETE_CAS("delete-cas");

  public static TxnServiceVerb ofVerb(String verb) {
    for (TxnServiceVerb type : values()) {
      if (type.getVerb().equals(verb)) {
        return type;
      }
    }
    return null;
  }

  private final String verb;

  TxnServiceVerb(String verb) {
    this.verb = verb;
  }

  public String getVerb() {
    return verb;
  }

}
