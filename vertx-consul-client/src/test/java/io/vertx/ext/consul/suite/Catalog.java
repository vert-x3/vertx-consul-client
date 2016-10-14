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
        assertEquals(1, datacenters.size());
        assertEquals(dc, datacenters.get(0));
    }

    @Test
    public void nodes() {
        List<Node> nodes = getAsync(h -> readClient.catalogNodes(h));
        assertEquals(3, nodes.size());
        Optional<Node> node = nodes.stream().filter(n -> n.getNode().equals(nodeName)).findFirst();
        assertTrue(node.isPresent());
    }

}
