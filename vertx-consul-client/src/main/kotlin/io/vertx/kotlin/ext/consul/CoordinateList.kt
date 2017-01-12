package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.CoordinateList

fun CoordinateList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.Coordinate>? = null): CoordinateList = io.vertx.ext.consul.CoordinateList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

