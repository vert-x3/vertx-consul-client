package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class KeyValuePair {

    public static final String KEY = "key";
    public static final String VALUE = "value";

    private final String key;
    private final String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValuePair(JsonObject jsonObject) {
        this(jsonObject.getString(KEY), jsonObject.getString(VALUE));
    }

    public JsonObject toJson() {
        return new JsonObject().put(KEY, key).put(VALUE, value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValuePair pair = (KeyValuePair) o;

        if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
        return value != null ? value.equals(pair.value) : pair.value == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
