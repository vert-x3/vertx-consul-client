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
package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.ConsulTestBase;
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
    masterClient.listAclTokens().onComplete(tc.asyncAssertSuccess(backupTokens -> {
      AclToken init = new AclToken()
        .setName("tokenName")
        .setRules("key \"bar/\" { policy = \"read\" }");
      masterClient.createAclToken(init).onComplete(tc.asyncAssertSuccess(id -> {
        masterClient.infoAclToken(id).onComplete(tc.asyncAssertSuccess(token -> {
          tc.assertEquals(id, token.getId());
          tc.assertEquals(init.getName(), token.getName());
          tc.assertEquals(init.getRules(), token.getRules());
          masterClient.cloneAclToken(id).onComplete(tc.asyncAssertSuccess(clonedId -> {
            masterClient.infoAclToken(clonedId).onComplete(tc.asyncAssertSuccess(clonedToken -> {
              tc.assertEquals(clonedId, clonedToken.getId());
              tc.assertEquals(token.getName(), clonedToken.getName());
              tc.assertEquals(token.getRules(), clonedToken.getRules());
              AclToken token2 = new AclToken()
                .setId(clonedToken.getId())
                .setName("updatedName")
                .setRules("key \"bar/\" { policy = \"write\" }");
              masterClient.updateAclToken(token2).onComplete(tc.asyncAssertSuccess(updatedId -> {
                masterClient.infoAclToken(updatedId).onComplete(tc.asyncAssertSuccess(updatedToken -> {
                  tc.assertEquals(token2.getId(), updatedToken.getId());
                  tc.assertEquals(token2.getName(), updatedToken.getName());
                  tc.assertEquals(token2.getRules(), updatedToken.getRules());
                  masterClient.destroyAclToken(id).onComplete(tc.asyncAssertSuccess(v1 -> {
                    masterClient.destroyAclToken(clonedId).onComplete(tc.asyncAssertSuccess(v2 -> {
                      masterClient.listAclTokens().onComplete(tc.asyncAssertSuccess(aliveTokens -> {
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
  }
}
