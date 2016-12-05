package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ServiceQueryOptions

fun ServiceQueryOptions(
    blockingOptions: io.vertx.ext.consul.BlockingQueryOptions? = null,
  near: String? = null,
  tag: String? = null): ServiceQueryOptions = io.vertx.ext.consul.ServiceQueryOptions().apply {

  if (blockingOptions != null) {
    this.blockingOptions = blockingOptions
  }

  if (near != null) {
    this.near = near
  }

  if (tag != null) {
    this.tag = tag
  }

}

