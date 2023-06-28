package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.ConsulTestBase;
import io.vertx.ext.consul.policy.AclPolicy;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;

@RunWith(VertxUnitRunner.class)
public class AclPolicyTest extends ConsulTestBase {

  @Test
  public void lifecycle(TestContext tc) {
    List<AclPolicy> policies = getAsync(h -> masterClient.getAclPolicies(h));
    tc.assertEquals(3, policies.size());
    AclPolicy policy = new AclPolicy()
      .setName("created_policy")
      .setRules("key \"bar/\" { policy = \"read\" }");
    String createdId = getAsync(h -> masterClient.createAclPolicy(policy, h));
    List<AclPolicy> newPolicies = getAsync(h -> masterClient.getAclPolicies(h));
    tc.assertEquals(4, newPolicies.size());
    AclPolicy read = getAsync(h -> masterClient.readPolicy(createdId, h));
    tc.assertEquals(createdId, read.getId());
    tc.assertEquals(policy.getRules(), read.getRules());
    tc.assertEquals(policy.getName(), read.getName());

    AclPolicy updateOptions = new AclPolicy()
      .setName("updated_policy")
      .setRules("key \"bar/\" { policy = \"write\" }");
    AclPolicy updatedPolicy = getAsync(h -> masterClient.updatePolicy(createdId, updateOptions, h));
    tc.assertEquals(updateOptions.getName(), updatedPolicy.getName());
    tc.assertEquals(updateOptions.getRules(), updateOptions.getRules());
    AclPolicy readByName = getAsync(h -> masterClient.readPolicyByName(updateOptions.getName(), h));
    tc.assertEquals(createdId, readByName.getId());
    tc.assertEquals(updateOptions.getName(), readByName.getName());
    tc.assertEquals(updateOptions.getRules(), readByName.getRules());
    Boolean isDeleted = getAsync(h -> masterClient.deletePolicy(createdId, h));
    tc.assertTrue(isDeleted);
    List<AclPolicy> afterDeleting = getAsync(h -> masterClient.getAclPolicies(h));
    tc.assertEquals(3, afterDeleting.size());
  }
}
