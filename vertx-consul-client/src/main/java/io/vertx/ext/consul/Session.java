package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Session {

    private static final String LOCK_KEY = "LockDelay";
    private static final String NAME_KEY = "Name";
    private static final String NODE_KEY = "Node";
    private static final String CHECKS_KEY = "Checks";
    private static final String BEHAVIOR_KEY = "Behavior";
    private static final String TTL_KEY = "TTL";

    private String lockDelay;
    private String name;
    private String node;
    private List<String> checks;
    private String behavior;
    private String ttl;

    public Session() {}

    public Session(JsonObject jsonObject) {
        Object delay = jsonObject.getValue(LOCK_KEY);
        if (delay != null) {
            if (delay instanceof String) {
                this.lockDelay = (String) delay;
            } else {
                this.lockDelay = TimeUnit.NANOSECONDS.toSeconds((Long) delay) + "s";
            }
        }
        this.name = jsonObject.getString(NAME_KEY);
        this.node = jsonObject.getString(NODE_KEY);
        JsonArray arr = jsonObject.getJsonArray(CHECKS_KEY);
        this.checks = arr == null ? null : arr.getList();
        this.behavior = jsonObject.getString(BEHAVIOR_KEY);
        this.ttl = jsonObject.getString(TTL_KEY);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (lockDelay != null) {
            jsonObject.put(LOCK_KEY, lockDelay);
        }
        if (name != null) {
            jsonObject.put(NAME_KEY, name);
        }
        if (node != null) {
            jsonObject.put(NODE_KEY, node);
        }
        if (checks != null) {
            jsonObject.put(CHECKS_KEY, checks);
        }
        if (behavior != null) {
            jsonObject.put(BEHAVIOR_KEY, behavior);
        }
        if (ttl != null) {
            jsonObject.put(TTL_KEY, ttl);
        }
        return jsonObject;
    }

    public String getLockDelay() {
        return lockDelay;
    }

    public String getName() {
        return name;
    }

    public String getNode() {
        return node;
    }

    public List<String> getChecks() {
        return checks;
    }

    public String getBehavior() {
        return behavior;
    }

    public String getTtl() {
        return ttl;
    }

    public Session setLockDelay(String lockDelay) {
        this.lockDelay = lockDelay;
        return this;
    }

    public Session setName(String name) {
        this.name = name;
        return this;
    }

    public Session setNode(String node) {
        this.node = node;
        return this;
    }

    public Session setChecks(List<String> checks) {
        this.checks = checks;
        return this;
    }

    public Session setBehavior(String behavior) {
        this.behavior = behavior;
        return this;
    }

    public Session setTtl(String ttl) {
        this.ttl = ttl;
        return this;
    }
}
