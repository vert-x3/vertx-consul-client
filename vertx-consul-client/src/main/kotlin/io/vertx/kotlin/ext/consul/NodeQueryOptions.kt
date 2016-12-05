package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.NodeQueryOptions

fun NodeQueryOptions(
    blockingOptions: io.vertx.ext.consul.BlockingQueryOptions? = null,
  near: String? = null): NodeQueryOptions = io.vertx.ext.consul.NodeQueryOptions().apply {

  if (blockingOptions != null) {
    this.blockingOptions = blockingOptions
  }

  if (near != null) {
    this.near = near
  }

}

