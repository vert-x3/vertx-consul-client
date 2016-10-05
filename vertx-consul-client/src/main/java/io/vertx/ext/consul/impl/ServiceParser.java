package io.vertx.ext.consul.impl;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
class ServiceParser {

    private static final String AGENT_SERVICE_ID = "ID";
    private static final String AGENT_SERVICE_SERVICE = "Service";
    private static final String AGENT_SERVICE_TAGS = "Tags";
    private static final String AGENT_SERVICE_ADDRESS = "Address";
    private static final String AGENT_SERVICE_PORT = "Port";

    static Service parseAgentInfo(JsonObject jsonObject) {
        JsonArray tagsArr = jsonObject.getJsonArray(AGENT_SERVICE_TAGS);
        return new Service()
                .setId(jsonObject.getString(AGENT_SERVICE_ID))
                .setName(jsonObject.getString(AGENT_SERVICE_SERVICE))
                .setTags(tagsArr == null ? null : tagsArr.getList())
                .setAddress(jsonObject.getString(AGENT_SERVICE_ADDRESS))
                .setPort(jsonObject.getInteger(AGENT_SERVICE_PORT));
    }

    static Service parseCatalogInfo(Map.Entry<String, Object> entry) {
        Object tags = entry.getValue();
        return new Service()
                .setName(entry.getKey())
                .setTags(((JsonArray) tags).stream().map(o -> (String) o).collect(Collectors.toList()));
    }

    static Service parseNodeInfo(String nodeName, String nodeAddress, JsonObject serviceInfo) {
        JsonArray tagsArr = serviceInfo.getJsonArray(AGENT_SERVICE_TAGS);
        return new Service()
                .setNode(nodeName)
                .setNodeAddress(nodeAddress)
                .setId(serviceInfo.getString(AGENT_SERVICE_ID))
                .setAddress(serviceInfo.getString(AGENT_SERVICE_ADDRESS))
                .setName(serviceInfo.getString(AGENT_SERVICE_SERVICE))
                .setTags(tagsArr == null ? null : tagsArr.getList())
                .setPort(serviceInfo.getInteger(AGENT_SERVICE_PORT));
    }

}
