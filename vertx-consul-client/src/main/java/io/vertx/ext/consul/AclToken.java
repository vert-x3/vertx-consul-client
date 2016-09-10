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

    private static final String ID_KEY = "ID";
    private static final String NAME_KEY = "Name";
    private static final String TYPE_KEY = "Type";
    private static final String RULES_KEY = "Rules";

    private final String id;
    private final String name;
    private final Type type;
    private final String rules;

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getRules() {
        return rules;
    }

    public AclToken withId(String id) {
        return new AclToken(id, name, type, rules);
    }

    public AclToken withName(String name) {
        return new AclToken(id, name, type, rules);
    }

    public AclToken withType(Type type) {
        return new AclToken(id, name, type, rules);
    }

    public AclToken withRules(String rules) {
        return new AclToken(id, name, type, rules);
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
