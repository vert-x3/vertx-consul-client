package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class MaintenanceOptions {

    private static final String ID_KEY = "ID";
    private static final String ENABLE_KEY = "Enable";
    private static final String REASON_KEY = "Reason";

    private String id;
    private boolean enable;
    private String reason;

    public MaintenanceOptions() {}

    public MaintenanceOptions(JsonObject jsonObject) {
        id = jsonObject.getString(ID_KEY);
        enable = jsonObject.getBoolean(ENABLE_KEY);
        reason = jsonObject.getString(REASON_KEY);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (id != null) {
            jsonObject.put(ID_KEY, id);
        }
        jsonObject.put(ENABLE_KEY, enable);
        if (reason != null) {
            jsonObject.put(REASON_KEY, reason);
        }
        return jsonObject;
    }

    public MaintenanceOptions setId(String id) {
        this.id = id;
        return this;
    }

    public MaintenanceOptions setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public MaintenanceOptions setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getId() {
        return id;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getReason() {
        return reason;
    }
}
