/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.impl.WatchImpl;

/**
 * Watches are a way of specifying a view of data (e.g. list of nodes, KV pairs, health checks)
 * which is monitored for updates. When an update is detected, an {@code Handler} with {@code AsyncResult} is invoked.
 * As an example, you could watch the status of health checks and notify when a check is critical.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@VertxGen
public interface Watch<T> {

  /**
   * Creates {@code Watch} to monitoring a specific key in the KV store.
   * The underlying Consul client will be created with default options.
   * This maps to the <a href="https://www.consul.io/api/kv.html">/v1/kv/</a> API internally.
   *
   * @param key the key
   * @param vertx the {@code Vertx} instance
   * @return the {@code Watch} instance
   */
  static Watch<KeyValue> key(String key, Vertx vertx) {
    return key(key, vertx, new ConsulClientOptions());
  }

  /**
   * Creates {@code Watch} to monitoring a specific key in the KV store.
   * This maps to the <a href="https://www.consul.io/api/kv.html">/v1/kv/</a> API internally.
   *
   * @param key the key
   * @param vertx the {@code Vertx} instance
   * @param options the options to create underlying Consul client
   * @return the {@code Watch} instance
   */
  static Watch<KeyValue> key(String key, Vertx vertx, ConsulClientOptions options) {
    return new WatchImpl.Key(key, vertx, options);
  }

  /**
   * Creates {@code Watch} to monitoring a prefix of keys in the KV store.
   * The underlying Consul client will be created with default options.
   * This maps to the <a href="https://www.consul.io/api/kv.html">/v1/kv/</a> API internally.
   *
   * @param keyPrefix the key
   * @param vertx the {@code Vertx} instance
   * @return the {@code Watch} instance
   */
  static Watch<KeyValueList> keyPrefix(String keyPrefix, Vertx vertx) {
    return keyPrefix(keyPrefix, vertx, new ConsulClientOptions());
  }

  /**
   * Creates {@code Watch} to monitoring a prefix of keys in the KV store.
   * This maps to the <a href="https://www.consul.io/api/kv.html">/v1/kv/</a> API internally.
   *
   * @param keyPrefix the key
   * @param vertx the {@code Vertx} instance
   * @param options the options to create underlying Consul client
   * @return the {@code Watch} instance
   */
  static Watch<KeyValueList> keyPrefix(String keyPrefix, Vertx vertx, ConsulClientOptions options) {
    return new WatchImpl.KeyPrefix(keyPrefix, vertx, options);
  }

  /**
   * Creates {@code Watch} to monitoring the list of available services.
   * The underlying Consul client will be created with default options.
   * This maps to the <a href="https://www.consul.io/docs/agent/http/catalog.html#catalog_services">/v1/catalog/services</a> API internally.
   *
   * @param vertx the {@code Vertx} instance
   * @return the {@code Watch} instance
   */
  static Watch<ServiceList> services(Vertx vertx) {
    return services(vertx, new ConsulClientOptions());
  }

  /**
   * Creates {@code Watch} to monitoring the list of available services.
   * This maps to the <a href="https://www.consul.io/docs/agent/http/catalog.html#catalog_services">/v1/catalog/services</a> API internally.
   *
   * @param vertx the {@code Vertx} instance
   * @param options the options to create underlying Consul client
   * @return the {@code Watch} instance
   */
  static Watch<ServiceList> services(Vertx vertx, ConsulClientOptions options) {
    return new WatchImpl.Services(vertx, options);
  }

  /**
   * Creates {@code Watch} to monitoring the nodes providing the service.
   * The underlying Consul client will be created with default options.
   * This maps to the <a href="https://www.consul.io/docs/agent/http/health.html#health_service">/v1/health/service/&lt;service&gt;</a> API internally.
   *
   * @param service the service name
   * @param vertx the {@code Vertx} instance
   * @return the {@code Watch} instance
   */
  static Watch<ServiceEntryList> service(String service, Vertx vertx) {
    return service(service, vertx, new ConsulClientOptions());
  }

  /**
   * Creates {@code Watch} to monitoring the nodes providing the service.
   * This maps to the <a href="https://www.consul.io/docs/agent/http/health.html#health_service">/v1/health/service/&lt;service&gt;</a> API internally.
   *
   * @param service the service name
   * @param vertx the {@code Vertx} instance
   * @param options the options to create underlying Consul client
   * @return the {@code Watch} instance
   */
  static Watch<ServiceEntryList> service(String service, Vertx vertx, ConsulClientOptions options) {
    return new WatchImpl.Service(service, vertx, options);
  }

  /**
   * Creates {@code Watch} to monitoring the custom user events.
   * The underlying Consul client will be created with default options.
   * This maps to the <a href="https://www.consul.io/docs/agent/http/event.html#event_list">/v1/event/list</a> API internally.
   *
   * @param event the event name
   * @param vertx the {@code Vertx} instance
   * @return the {@code Watch} instance
   */
  static Watch<EventList> events(String event, Vertx vertx) {
    return events(event, vertx, new ConsulClientOptions());
  }

  /**
   * Creates {@code Watch} to monitoring the custom user events.
   * This maps to the <a href="https://www.consul.io/docs/agent/http/event.html#event_list">/v1/event/list</a> API internally.
   *
   * @param event the event name
   * @param vertx the {@code Vertx} instance
   * @param options the options to create underlying Consul client
   * @return the {@code Watch} instance
   */
  static Watch<EventList> events(String event, Vertx vertx, ConsulClientOptions options) {
    return new WatchImpl.Events(event, vertx, options);
  }

  /**
   * Creates {@code Watch} to monitoring the list of available nodes.
   * The underlying Consul client will be created with default options.
   * This maps to the <a href="https://www.consul.io/api/catalog.html#list-nodes">/v1/catalog/nodes</a> API internally.
   *
   * @param vertx the {@code Vertx} instance
   * @return the {@code Watch} instance
   */
  static Watch<NodeList> nodes(Vertx vertx) {
    return new WatchImpl.Nodes(vertx, new ConsulClientOptions());
  }

  /**
   * Creates {@code Watch} to monitoring the list of available nodes.
   * This maps to the <a href="https://www.consul.io/api/catalog.html#list-nodes">/v1/catalog/nodes</a> API internally.
   *
   * @param vertx the {@code Vertx} instance
   * @param options the options to create underlying Consul client
   * @return the {@code Watch} instance
   */
  static Watch<NodeList> nodes(Vertx vertx, ConsulClientOptions options) {
    return new WatchImpl.Nodes(vertx, options);
  }

  /**
   * Set the result handler. As data is changed, the handler will be called with the result.
   *
   * @param handler the result handler
   * @return reference to this, for fluency
   */
  @Fluent
  Watch<T> setHandler(Handler<WatchResult<T>> handler);

  /**
   * Start this {@code Watch}
   *
   * @return reference to this, for fluency
   */
  @Fluent
  Watch<T> start();

  /**
   * Stop the watch and release its resources
   */
  void stop();

}
