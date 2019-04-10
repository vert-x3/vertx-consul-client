package io.vertx.ext.consul.v1.agent;


public interface Agent {

  Checks checks();

  Check check();

  Service service();

  Services services();

  Connect connect();

}
