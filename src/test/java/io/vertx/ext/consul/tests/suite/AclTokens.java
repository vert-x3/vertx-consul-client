/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul.tests.suite;

import io.vertx.ext.consul.tests.ConsulTestBase;
import io.vertx.ext.consul.policy.AclPolicy;
import io.vertx.ext.consul.token.AclToken;
import io.vertx.ext.consul.token.CloneAclTokenOptions;
import io.vertx.ext.consul.token.PolicyLink;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@RunWith(VertxUnitRunner.class)
public class AclTokens extends ConsulTestBase {

  @Test
  public void lifecycle(TestContext tc) {
    masterClient.getAclTokens().onComplete(tc.asyncAssertSuccess(backupTokens -> {
      AclPolicy policy = new AclPolicy()
        .setName("tokenName")
        .setRules("key \"bar/\" { policy = \"read\" }");
      masterClient.createAclPolicy(policy).onComplete(tc.asyncAssertSuccess(policyId -> {
        AclToken init = new AclToken()
          .addPolicy(new PolicyLink().setId(policyId));
        masterClient.createAclToken(init).onComplete(tc.asyncAssertSuccess(aclToken -> {
          String id = aclToken.getAccessorId();
          masterClient.readAclToken(id).onComplete(tc.asyncAssertSuccess(token -> {
            PolicyLink receivedPolicy = token.getPolicies().stream().findFirst()
              .orElse(new PolicyLink());
            tc.assertEquals(id, token.getAccessorId());
            tc.assertEquals(policy.getName(), receivedPolicy.getName());
            masterClient.cloneAclToken(id, new CloneAclTokenOptions()).onComplete(tc.asyncAssertSuccess(clonedToken -> {
              String clonedId = clonedToken.getAccessorId();
              masterClient.readAclToken(clonedId).onComplete(tc.asyncAssertSuccess(receivedClonedToken -> {
                PolicyLink receivedClonedPolicy = receivedClonedToken.getPolicies().stream().findFirst()
                  .orElse(new PolicyLink());
                tc.assertEquals(clonedId, receivedClonedToken.getAccessorId());
                tc.assertEquals(receivedPolicy.getName(), receivedClonedPolicy.getName());
                AclPolicy policy2 = new AclPolicy()
                  .setName("updatedName")
                  .setRules("key \"bar/\" { policy = \"write\" }");
                masterClient.createAclPolicy(policy2).onComplete(tc.asyncAssertSuccess(policyId2 -> {
                  AclToken token2 = new AclToken()
                    .addPolicy(new PolicyLink().setId(policyId2));
                  masterClient.updateAclToken(clonedToken.getAccessorId(), token2).onComplete(tc.asyncAssertSuccess(updatedId -> {
                    masterClient.readAclToken(updatedId.getAccessorId()).onComplete(tc.asyncAssertSuccess(updatedToken -> {
                      PolicyLink receivedPolicy2 = updatedToken.getPolicies().stream().findFirst()
                        .orElse(new PolicyLink());
                      tc.assertEquals(clonedToken.getAccessorId(), updatedToken.getAccessorId());
                      tc.assertEquals(policy2.getName(), receivedPolicy2.getName());
                      masterClient.deleteAclToken(id).onComplete(tc.asyncAssertSuccess(v1 -> {
                        masterClient.deleteAclToken(clonedId).onComplete(tc.asyncAssertSuccess(v2 -> {
                          masterClient.getAclTokens().onComplete(tc.asyncAssertSuccess(aliveTokens -> {
                            tc.assertEquals(backupTokens.size(), aliveTokens.size());
                          }));
                        }));
                      }));
                    }));
                  }));
                }));
              }));
            }));
          }));
        }));
      }));
    }));
  }
}
