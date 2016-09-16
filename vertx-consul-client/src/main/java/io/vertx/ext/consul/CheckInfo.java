package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class CheckInfo {

    private static final String SERVICE_ID_KEY = "ServiceID";
    private static final String SERVICE_NAME_KEY = "ServiceName";
    private static final String ID_KEY = "CheckID";
    private static final String NAME_KEY = "Name";
    private static final String STATUS_KEY = "Status";
    private static final String NOTES_KEY = "Notes";
    private static final String OUTPUT_KEY = "Output";

    private String id;
    private String name;
    private String status;
    private String notes;
    private String output;
    private String serviceId;
    private String serviceName;

    public CheckInfo(JsonObject jsonObject) {
        this.id = jsonObject.getString(ID_KEY);
        this.name = jsonObject.getString(NAME_KEY);
        this.status = jsonObject.getString(STATUS_KEY);
        this.notes = jsonObject.getString(NOTES_KEY);
        this.output = jsonObject.getString(OUTPUT_KEY);
        this.serviceId = jsonObject.getString(SERVICE_ID_KEY);
        this.serviceName = jsonObject.getString(SERVICE_NAME_KEY);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (id != null) {
            jsonObject.put(ID_KEY, id);
        }
        if (name != null) {
            jsonObject.put(NAME_KEY, name);
        }
        if (status != null) {
            jsonObject.put(STATUS_KEY, status);
        }
        if (notes != null) {
            jsonObject.put(NOTES_KEY, notes);
        }
        if (output != null) {
            jsonObject.put(OUTPUT_KEY, output);
        }
        if (serviceId != null) {
            jsonObject.put(SERVICE_ID_KEY, serviceId);
        }
        if (serviceName != null) {
            jsonObject.put(SERVICE_NAME_KEY, serviceName);
        }
        return jsonObject;
    }

    public JsonObject updateRequest() {
        JsonObject jsonObject = new JsonObject();
        if (status != null) {
            jsonObject.put(STATUS_KEY, status);
        }
        if (output != null) {
            jsonObject.put(OUTPUT_KEY, output);
        }
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getOutput() {
        return output;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public CheckInfo setId(String id) {
        this.id = id;
        return this;
    }

    public CheckInfo setName(String name) {
        this.name = name;
        return this;
    }

    public CheckInfo setStatus(String status) {
        this.status = status;
        return this;
    }

    public CheckInfo setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public CheckInfo setOutput(String output) {
        this.output = output;
        return this;
    }

    public CheckInfo setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public CheckInfo setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }
}
