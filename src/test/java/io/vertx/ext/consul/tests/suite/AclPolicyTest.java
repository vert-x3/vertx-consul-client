package io.vertx.ext.consul.tests.suite;

import io.vertx.ext.consul.tests.ConsulTestBase;
import io.vertx.ext.consul.policy.AclPolicy;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static io.vertx.ext.consul.tests.Utils.getAsync;

@RunWith(VertxUnitRunner.class)
public class AclPolicyTest extends ConsulTestBase {

  @Test
  public void lifecycle(TestContext tc) {
    List<AclPolicy> policies = getAsync(() -> masterClient.getAclPolicies());
    tc.assertEquals(3, policies.size());
    AclPolicy policy = new AclPolicy()
      .setName("created_policy")
      .setRules("key \"bar/\" { policy = \"read\" }");
    String createdId = getAsync(() -> masterClient.createAclPolicy(policy));
    List<AclPolicy> newPolicies = getAsync(() -> masterClient.getAclPolicies());
    tc.assertEquals(4, newPolicies.size());
    AclPolicy read = getAsync(() -> masterClient.readPolicy(createdId));
    tc.assertEquals(createdId, read.getId());
    tc.assertEquals(policy.getRules(), read.getRules());
    tc.assertEquals(policy.getName(), read.getName());

    AclPolicy updateOptions = new AclPolicy()
      .setName("updated_policy")
      .setRules("key \"bar/\" { policy = \"write\" }");
    AclPolicy updatedPolicy = getAsync(() -> masterClient.updatePolicy(createdId, updateOptions));
    tc.assertEquals(updateOptions.getName(), updatedPolicy.getName());
    tc.assertEquals(updateOptions.getRules(), updateOptions.getRules());
    AclPolicy readByName = getAsync(() -> masterClient.readPolicyByName(updateOptions.getName()));
    tc.assertEquals(createdId, readByName.getId());
    tc.assertEquals(updateOptions.getName(), readByName.getName());
    tc.assertEquals(updateOptions.getRules(), readByName.getRules());
    Boolean isDeleted = getAsync(() -> masterClient.deletePolicy(createdId));
    tc.assertTrue(isDeleted);
    List<AclPolicy> afterDeleting = getAsync(() -> masterClient.getAclPolicies());
    tc.assertEquals(3, afterDeleting.size());
  }
}
