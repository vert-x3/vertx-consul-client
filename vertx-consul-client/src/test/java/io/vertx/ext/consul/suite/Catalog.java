package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.Node;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Catalog extends ConsulTestBase {

  @Test
  public void datacenters() {
    List<String> datacenters = getAsync(h -> readClient.catalogDatacenters(h));
    assertEquals(datacenters.size(), 1);
    assertEquals(datacenters.get(0), dc);
  }

  @Test
  public void nodes() {
    List<Node> nodes = getAsync(h -> readClient.catalogNodes(h));
    assertEquals(nodes.size(), 1);
    Node node = nodes.get(0);
    assertEquals(node.getNode(), nodeName);
  }

}
