package io.vertx.kotlin.ext.consul

import io.vertx.ext.consul.SessionOptions
import io.vertx.ext.consul.SessionBehavior

/**
 * A function providing a DSL for building [io.vertx.ext.consul.SessionOptions] objects.
 *
 * Options used to create session.
 *
 * @param behavior  Set the behavior when a session is invalidated. The release behavior is the default if none is specified.
 * @param checks  Set a list of associated health checks. It is highly recommended that, if you override this list, you include the default "serfHealth"
 * @param lockDelay  Set the lock-delay period.
 * @param name  Set the human-readable name for the Session
 * @param node  Set the node to which the session will be assigned
 * @param ttl  Set the TTL interval. When TTL interval expires without being renewed, the session has expired and an invalidation is triggered. If specified, it must be between 10s and 86400s currently.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.consul.SessionOptions original] using Vert.x codegen.
 */
fun SessionOptions(
  behavior: SessionBehavior? = null,
  checks: List<String>? = null,
  lockDelay: Long? = null,
  name: String? = null,
  node: String? = null,
  ttl: Long? = null): SessionOptions = io.vertx.ext.consul.SessionOptions().apply {

  if (behavior != null) {
    this.setBehavior(behavior)
  }
  if (checks != null) {
    this.setChecks(checks)
  }
  if (lockDelay != null) {
    this.setLockDelay(lockDelay)
  }
  if (name != null) {
    this.setName(name)
  }
  if (node != null) {
    this.setNode(node)
  }
  if (ttl != null) {
    this.setTtl(ttl)
  }
}

