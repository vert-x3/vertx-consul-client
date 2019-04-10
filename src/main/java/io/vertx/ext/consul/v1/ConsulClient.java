package io.vertx.ext.consul.v1;


import io.vertx.ext.consul.v1.acl.ACL;
import io.vertx.ext.consul.v1.agent.Agent;
import io.vertx.ext.consul.v1.kv.KV;

/**
 * A client for Consul HTTP API.
 */
public interface ConsulClient {

  /**
   * Get the ACL object. Only a single instance of {@link ACL} per {@link ConsulClient} instance.
   *
   * @return the ACL object
   */
  ACL acl();

  /**
   * Get the KV object. Only a single instance of {@link KV} per {@link ConsulClient} instance.
   *
   * @return the KV object
   */
  KV kv();

  Agent agent();

  Catalog catalog();

  Connect connect();

  Coordinate coordinate();

  Event event();

  Health health();

  Operator operator();

  Query query();

  Session session();

  Snapshot snapshot();

  Status status();

  TXN txn();

}
