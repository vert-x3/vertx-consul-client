package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject
public class AclToken {

    public enum Type {
        CLIENT("client"),
        MANAGEMENT("management");

        public final String name;

        public static Type of(String name) {
            if (name == null) {
                return null;
            } else {
                return CLIENT.name.equals(name) ? CLIENT : MANAGEMENT;
            }
        }

        Type(String name) {
            this.name = name;
        }
    }

    public static final String ID_KEY = "ID";
    public static final String NAME_KEY = "Name";
    public static final String TYPE_KEY = "Type";
    public static final String RULES_KEY = "Rules";

    public final String id;
    public final String name;
    public final Type type;
    public final String rules;

    public static AclToken empty() {
        return new AclToken(null, null, null, null);
    }

    public AclToken(String id, String name, Type type, String rules) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.rules = rules;
    }

    public AclToken(JsonObject json) {
        this.id = json.getString(ID_KEY);
        this.name = json.getString(NAME_KEY);
        this.type = Type.of(json.getString(TYPE_KEY));
        this.rules = json.getString(RULES_KEY);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        if (id != null) {
            jsonObject.put(ID_KEY, id);
        }
        if (name != null) {
            jsonObject.put(NAME_KEY, name);
        }
        if (type != null) {
            jsonObject.put(TYPE_KEY, type);
        }
        if (rules != null) {
            jsonObject.put(RULES_KEY, rules);
        }
        return jsonObject;
    }
}
