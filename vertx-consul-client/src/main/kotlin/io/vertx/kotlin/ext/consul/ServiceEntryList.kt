package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.ServiceEntryList

fun ServiceEntryList(
    index: Long? = null,
  list: List<io.vertx.ext.consul.ServiceEntry>? = null): ServiceEntryList = io.vertx.ext.consul.ServiceEntryList().apply {

  if (index != null) {
    this.index = index
  }

  if (list != null) {
    this.list = list
  }

}

