package io.vertx.ext.consul.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.connect.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectOptionsTest {

  @Test
  public void parse() throws Exception {
    JsonObject src = new JsonObject(Utils.readResource("connect_example.json"));
    SidecarServiceOptions scOpts = new ConnectOptions(src).getSidecarService();
    assertEquals(33333, scOpts.getPort());
    ProxyOptions proxyOptions = scOpts.getProxy();
    assertEquals(1, proxyOptions.getUpstreams().size());
    UpstreamOptions upstream = proxyOptions.getUpstreams().get(0);
    assertEquals("dev-mesh-database-service", upstream.getDestinationName());
    assertEquals(19102, upstream.getLocalBindPort());
    assertEquals("dc-v5", upstream.getDc());
    JsonObject config = proxyOptions.getConfig();
    assertEquals("envoy_local_cluster_json1", config.getString("envoy_local_cluster_json"));
    assertEquals("envoy_local_cluster_json2", config.getString("envoy_public_listener_json"));
    assertEquals("0.0.0.0:19500", config.getString("envoy_prometheus_bind_addr"));
    assertEquals("envoy_local_cluster_json3", config.getString("envoy_extra_static_clusters_json"));
    ExposeOptions expOptions = proxyOptions.getExpose();
    assertEquals(1, expOptions.getPaths().size());
    ExposePathOptions path = expOptions.getPaths().get(0);
    assertEquals("/metrics", path.getPath());
    assertEquals("http", path.getProtocol());
    assertEquals((Integer) 53000, path.getLocalPathPort());
    assertEquals((Integer) 19600, path.getListenerPort());
  }

  @Test
  public void convert() throws Exception {
    JsonObject src = new JsonObject(Utils.readResource("connect_example.json"));
    ConnectOptions options = new ConnectOptions(src);
    JsonObject act = new JsonObject(options.toJson().encode());
    assertEquals(src, act);
  }
}
