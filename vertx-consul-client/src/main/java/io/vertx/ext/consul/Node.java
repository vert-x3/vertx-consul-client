package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class Node {

    private static final String NODE_KEY = "Node";
    private static final String ADDRESS_KEY = "Address";
    private static final String TAGGED_ADDRESSES_KEY = "TaggedAddresses";
    private static final String LAN_KEY = "lan";
    private static final String WAN_KEY = "wan";

    private String node;
    private String address;
    private String lanAddress;
    private String wanAddress;

    /**
     * Constructor from JSON
     *
     * @param json  the JSON
     */
    public Node(JsonObject json) {
        this.node = json.getString(NODE_KEY);
        this.address = json.getString(ADDRESS_KEY);
        JsonObject tagged = json.getJsonObject(TAGGED_ADDRESSES_KEY);
        if (tagged != null) {
            this.lanAddress = tagged.getString(LAN_KEY);
            this.wanAddress = tagged.getString(WAN_KEY);
        }
    }

    /**
     * Convert to JSON
     *
     * @return  the JSON
     */
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (node != null) {
            jsonObject.put(NODE_KEY, node);
        }
        if (address != null) {
            jsonObject.put(ADDRESS_KEY, address);
        }
        if (lanAddress != null || wanAddress != null) {
            JsonObject tagged = new JsonObject();
            if (lanAddress != null) {
                jsonObject.put(LAN_KEY, lanAddress);
            }
            if (wanAddress != null) {
                jsonObject.put(WAN_KEY, wanAddress);
            }
            jsonObject.put(TAGGED_ADDRESSES_KEY, tagged);
        }
        return jsonObject;
    }

    /**
     * Get node name
     *
     * @return node name
     */
    public String getNode() {
        return node;
    }

    /**
     * Get node address
     *
     * @return node address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get node lan address
     *
     * @return node lan address
     */
    public String getLanAddress() {
        return lanAddress;
    }

    /**
     * Get node wan address
     *
     * @return node wan address
     */
    public String getWanAddress() {
        return wanAddress;
    }
}
