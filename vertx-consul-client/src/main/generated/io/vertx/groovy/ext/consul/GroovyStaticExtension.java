package io.vertx.groovy.ext.consul;
public class GroovyStaticExtension {
  public static io.vertx.ext.consul.ConsulClient create(io.vertx.ext.consul.ConsulClient j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> config) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.consul.ConsulClient.create(vertx,
      config != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(config) : null));
  }
}
