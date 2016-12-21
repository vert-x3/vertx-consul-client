package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.EventList

fun EventList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.Event>? = null): EventList = io.vertx.ext.consul.EventList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

