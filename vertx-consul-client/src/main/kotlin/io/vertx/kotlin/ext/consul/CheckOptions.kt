package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.CheckOptions
import io.vertx.ext.consul.CheckStatus

/**
 * A function providing a DSL for building [io.vertx.ext.consul.CheckOptions] objects.
 *
 * Options used to register checks in Consul.
 *
 * @param http  Set HTTP address to check. Also you should set checking interval
 * @param id  Set check ID
 * @param interval  Set checking interval
 * @param name  Set check name. This is mandatory field
 * @param notes  Set check notes
 * @param script  Set path to checking script. Also you should set checking interval
 * @param serviceId  Set the service ID to associate the registered check with an existing service provided by the agent.
 * @param status  Set the check status to specify the initial state of the health check.
 * @param tcp  Set TCP address to check. Also you should set checking interval
 * @param ttl  Set Time to Live of check.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.consul.CheckOptions original] using Vert.x codegen.
 */
fun CheckOptions(
  http: String? = null,
  id: String? = null,
  interval: String? = null,
  name: String? = null,
  notes: String? = null,
  script: String? = null,
  serviceId: String? = null,
  status: CheckStatus? = null,
  tcp: String? = null,
  ttl: String? = null): CheckOptions = io.vertx.ext.consul.CheckOptions().apply {

  if (http != null) {
    this.setHttp(http)
  }
  if (id != null) {
    this.setId(id)
  }
  if (interval != null) {
    this.setInterval(interval)
  }
  if (name != null) {
    this.setName(name)
  }
  if (notes != null) {
    this.setNotes(notes)
  }
  if (script != null) {
    this.setScript(script)
  }
  if (serviceId != null) {
    this.setServiceId(serviceId)
  }
  if (status != null) {
    this.setStatus(status)
  }
  if (tcp != null) {
    this.setTcp(tcp)
  }
  if (ttl != null) {
    this.setTtl(ttl)
  }
}

