package io.vertx.ext.consul.v1.impl;

import io.vertx.ext.consul.v1.WriteOptions;

public class WriteOptionsImpl implements WriteOptions {

  private String datacenter;
  private String token;
  private byte relayFactor;

  @Override
  public String datacenter() {
    return datacenter;
  }

  @Override
  public String token() {
    return token;
  }

  @Override
  public byte relayFactor() {
    return relayFactor;
  }

  @Override
  public WriteOptions setDatacenter(String datacenter) {
    this.datacenter = datacenter;
    return this;
  }

  @Override
  public WriteOptions setToken(String token) {
    this.token = token;
    return this;
  }

  @Override
  public WriteOptions setRelayFactor(byte relayFactor) {
    this.relayFactor = relayFactor;
    return this;
  }
}
