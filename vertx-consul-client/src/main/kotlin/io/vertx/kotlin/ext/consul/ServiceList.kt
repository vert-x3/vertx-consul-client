package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ServiceList

fun ServiceList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.Service>? = null): ServiceList = io.vertx.ext.consul.ServiceList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

