package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class KeyValuePairOptions {

    private static final String KEY_KEY = "Key";
    private static final String VALUE_KEY = "Value";
    private static final String ACQUIRE_SESSION_KEY = "AcquireSession";
    private static final String RELEASE_SESSION_KEY = "ReleaseSession";
    private static final String FLAGS_KEY = "Flags";

    private String key;
    private String value;
    private String acquireSession;
    private String releaseSession;
    private long flags;

    public KeyValuePairOptions() {}

    public KeyValuePairOptions(JsonObject jsonObject) {
        key = jsonObject.getString(KEY_KEY);
        value = jsonObject.getString(VALUE_KEY);
        acquireSession = jsonObject.getString(ACQUIRE_SESSION_KEY);
        releaseSession = jsonObject.getString(RELEASE_SESSION_KEY);
        String flagsStr = jsonObject.getString(FLAGS_KEY);
        flags = flagsStr == null ? 0L : Long.parseUnsignedLong(flagsStr);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (key != null) {
            jsonObject.put(KEY_KEY, key);
        }
        if (value != null) {
            jsonObject.put(VALUE_KEY, value);
        }
        if (acquireSession != null) {
            jsonObject.put(ACQUIRE_SESSION_KEY, acquireSession);
        }
        if (releaseSession != null) {
            jsonObject.put(RELEASE_SESSION_KEY, releaseSession);
        }
        if (flags != 0) {
            jsonObject.put(FLAGS_KEY, Long.toUnsignedString(flags));
        }
        return jsonObject;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getAcquireSession() {
        return acquireSession;
    }

    public String getReleaseSession() {
        return releaseSession;
    }

    public long getFlags() {
        return flags;
    }

    public KeyValuePairOptions setKey(String key) {
        this.key = key;
        return this;
    }

    public KeyValuePairOptions setValue(String value) {
        this.value = value;
        return this;
    }

    public KeyValuePairOptions setAcquireSession(String acquireSession) {
        this.acquireSession = acquireSession;
        return this;
    }

    public KeyValuePairOptions setReleaseSession(String releaseSession) {
        this.releaseSession = releaseSession;
        return this;
    }

    public KeyValuePairOptions setFlags(long flags) {
        this.flags = flags;
        return this;
    }
}
