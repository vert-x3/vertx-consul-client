package io.vertx.ext.consul.v1;

import io.vertx.codegen.annotations.DataObject;

@DataObject(generateConverter = true)
public class WriteOptions {

  // Providing a datacenter overwrites the DC provided
  // by the Config
  private String datacenter;

  // Token is used to provide a per-request ACL token
  // which overrides the agent's default token.
  private String token;

  // RelayFactor is used in keyring operations to cause responses to be
  // relayed back to the sender through N other random nodes. Must be
  // a value from 0 to 5 (inclusive).
  private byte relayFactor;

  // ctx is an optional context pass through to the underlying HTTP
  // request layer. Use Context() and WithContext() to manage this.
//  ctx context.Context


  public String getDatacenter() {
    return datacenter;
  }

  public void setDatacenter(String datacenter) {
    this.datacenter = datacenter;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public byte getRelayFactor() {
    return relayFactor;
  }

  public void setRelayFactor(byte relayFactor) {
    this.relayFactor = relayFactor;
  }
}
