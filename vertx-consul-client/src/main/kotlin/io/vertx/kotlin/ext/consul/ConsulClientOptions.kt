package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ConsulClientOptions

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
    this.aclToken = aclToken
  }

  if (dc != null) {
    this.dc = dc
  }

  if (host != null) {
    this.host = host
  }

  if (pemTrustOptions != null) {
    this.pemTrustOptions = pemTrustOptions
  }

  if (port != null) {
    this.port = port
  }

  if (ssl != null) {
    this.isSsl = ssl
  }

  if (timeoutMs != null) {
    this.timeoutMs = timeoutMs
  }

  if (trustAll != null) {
    this.isTrustAll = trustAll
  }

}

