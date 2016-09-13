package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Service {

    private static final String NODE = "Node";
    private static final String ADDRESS = "Address";
    private static final String SERVICE_ID = "ServiceID";
    private static final String SERVICE_NAME = "ServiceName";
    private static final String SERVICE_TAGS = "ServiceTags";
    private static final String SERVICE_ADDRESS = "ServiceAddress";
    private static final String SERVICE_PORT = "ServicePort";

    private static final String AGENT_SERVICE_ID = "ID";
    private static final String AGENT_SERVICE_NAME = "Name";
    private static final String AGENT_SERVICE_TAGS = "Tags";
    private static final String AGENT_SERVICE_ADDRESS = "Address";
    private static final String AGENT_SERVICE_PORT = "Port";

    private final String node;
    private final String nodeAddress;
    private final String id;
    private final String name;
    private final List<String> tags;
    private final String address;
    private final int port;

    public static Service build() {
        return new Service(null, null, null, null, null, null, 0);
    }

    public Service withNode(String node) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    public Service withNodeAddress(String nodeAddress) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    public Service withId(String id) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    public Service withName(String name) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    public Service withTags(List<String> tags) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    public Service withAddress(String address) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    public Service withPort(int port) {
        return new Service(node, nodeAddress, id, name, tags, address, port);
    }

    private Service(String node, String nodeAddress, String id, String name, List<String> tags, String address, int port) {
        this.node = node;
        this.nodeAddress = nodeAddress;
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.address = address;
        this.port = port;
    }

    public Service(JsonObject jsonObject) {
        this.node = jsonObject.getString(NODE);
        this.nodeAddress = jsonObject.getString(ADDRESS);
        this.id = jsonObject.getString(SERVICE_ID);
        this.name = jsonObject.getString(SERVICE_NAME);
        this.tags = jsonObject.getJsonArray(SERVICE_TAGS).stream().map(obj -> (String) obj).collect(Collectors.toList());
        this.address = jsonObject.getString(SERVICE_ADDRESS);
        this.port = jsonObject.getInteger(SERVICE_PORT);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (node != null) {
            jsonObject.put(NODE, node);
        }
        if (nodeAddress != null) {
            jsonObject.put(ADDRESS, nodeAddress);
        }
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
        return jsonObject;
    }

    public JsonObject registerRequest() {
        JsonObject jsonObject = new JsonObject();
        if (id != null) {
            jsonObject.put(AGENT_SERVICE_ID, id);
        }
        if (name != null) {
            jsonObject.put(AGENT_SERVICE_NAME, name);
        }
        if (tags != null) {
            jsonObject.put(AGENT_SERVICE_TAGS, new JsonArray(tags));
        }
        if (address != null) {
            jsonObject.put(AGENT_SERVICE_ADDRESS, address);
        }
        if (port != 0) {
            jsonObject.put(AGENT_SERVICE_PORT, port);
        }
        return jsonObject;
    }

    public String getNode() {
        return node;
    }

    public String getNodeAddress() {
        return nodeAddress;
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
}
