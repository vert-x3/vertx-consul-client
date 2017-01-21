package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ConsulClientOptions

fun ConsulClientOptions(
    aclToken: String? = null,
  dc: String? = null,
  host: String? = null,
  port: Int? = null,
  timeoutMs: Long? = null): ConsulClientOptions = io.vertx.ext.consul.ConsulClientOptions().apply {

  if (aclToken != null) {
    this.aclToken = aclToken
  }

  if (dc != null) {
    this.dc = dc
  }

  if (host != null) {
    this.host = host
  }

  if (port != null) {
    this.port = port
  }

  if (timeoutMs != null) {
    this.timeoutMs = timeoutMs
  }

}

