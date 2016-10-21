package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class Status extends ConsulTestBase {

  @Test
  public void leader() {
    String leader = getAsync(h -> readClient.leaderStatus(h));
    assertEquals(leader.substring(0, leader.indexOf(':')), "127.0.0.1");
  }

  @Test
  public void peers() {
    List<String> peers = getAsync(h -> readClient.peersStatus(h));
    assertEquals(peers.size(), 1);
  }

}
