package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Base64;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class KeyValuePair {

    private static final String KEY_KEY = "Key";
    private static final String VALUE_KEY = "Value";
    private static final String SESSION_KEY = "Session";
    private static final String FLAGS_KEY = "Flags";

    private String key;
    private String value;
    private String session;
    private long flags;

    public static KeyValuePair parse(JsonObject jsonObject) {
        KeyValuePair pair = new KeyValuePair();
        pair.key = jsonObject.getString(KEY_KEY);
        pair.value = new String(Base64.getDecoder().decode(jsonObject.getString(VALUE_KEY)));
        pair.session = jsonObject.getString(SESSION_KEY);
        Object valObj = jsonObject.getValue(FLAGS_KEY);
        pair.flags = valObj == null ? 0L : ((Number) valObj).longValue();
        return pair;
    }

    private KeyValuePair() {}

    public KeyValuePair(JsonObject jsonObject) {
        key = jsonObject.getString(KEY_KEY);
        value = jsonObject.getString(VALUE_KEY);
        session = jsonObject.getString(SESSION_KEY);
        flags = jsonObject.getLong(FLAGS_KEY, 0L);
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
            jsonObject.put(SESSION_KEY, value);
        }
        if (flags != 0) {
            jsonObject.put(FLAGS_KEY, flags);
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
}
