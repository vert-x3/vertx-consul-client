package io.vertx.ext.consul;

/**
 * Acl token type is either "client" (meaning the token cannot modify ACL rules) or "management"
 * (meaning the token is allowed to perform all actions).
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 * @see <a href="https://www.consul.io/docs/internals/acl.html">Acl Consul system</a>
 */
public enum AclTokenType {

  CLIENT("client"),
  MANAGEMENT("management");

  public final String key;

  public static AclTokenType of(String name) {
    if (name == null) {
      return null;
    } else {
      return CLIENT.key.equals(name) ? CLIENT : MANAGEMENT;
    }
  }

  AclTokenType(String key) {
    this.key = key;
  }
}
