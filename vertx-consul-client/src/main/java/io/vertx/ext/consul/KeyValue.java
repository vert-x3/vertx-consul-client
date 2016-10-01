package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Base64;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class KeyValue {

    private static final String KEY_KEY = "Key";
    private static final String VALUE_KEY = "Value";
    private static final String SESSION_KEY = "Session";
    private static final String FLAGS_KEY = "Flags";
    private static final String CREATE_KEY = "CreateIndex";
    private static final String MODIFY_KEY = "ModifyIndex";
    private static final String LOCK_KEY = "LockIndex";

    private String key;
    private String value;
    private String session;
    private long flags;
    private long createIndex;
    private long modifyIndex;
    private long lockIndex;

    public static KeyValue parse(JsonObject jsonObject) {
        KeyValue pair = new KeyValue();
        pair.key = jsonObject.getString(KEY_KEY);
        pair.value = new String(Base64.getDecoder().decode(jsonObject.getString(VALUE_KEY)));
        pair.session = jsonObject.getString(SESSION_KEY);
        Object valObj = jsonObject.getValue(FLAGS_KEY);
        pair.flags = valObj == null ? 0L : ((Number) valObj).longValue();
        pair.createIndex = jsonObject.getLong(CREATE_KEY, 0L);
        pair.modifyIndex = jsonObject.getLong(MODIFY_KEY, 0L);
        pair.lockIndex = jsonObject.getLong(LOCK_KEY, 0L);
        return pair;
    }

    private KeyValue() {}

    public KeyValue(JsonObject jsonObject) {
        key = jsonObject.getString(KEY_KEY);
        value = jsonObject.getString(VALUE_KEY);
        session = jsonObject.getString(SESSION_KEY);
        flags = jsonObject.getLong(FLAGS_KEY, 0L);
        createIndex = jsonObject.getLong(CREATE_KEY, 0L);
        modifyIndex = jsonObject.getLong(MODIFY_KEY, 0L);
        lockIndex = jsonObject.getLong(LOCK_KEY, 0L);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (key != null) {
            jsonObject.put(KEY_KEY, key);
        }
        if (value != null) {
            jsonObject.put(VALUE_KEY, value);
        }
        if (session != null) {
            jsonObject.put(SESSION_KEY, session);
        }
        if (flags != 0) {
            jsonObject.put(FLAGS_KEY, flags);
        }
        if (createIndex != 0) {
            jsonObject.put(CREATE_KEY, createIndex);
        }
        if (modifyIndex != 0) {
            jsonObject.put(MODIFY_KEY, modifyIndex);
        }
        if (lockIndex != 0) {
            jsonObject.put(LOCK_KEY, lockIndex);
        }
        return jsonObject;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getSession() {
        return session;
    }

    public long getFlags() {
        return flags;
    }

    public long getCreateIndex() {
        return createIndex;
    }

    public long getModifyIndex() {
        return modifyIndex;
    }

    public long getLockIndex() {
        return lockIndex;
    }
}
