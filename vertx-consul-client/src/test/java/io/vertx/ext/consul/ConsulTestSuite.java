package io.vertx.ext.consul;

import io.vertx.ext.consul.suite.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  AgentInfo.class,
  AclTokens.class,
  Catalog.class,
  Checks.class,
  Coordinates.class,
  Events.class,
  KVStore.class,
  Services.class,
  Sessions.class,
  Status.class
})
public class ConsulTestSuite {

  @BeforeClass
  public static void startConsul() throws Exception {
    ConsulCluster.consul();
  }

  @AfterClass
  public static void stopConsul() {
    ConsulCluster.close();
  }

}
