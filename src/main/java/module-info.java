module io.vertx.consul.client {

  requires static io.vertx.docgen;
  requires static io.vertx.codegen.api;
  requires static io.vertx.codegen.json;

  requires io.netty.codec.http;
  requires io.vertx.core;
  requires io.vertx.web.client;

  exports io.vertx.ext.consul;
  exports io.vertx.ext.consul.connect;
  exports io.vertx.ext.consul.policy;
  exports io.vertx.ext.consul.token;

  exports io.vertx.ext.consul.impl to io.vertx.consul.client.tests;

}
