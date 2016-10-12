package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Options used to trigger a new user event.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class EventOptions {

    private static final String NODE_KEY = "node";
    private static final String SERVICE_KEY = "service";
    private static final String TAG_KEY = "tag";
    private static final String PAYLOAD_KEY = "payload";

    private String node;
    private String service;
    private String tag;
    private String payload;

    /**
     * Default constructor
     */
    public EventOptions() {
    }

    /**
     * Copy constructor
     *
     * @param options  the one to copy
     */
    public EventOptions(EventOptions options) {
        this.node = options.node;
        this.service = options.service;
        this.tag = options.tag;
        this.payload = options.payload;
    }

    /**
     * Constructor from JSON
     *
     * @param options  the JSON
     */
    public EventOptions(JsonObject options) {
        this.node = options.getString(NODE_KEY);
        this.service = options.getString(SERVICE_KEY);
        this.tag = options.getString(TAG_KEY);
        this.payload = options.getString(PAYLOAD_KEY);
    }

    /**
     * Convert to JSON
     *
     * @return  the JSON
     */
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (payload != null) {
            jsonObject.put(PAYLOAD_KEY, payload);
        }
        return jsonObject;
    }

    /**
     * Get regular expression to filter by node name
     *
     * @return regular expression to filter by node name
     */
    public String getNode() {
        return node;
    }

    /**
     * Set regular expression to filter by node name
     *
     * @param node regular expression to filter by node name
     * @return reference to this, for fluency
     */
    public EventOptions setNode(String node) {
        this.node = node;
        return this;
    }

    /**
     * Get regular expression to filter by service
     *
     * @return regular expression to filter by service
     */
    public String getService() {
        return service;
    }

    /**
     * Set regular expression to filter by service
     *
     * @param service regular expression to filter by service
     * @return reference to this, for fluency
     */
    public EventOptions setService(String service) {
        this.service = service;
        return this;
    }

    /**
     * Get regular expression to filter by tag
     *
     * @return regular expression to filter by tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set regular expression to filter by tag
     *
     * @param tag regular expression to filter by tag
     * @return reference to this, for fluency
     */
    public EventOptions setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Get payload of event
     *
     * @return payload of event
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Set payload of event
     *
     * @param payload payload of event
     * @return reference to this, for fluency
     */
    public EventOptions setPayload(String payload) {
        this.payload = payload;
        return this;
    }
}
