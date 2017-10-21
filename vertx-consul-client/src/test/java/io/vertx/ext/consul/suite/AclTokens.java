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
    ctx.masterClient().listAclTokens(tc.asyncAssertSuccess(backupTokens -> {
      AclToken init = new AclToken()
        .setName("tokenName")
        .setRules("key \"bar/\" { policy = \"read\" }");
      ctx.masterClient().createAclToken(init, tc.asyncAssertSuccess(id -> {
        ctx.masterClient().infoAclToken(id, tc.asyncAssertSuccess(token -> {
          tc.assertEquals(id, token.getId());
          tc.assertEquals(init.getName(), token.getName());
          tc.assertEquals(init.getRules(), token.getRules());
          ctx.masterClient().cloneAclToken(id, tc.asyncAssertSuccess(clonedId -> {
            ctx.masterClient().infoAclToken(clonedId, tc.asyncAssertSuccess(clonedToken -> {
              tc.assertEquals(clonedId, clonedToken.getId());
              tc.assertEquals(token.getName(), clonedToken.getName());
              tc.assertEquals(token.getRules(), clonedToken.getRules());
              AclToken token2 = new AclToken()
                .setId(clonedToken.getId())
                .setName("updatedName")
                .setRules("key \"bar/\" { policy = \"write\" }");
              ctx.masterClient().updateAclToken(token2, tc.asyncAssertSuccess(updatedId -> {
                ctx.masterClient().infoAclToken(updatedId, tc.asyncAssertSuccess(updatedToken -> {
                  tc.assertEquals(token2.getId(), updatedToken.getId());
                  tc.assertEquals(token2.getName(), updatedToken.getName());
                  tc.assertEquals(token2.getRules(), updatedToken.getRules());
                  ctx.masterClient().destroyAclToken(id, tc.asyncAssertSuccess(v1 -> {
                    ctx.masterClient().destroyAclToken(clonedId, tc.asyncAssertSuccess(v2 -> {
                      ctx.masterClient().listAclTokens(tc.asyncAssertSuccess(aliveTokens -> {
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
