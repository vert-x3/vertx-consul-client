package io.vertx.ext.consul.v1.acl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.consul.v1.acl.policy.Policies;
import io.vertx.ext.consul.v1.acl.policy.Policy;
import io.vertx.ext.consul.v1.acl.token.Token;
import io.vertx.ext.consul.v1.acl.token.Tokens;


/**
 * An object to consul /acl endpoint functionality interaction.
 */
public interface ACL {


  /**
   * @return a reference to this, so the API can be used fluently
   * @see <a href="https://www.consul.io/api/acl/acl.html#bootstrap-acls">/acl/bootstrap</a> endpoint
   */
  ACL bootstrap(Handler<AsyncResult<Bootstrap>> handler);

  /**
   * @return a reference to this, so the API can be used fluently
   * @see <a href="https://www.consul.io/api/acl/acl.html#check-acl-replication">/acl/replication</a> endpoint
   */
  ACL replication(Handler<AsyncResult<AclReplication>> handler);


  Token token();

  Tokens tokens();

  Policy policy();

  Policies policies();

}
