package io.vertx.ext.consul;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.impl.ConsulClientImpl;

import java.util.List;

/**
 * A Vert.x service used to interact with Consul.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@VertxGen
public interface ConsulClient {

    static ConsulClient create(Vertx vertx, JsonObject config) {
        return new ConsulClientImpl(vertx, config);
    }

    @Fluent
    ConsulClient getValue(String key, Handler<AsyncResult<KeyValue>> resultHandler);

    @Fluent
    ConsulClient deleteValue(String key, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient getValues(String keyPrefix, Handler<AsyncResult<List<KeyValue>>> resultHandler);

    @Fluent
    ConsulClient deleteValues(String keyPrefix, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient putValue(String key, String value, Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    ConsulClient putValueWithOptions(String key, String value, KeyValueOptions options, Handler<AsyncResult<Boolean>> resultHandler);

    @Fluent
    ConsulClient createAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

    @Fluent
    ConsulClient updateAclToken(AclToken token, Handler<AsyncResult<String>> idHandler);

    @Fluent
    ConsulClient cloneAclToken(String id, Handler<AsyncResult<String>> idHandler);

    @Fluent
    ConsulClient listAclTokens(Handler<AsyncResult<List<AclToken>>> resultHandler);

    @Fluent
    ConsulClient infoAclToken(String id, Handler<AsyncResult<AclToken>> tokenHandler);

    @Fluent
    ConsulClient destroyAclToken(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient fireEvent(Event event, Handler<AsyncResult<Event>> resultHandler);

    @Fluent
    ConsulClient listEvents(Handler<AsyncResult<List<Event>>> resultHandler);

    @Fluent
    ConsulClient registerService(ServiceOptions service, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient maintenanceService(MaintenanceOptions maintenanceOptions, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient deregisterService(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient infoService(String name, Handler<AsyncResult<List<Service>>> resultHandler);

    @Fluent
    ConsulClient catalogServices(Handler<AsyncResult<List<Service>>> resultHandler);

    @Fluent
    ConsulClient localServices(Handler<AsyncResult<List<Service>>> resultHandler);

    @Fluent
    ConsulClient nodeServices(String nodeId, Handler<AsyncResult<List<Service>>> resultHandler);

    @Fluent
    ConsulClient localChecks(Handler<AsyncResult<List<CheckInfo>>> resultHandler);

    @Fluent
    ConsulClient registerCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient deregisterCheck(String id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient passCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient warnCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient failCheck(CheckOptions check, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient updateCheck(CheckInfo checkInfo, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    ConsulClient leaderStatus(Handler<AsyncResult<String>> resultHandler);

    @Fluent
    ConsulClient peersStatus(Handler<AsyncResult<List<String>>> resultHandler);

    /**
     * Initialize a new session
     *
     * @param idHandler will be provided with ID of new session
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient createSession(Handler<AsyncResult<String>> idHandler);

    /**
     * Initialize a new session
     *
     * @param options options used to create session
     * @param idHandler will be provided with ID of new session
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient createSessionWithOptions(SessionOptions options, Handler<AsyncResult<String>> idHandler);

    /**
     * Returns the requested session information
     *
     * @param id the ID of requested session
     * @param resultHandler will be provided with info of requested session
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient infoSession(String id, Handler<AsyncResult<Session>> resultHandler);

    /**
     * Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
     *
     * @param id the ID of session that should be renewed
     * @param resultHandler will be provided with info of renewed session
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient renewSession(String id, Handler<AsyncResult<Session>> resultHandler);

    /**
     * Returns the active sessions
     *
     * @param resultHandler will be provided with list of sessions
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient listSessions(Handler<AsyncResult<List<Session>>> resultHandler);

    /**
     * Returns the active sessions for a given node
     *
     * @param nodeId the ID of node
     * @param resultHandler will be provided with list of sessions
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient listNodeSessions(String nodeId, Handler<AsyncResult<List<Session>>> resultHandler);

    /**
     * Destroys the given session
     *
     * @param id the ID of session
     * @param resultHandler will be called when complete
     * @return reference to this, for fluency
     */
    @Fluent
    ConsulClient destroySession(String id, Handler<AsyncResult<Void>> resultHandler);

    /**
     * Close the client and release its resources
     */
    void close();
}
