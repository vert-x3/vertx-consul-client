package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Options used to create session.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class SessionOptions {

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
    private SessionBehavior behavior;
    private String ttl;

    /**
     * Default constructor
     */
    public SessionOptions() {}

    /**
     * Copy constructor
     *
     * @param options  the one to copy
     */
    public SessionOptions(SessionOptions options) {
        this.lockDelay = options.lockDelay;
        this.name = options.name;
        this.node = options.node;
        this.checks = new ArrayList<>(options.checks);
        this.behavior = options.behavior;
        this.ttl = options.ttl;
    }

    /**
     * Constructor from JSON
     *
     * @param options  the JSON
     */
    public SessionOptions(JsonObject options) {
        Object delay = options.getValue(LOCK_KEY);
        if (delay != null) {
            if (delay instanceof String) {
                this.lockDelay = (String) delay;
            } else {
                this.lockDelay = TimeUnit.NANOSECONDS.toSeconds((Long) delay) + "s";
            }
        }
        this.name = options.getString(NAME_KEY);
        this.node = options.getString(NODE_KEY);
        JsonArray arr = options.getJsonArray(CHECKS_KEY);
        this.checks = arr == null ? null : arr.getList();
        this.behavior = SessionBehavior.of(options.getString(BEHAVIOR_KEY));
        this.ttl = options.getString(TTL_KEY);
    }

    /**
     * Convert to JSON
     *
     * @return  the JSON
     */
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
            jsonObject.put(BEHAVIOR_KEY, behavior.key);
        }
        if (ttl != null) {
            jsonObject.put(TTL_KEY, ttl);
        }
        return jsonObject;
    }

    /**
     * Gets the lock-delay period.
     *
     * @return the lock-delay period
     * @see #setLockDelay(String)
     */
    public String getLockDelay() {
        return lockDelay;
    }

    /**
     * Sets the lock-delay period. This is a time duration, between 0 and 60 seconds. When a session invalidation
     * takes place, Consul prevents any of the previously held locks from being re-acquired for the lock-delay interval
     * Must be represented by the seconds number with the suffix "s".
     *
     * @param lockDelay the lock-delay period
     * @return reference to this, for fluency
     */
    public SessionOptions setLockDelay(String lockDelay) {
        this.lockDelay = lockDelay;
        return this;
    }

    /**
     * Gets the human-readable name for the Session
     *
     * @return the name of session
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name for the Session
     *
     * @param name the name of session
     * @return reference to this, for fluency
     */
    public SessionOptions setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the node to which the session will be assigned
     *
     * @return the ID of node
     */
    public String getNode() {
        return node;
    }

    /**
     * Sets the node to which the session will be assigned
     *
     * @param node the ID of node
     * @return reference to this, for fluency
     */
    public SessionOptions setNode(String node) {
        this.node = node;
        return this;
    }

    /**
     * Gets Set a list of associated health checks.
     *
     * @return list of associated health checks
     * @see #setChecks(List)
     */
    public List<String> getChecks() {
        return checks;
    }

    /**
     * Sets a list of associated health checks. It is highly recommended that,
     * if you override this list, you include the default "serfHealth"
     *
     * @param checks list of associated health checks
     * @return reference to this, for fluency
     */
    public SessionOptions setChecks(List<String> checks) {
        this.checks = checks;
        return this;
    }

    /**
     * Gets the behavior when a session is invalidated.
     *
     * @return the session behavior
     * @see #setBehavior(SessionBehavior)
     */
    public SessionBehavior getBehavior() {
        return behavior;
    }

    /**
     * Sets the behavior when a session is invalidated. The release behavior is the default if none is specified.
     *
     * @param behavior the session behavior
     * @return reference to this, for fluency
     */
    public SessionOptions setBehavior(SessionBehavior behavior) {
        this.behavior = behavior;
        return this;
    }

    /**
     * Gets the TTL interval.
     *
     * @return the TTL interval
     * @see #setTtl(String)
     */
    public String getTtl() {
        return ttl;
    }

    /**
     * Sets the TTL interval. When TTL interval expires without being renewed, the session has expired
     * and an invalidation is triggered. Must be represented by the seconds number with the suffix "s".
     * If specified, it must be between 10s and 86400s currently.
     *
     * @param ttl the TTL interval
     * @return reference to this, for fluency
     */
    public SessionOptions setTtl(String ttl) {
        this.ttl = ttl;
        return this;
    }
}
