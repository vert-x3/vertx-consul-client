package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class ServiceOptions {

    private static final String SERVICE_ID = "ID";
    private static final String SERVICE_NAME = "Name";
    private static final String SERVICE_TAGS = "Tags";
    private static final String SERVICE_ADDRESS = "Address";
    private static final String SERVICE_PORT = "Port";
    private static final String SERVICE_CHECK = "Check";

    private String id;
    private String name;
    private List<String> tags;
    private String address;
    private int port;
    private CheckOptions checkOptions;

    public ServiceOptions() {
    }

    public ServiceOptions(JsonObject jsonObject) {
        this.id = jsonObject.getString(SERVICE_ID);
        this.name = jsonObject.getString(SERVICE_NAME);
        JsonArray tagsArr = jsonObject.getJsonArray(SERVICE_TAGS);
        this.tags = tagsArr == null ? null : tagsArr.getList();
        this.address = jsonObject.getString(SERVICE_ADDRESS);
        this.port = jsonObject.getInteger(SERVICE_PORT);
        JsonObject checkObj = jsonObject.getJsonObject(SERVICE_CHECK);
        this.checkOptions = checkObj == null ? null : new CheckOptions(checkObj);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (id != null) {
            jsonObject.put(SERVICE_ID, id);
        }
        if (name != null) {
            jsonObject.put(SERVICE_NAME, name);
        }
        if (tags != null) {
            jsonObject.put(SERVICE_TAGS, new JsonArray(tags));
        }
        if (address != null) {
            jsonObject.put(SERVICE_ADDRESS, address);
        }
        if (port != 0) {
            jsonObject.put(SERVICE_PORT, port);
        }
        if (checkOptions != null) {
            jsonObject.put(SERVICE_CHECK, checkOptions.toJson());
        }
        return jsonObject;
    }

    public ServiceOptions setId(String id) {
        this.id = id;
        return this;
    }

    public ServiceOptions setName(String name) {
        this.name = name;
        return this;
    }

    public ServiceOptions setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public ServiceOptions setAddress(String address) {
        this.address = address;
        return this;
    }

    public ServiceOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public ServiceOptions setCheckOptions(CheckOptions checkOptions) {
        this.checkOptions = checkOptions;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public CheckOptions getCheckOptions() {
        return checkOptions;
    }
}
