package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.AclTokenType;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getAsync;
import static io.vertx.ext.consul.Utils.runAsync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class AclTokens extends ConsulTestBase {

  @Test
  public void serializeToken() {
    AclToken src = new AclToken()
      .setId("tokenId")
      .setName("tokenName")
      .setType(AclTokenType.MANAGEMENT)
      .setRules("tokenRules");
    AclToken restored = new AclToken(src.toJson());
    assertEquals(src.getId(), restored.getId());
    assertEquals(src.getName(), restored.getName());
    assertEquals(src.getType(), restored.getType());
    assertEquals(src.getRules(), restored.getRules());
  }

  @Test
  public void copyToken() {
    AclToken src = new AclToken()
      .setId("tokenId")
      .setName("tokenName")
      .setType(AclTokenType.MANAGEMENT)
      .setRules("tokenRules");
    AclToken restored = new AclToken(src);
    assertEquals(src.getId(), restored.getId());
    assertEquals(src.getName(), restored.getName());
    assertEquals(src.getType(), restored.getType());
    assertEquals(src.getRules(), restored.getRules());
  }

  @Test
  public void tokenLifecycle() {
    List<AclToken> backupTokens = getAsync(h -> masterClient.listAclTokens(h));

    AclToken init = new AclToken()
      .setName("tokenName")
      .setRules("key \"bar/\" { policy = \"read\" }");
    String id = getAsync(h -> masterClient.createAclToken(init, h));

    List<AclToken> list = getAsync(h -> masterClient.listAclTokens(h));
    long filteredById = list.stream().filter(t -> t.getId().equals(id)).count();
    assertEquals(1, filteredById);

    AclToken token = getAsync(h -> masterClient.infoAclToken(id, h));
    assertEquals(id, token.getId());
    assertEquals(init.getName(), token.getName());
    assertEquals(init.getRules(), token.getRules());

    String clonedId = getAsync(h -> masterClient.cloneAclToken(id, h));
    AclToken clonedToken = getAsync(h -> masterClient.infoAclToken(clonedId, h));
    assertEquals(clonedId, clonedToken.getId());
    assertEquals(token.getName(), clonedToken.getName());
    assertEquals(token.getRules(), clonedToken.getRules());

    AclToken token2 = new AclToken()
      .setId(clonedToken.getId())
      .setName("updatedName")
      .setRules("key \"bar/\" { policy = \"write\" }");
    String updatedId = getAsync(h -> masterClient.updateAclToken(token2, h));
    AclToken updatedToken = getAsync(h -> masterClient.infoAclToken(updatedId, h));
    assertEquals(token2.getId(), updatedToken.getId());
    assertEquals(token2.getName(), updatedToken.getName());
    assertEquals(token2.getRules(), updatedToken.getRules());

    runAsync(h -> masterClient.destroyAclToken(id, h));
    runAsync(h -> masterClient.destroyAclToken(clonedId, h));

    List<AclToken> aliveTokens = getAsync(h -> masterClient.listAclTokens(h));
    assertEquals(backupTokens.size(), aliveTokens.size());
  }

}
