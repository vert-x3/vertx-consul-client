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
package io.vertx.ext.consul.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.*;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public abstract class WatchImpl<T> implements Watch<T> {

  private static final String BLOCKING_WAIT = "10m";
  private static final int DELAY_LIMIT_SECONDS = 180;

  public static class Key extends WatchImpl<KeyValue> {

    private final String key;

    public Key(String key, Vertx vertx, ConsulClientOptions options) {
      super(vertx, ConsulClient.create(vertx, options));
      this.key = key;
    }

    @Override
    void wait(long index, Handler<AsyncResult<State<KeyValue>>> handler) {
      BlockingQueryOptions options = new BlockingQueryOptions().setWait(BLOCKING_WAIT).setIndex(index);
      consulClient.getValueWithOptions(key, options, h ->
        handler.handle(h.map(kv -> new State<KeyValue>(kv, kv.getModifyIndex()))));
    }
  }

  public static class KeyPrefix extends WatchImpl<KeyValueList> {

    private final String keyPrefix;

    public KeyPrefix(String keyPrefix, Vertx vertx, ConsulClientOptions options) {
      super(vertx, ConsulClient.create(vertx, options));
      this.keyPrefix = keyPrefix;
    }

    @Override
    void wait(long index, Handler<AsyncResult<State<KeyValueList>>> handler) {
      BlockingQueryOptions options = new BlockingQueryOptions().setWait(BLOCKING_WAIT).setIndex(index);
      consulClient.getValuesWithOptions(keyPrefix, options, h ->
        handler.handle(h.map(kv -> new State<KeyValueList>(kv, kv.getIndex()))));
    }
  }

  public static class Services extends WatchImpl<ServiceList> {

    public Services(Vertx vertx, ConsulClientOptions options) {
      super(vertx, ConsulClient.create(vertx, options));
    }

    @Override
    void wait(long index, Handler<AsyncResult<State<ServiceList>>> handler) {
      BlockingQueryOptions options = new BlockingQueryOptions().setWait(BLOCKING_WAIT).setIndex(index);
      consulClient.catalogServicesWithOptions(options, h ->
        handler.handle(h.map(services -> new State<ServiceList>(services, services.getIndex()))));
    }
  }

  public static class Service extends WatchImpl<ServiceEntryList> {

    private final String service;

    public Service(String service, Vertx vertx, ConsulClientOptions options) {
      super(vertx, ConsulClient.create(vertx, options));
      this.service = service;
    }

    @Override
    void wait(long index, Handler<AsyncResult<State<ServiceEntryList>>> handler) {
      BlockingQueryOptions bOpts = new BlockingQueryOptions().setWait(BLOCKING_WAIT).setIndex(index);
      ServiceQueryOptions sOpts = new ServiceQueryOptions().setBlockingOptions(bOpts);
      consulClient.healthServiceNodesWithOptions(service, false, sOpts, h ->
        handler.handle(h.map(services -> new State<ServiceEntryList>(services, services.getIndex()))));
    }
  }

  public static class Events extends WatchImpl<EventList> {

    private final String event;

    public Events(String event, Vertx vertx, ConsulClientOptions options) {
      super(vertx, ConsulClient.create(vertx, options));
      this.event = event;
    }

    @Override
    void wait(long index, Handler<AsyncResult<State<EventList>>> handler) {
      BlockingQueryOptions bOpts = new BlockingQueryOptions().setWait(BLOCKING_WAIT).setIndex(index);
      EventListOptions eOpts = new EventListOptions().setBlockingOptions(bOpts).setName(event);
      consulClient.listEventsWithOptions(eOpts, h ->
        handler.handle(h.map(events -> new State<EventList>(events, events.getIndex()))));
    }
  }

  public static class Nodes extends WatchImpl<NodeList> {

    public Nodes(Vertx vertx, ConsulClientOptions options) {
      super(vertx, ConsulClient.create(vertx, options));
    }

    @Override
    void wait(long index, Handler<AsyncResult<State<NodeList>>> handler) {
      BlockingQueryOptions bOpts = new BlockingQueryOptions().setWait(BLOCKING_WAIT).setIndex(index);
      NodeQueryOptions qOpts = new NodeQueryOptions().setBlockingOptions(bOpts);
      consulClient.catalogNodesWithOptions(qOpts, h ->
        handler.handle(h.map(nodes -> new State<NodeList>(nodes, nodes.getIndex()))));
    }
  }

  private volatile boolean started = false;
  private volatile boolean stopped = false;
  private Handler<WatchResult<T>> handler;
  private State<T> current = new State<>(null, 0);

  protected final Vertx vertx;
  protected final ConsulClient consulClient;

  private WatchImpl(Vertx vertx, ConsulClient consulClient) {
    this.vertx = vertx;
    this.consulClient = consulClient;
  }

  abstract void wait(long index, Handler<AsyncResult<State<T>>> handler);

  @Override
  public Watch<T> setHandler(Handler<WatchResult<T>> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public synchronized Watch<T> start() {
    if (!started) {
      started = true;
      vertx.runOnContext(v -> go());
    } else {
      throw new IllegalStateException("Watch already started");
    }
    return this;
  }

  @Override
  public synchronized void stop() {
    if (!started) {
      throw new IllegalStateException("An unstarted watch");
    }
    if (stopped) {
      throw new IllegalStateException("Watch already stopped");
    }
    stopped = true;
    vertx.runOnContext(v -> consulClient.close());
  }

  private void go() {
    fetch(0, newState -> {
      if (newState.equals(current)) {
        // avoid floods
        vertx.setTimer(1000, l -> go());
      } else {
        State<T> prevState = current;
        current = newState;
        sendSuccess(prevState.value, newState.value);
        vertx.runOnContext(v -> go());
      }
    });
  }

  private void fetch(long cnt, Handler<State<T>> result) {
    if (stopped) {
      return;
    }
    wait(current.index, h -> {
      if (stopped) {
        return;
      }
      if (h.succeeded()) {
        result.handle(h.result());
      } else {
        sendFail(current.value, h.cause());
        long newCnt = cnt + 1;
        long delay = newCnt * newCnt;
        if (delay > DELAY_LIMIT_SECONDS) {
          delay = DELAY_LIMIT_SECONDS;
        }
        vertx.setTimer(delay * 1000, l -> fetch(newCnt, result));
      }
    });
  }

  private void sendSuccess(T prevValue, T nextValue) {
    if (!stopped && handler != null) {
      handler.handle(new WatchResult<T>() {
        @Override
        public T prevResult() {
          return prevValue;
        }

        @Override
        public T nextResult() {
          return nextValue;
        }

        @Override
        public Throwable cause() {
          return null;
        }

        @Override
        public boolean succeeded() {
          return true;
        }

        @Override
        public boolean failed() {
          return false;
        }
      });
    }
  }

  private void sendFail(T prevValue, Throwable cause) {
    if (!stopped && handler != null) {
      handler.handle(new WatchResult<T>() {
        @Override
        public T prevResult() {
          return prevValue;
        }

        @Override
        public T nextResult() {
          return null;
        }

        @Override
        public Throwable cause() {
          return cause;
        }

        @Override
        public boolean succeeded() {
          return false;
        }

        @Override
        public boolean failed() {
          return true;
        }
      });
    }
  }

  static class State<T> {

    final T value;
    final long index;

    State(T v, long i) {
      value = v;
      index = i;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State<?> state = (State<?>) o;
      return index == state.index && (value != null ? value.equals(state.value) : state.value == null);
    }

    @Override
    public int hashCode() {
      int result = value != null ? value.hashCode() : 0;
      result = 31 * result + (int) (index ^ (index >>> 32));
      return result;
    }
  }
}
