package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ConsulClientOptions
import io.vertx.core.net.PemTrustOptions

/**
 * A function providing a DSL for building [io.vertx.ext.consul.ConsulClientOptions] objects.
 *
 * Options used to create Consul client.
 *
 * @param aclToken  Set the ACL token. When provided, the client will use this token when making requests to the Consul by providing the "?token" query parameter. When not provided, the empty token, which maps to the 'anonymous' ACL policy, is used.
 * @param dc  Set the datacenter name. When provided, the client will use it when making requests to the Consul by providing the "?dc" query parameter. When not provided, the datacenter of the consul agent is queried.
 * @param host  Set Consul host. Defaults to `localhost`
 * @param pemTrustOptions  Set the trust options.
 * @param port  Set Consul HTTP API port. Defaults to `8500`
 * @param ssl  Set whether SSL/TLS is enabled
 * @param timeoutMs  Sets the amount of time (in milliseconds) after which if the request does not return any data within the timeout period an failure will be passed to the handler and the request will be closed.
 * @param trustAll  Set whether all server certificates should be trusted
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.consul.ConsulClientOptions original] using Vert.x codegen.
 */
fun ConsulClientOptions(
  aclToken: String? = null,
  dc: String? = null,
  host: String? = null,
  pemTrustOptions: io.vertx.core.net.PemTrustOptions? = null,
  port: Int? = null,
  ssl: Boolean? = null,
  timeoutMs: Long? = null,
  trustAll: Boolean? = null): ConsulClientOptions = io.vertx.ext.consul.ConsulClientOptions().apply {

  if (aclToken != null) {
    this.setAclToken(aclToken)
  }
  if (dc != null) {
    this.setDc(dc)
  }
  if (host != null) {
    this.setHost(host)
  }
  if (pemTrustOptions != null) {
    this.setPemTrustOptions(pemTrustOptions)
  }
  if (port != null) {
    this.setPort(port)
  }
  if (ssl != null) {
    this.setSsl(ssl)
  }
  if (timeoutMs != null) {
    this.setTimeoutMs(timeoutMs)
  }
  if (trustAll != null) {
    this.setTrustAll(trustAll)
  }
}

