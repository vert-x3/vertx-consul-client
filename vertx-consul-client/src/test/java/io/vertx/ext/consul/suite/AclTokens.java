package io.vertx.ext.consul.suite;

import io.vertx.ext.consul.AclToken;
import io.vertx.ext.consul.ConsulTestBase;
import org.junit.Test;

import java.util.List;

import static io.vertx.ext.consul.Utils.getSync;
import static io.vertx.ext.consul.Utils.runSync;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class AclTokens extends ConsulTestBase {

    @Test
    public void tokenLifecycle() throws Throwable {
        List<AclToken> backupTokens = getSync(h -> masterClient.listAclTokens(h));

        AclToken init = new AclToken()
                .setName("tokenName")
                .setRules("key \"bar/\" { policy = \"read\" }");
        String id = getSync(h -> masterClient.createAclToken(init, h));

        List<AclToken> list = getSync(h -> masterClient.listAclTokens(h));
        long filteredById = list.stream().filter(t -> t.getId().equals(id)).count();
        assertEquals(1, filteredById);

        AclToken token = getSync(h -> masterClient.infoAclToken(id, h));
        assertEquals(id, token.getId());
        assertEquals(init.getName(), token.getName());
        assertEquals(init.getRules(), token.getRules());

        String clonedId = getSync(h -> masterClient.cloneAclToken(id, h));
        AclToken clonedToken = getSync(h -> masterClient.infoAclToken(clonedId, h));
        assertEquals(clonedId, clonedToken.getId());
        assertEquals(token.getName(), clonedToken.getName());
        assertEquals(token.getRules(), clonedToken.getRules());

        AclToken token2 = new AclToken()
                .setId(clonedToken.getId())
                .setName("updatedName")
                .setRules("key \"bar/\" { policy = \"write\" }");
        String updatedId = getSync(h -> masterClient.updateAclToken(token2, h));
        AclToken updatedToken = getSync(h -> masterClient.infoAclToken(updatedId, h));
        assertEquals(token2.getId(), updatedToken.getId());
        assertEquals(token2.getName(), updatedToken.getName());
        assertEquals(token2.getRules(), updatedToken.getRules());

        runSync(h -> masterClient.destroyAclToken(id, h));
        runSync(h -> masterClient.destroyAclToken(clonedId, h));

        List<AclToken> aliveTokens = getSync(h -> masterClient.listAclTokens(h));
        assertEquals(backupTokens.size(), aliveTokens.size());
    }

}
