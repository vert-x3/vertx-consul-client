package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Holds properties of Consul sessions
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Session {

    private static final String LOCK_KEY = "LockDelay";
    private static final String NODE_KEY = "Node";
    private static final String CHECKS_KEY = "Checks";
    private static final String ID_KEY = "ID";
    private static final String CREATE_INDEX_KEY = "CreateIndex";

    private long lockDelay;
    private String node;
    private List<String> checks;
    private long createIndex;
    private String id;

    /**
     * Constructor from JSON
     *
     * @param session  the JSON
     */
    public Session(JsonObject session) {
        this.lockDelay = TimeUnit.NANOSECONDS.toSeconds(session.getLong(LOCK_KEY, 0L));
        this.node = session.getString(NODE_KEY);
        this.id = session.getString(ID_KEY);
        this.createIndex = session.getLong(CREATE_INDEX_KEY, 0L);
        JsonArray arr = session.getJsonArray(CHECKS_KEY);
        this.checks = arr == null ? null : arr.getList();
    }

    /**
     * Convert to JSON
     *
     * @return  the JSON
     */
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (lockDelay != 0) {
            jsonObject.put(LOCK_KEY, TimeUnit.SECONDS.toNanos(lockDelay));
        }
        if (node != null) {
            jsonObject.put(NODE_KEY, node);
        }
        if (checks != null) {
            jsonObject.put(CHECKS_KEY, checks);
        }
        if (id != null) {
            jsonObject.put(ID_KEY, id);
        }
        if (createIndex != 0) {
            jsonObject.put(CREATE_INDEX_KEY, createIndex);
        }
        return jsonObject;
    }

    /**
     * Lock delay is a time duration, between <code>0</code> and <code>60</code> seconds. When a session invalidation
     * takes place, Consul prevents any of the previously held locks from being re-acquired
     * for the <code>lock-delay</code> interval. The default is <code>15s</code>.
     *
     * @return the lock delay in seconds
     */
    public long getLockDelay() {
        return lockDelay;
    }

    /**
     * Get the human-readable name for the Session
     *
     * @return the name of session
     */
    public String getNode() {
        return node;
    }

    /**
     * Get the list of associated health checks
     *
     * @return the list of associated health checks
     */
    public List<String> getChecks() {
        return checks;
    }

    /**
     * Get the ID of session
     *
     * @return the ID of session
     */
    public String getId() {
        return id;
    }

    /**
     * Get the create index of session
     *
     * @return the create index of session
     */
    public long getCreateIndex() {
        return createIndex;
    }
}
