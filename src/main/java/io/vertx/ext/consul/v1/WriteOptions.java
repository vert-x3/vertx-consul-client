package io.vertx.ext.consul.v1;

import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public interface WriteOptions {

  // Providing a datacenter overwrites the DC provided
  // by the Config
  String datacenter();

  // Token is used to provide a per-request ACL token
  // which overrides the agent's default token.
  String token();

  // RelayFactor is used in keyring operations to cause responses to be
  // relayed back to the sender through N other random nodes. Must be
  // a value from 0 to 5 (inclusive).
  byte relayFactor();

  WriteOptions setDatacenter(String datacenter);

  WriteOptions setToken(String token);

  WriteOptions setRelayFactor(byte relayFactor);
}
