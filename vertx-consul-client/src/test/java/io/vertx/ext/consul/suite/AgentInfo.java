package io.vertx.ext.consul.suite;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import static io.vertx.ext.consul.Utils.getAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class AgentInfo extends ConsulTestBase {

  @Test
  public void info() {
    JsonObject info = getAsync(h -> readClient.agentInfo(h));
    JsonObject config = info.getJsonObject("Config");
    assertEquals(config.getString("Datacenter"), dc);
  }
}
