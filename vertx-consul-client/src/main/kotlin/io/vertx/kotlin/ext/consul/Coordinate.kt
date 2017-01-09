package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.Coordinate

fun Coordinate(
    adj: Float? = null,
  err: Float? = null,
  height: Float? = null,
  node: String? = null,
  vec: List<Float>? = null): Coordinate = io.vertx.ext.consul.Coordinate().apply {

  if (adj != null) {
    this.adj = adj
  }

  if (err != null) {
    this.err = err
  }

  if (height != null) {
    this.height = height
  }

  if (node != null) {
    this.node = node
  }

  if (vec != null) {
    this.vec = vec
  }

}

