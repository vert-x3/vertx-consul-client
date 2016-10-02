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
    private static final String NODE_KEY = "Node";
    private static final String CHECKS_KEY = "Checks";
    private static final String ID_KEY = "ID";
    private static final String CREATE_INDEX_KEY = "CreateIndex";

    private String lockDelay;
    private String node;
    private List<String> checks;
    private long createIndex;
    private String id;

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
        this.node = jsonObject.getString(NODE_KEY);
        this.id = jsonObject.getString(ID_KEY);
        this.createIndex = jsonObject.getLong(CREATE_INDEX_KEY, 0L);
        JsonArray arr = jsonObject.getJsonArray(CHECKS_KEY);
        this.checks = arr == null ? null : arr.getList();
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (lockDelay != null) {
            jsonObject.put(LOCK_KEY, lockDelay);
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

    public String getLockDelay() {
        return lockDelay;
    }

    public String getNode() {
        return node;
    }

    public List<String> getChecks() {
        return checks;
    }

    public String getId() {
        return id;
    }

    public long getCreateIndex() {
        return createIndex;
    }
}
