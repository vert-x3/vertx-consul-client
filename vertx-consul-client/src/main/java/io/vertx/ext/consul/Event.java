package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Event {

    private static final String ID_KEY = "ID";
    private static final String NAME_KEY = "Name";
    private static final String PAYLOAD_KEY = "Payload";

    private final String id;
    private final String name;
    private final String payload;

    public static Event empty() {
        return new Event(null, null, null);
    }

    public Event(String id, String name, String payload) {
        this.id = id;
        this.name = name;
        this.payload = payload;
    }

    public Event(JsonObject json) {
        this.id = json.getString(ID_KEY);
        this.name = json.getString(NAME_KEY);
        this.payload = json.getString(PAYLOAD_KEY);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPayload() {
        return payload;
    }

    public Event withId(String id) {
        return new Event(id, name, payload);
    }

    public Event withName(String name) {
        return new Event(id, name, payload);
    }

    public Event withPayload(String payload) {
        return new Event(id, name, payload);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (id != null) {
            jsonObject.put(ID_KEY, id);
        }
        if (name != null) {
            jsonObject.put(NAME_KEY, name);
        }
        if (payload != null) {
            jsonObject.put(PAYLOAD_KEY, payload);
        }
        return jsonObject;
    }
}
