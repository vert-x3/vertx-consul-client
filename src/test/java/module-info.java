open module io.vertx.consul.client.tests {
  requires consul;
  requires com.github.dockerjava.api;
  requires com.fasterxml.jackson.annotation;
  requires io.vertx.core;
  requires io.vertx.consul.client;
  requires io.vertx.core.tests;
  requires io.vertx.testing.unit;
  requires junit;
  requires hamcrest.core;
  requires org.slf4j;
  requires testcontainers;
}
