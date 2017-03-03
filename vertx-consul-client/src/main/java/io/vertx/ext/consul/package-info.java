/**
 * = Vert.x Consul Client
 *
 * https://www.consul.io[Consul] is a tool for discovering and configuring services in your infrastructure.
 * A Vert.x client allowing applications to interact with a Consul system via blocking and non-blocking HTTP API.
 *
 * == Using Vert.x Consul Client
 *
 * To use this project, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 *
 * == Creating a client
 *
 * Just use factory method:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#exampleCreateDefault}
 * ----
 *
 * Also the client can be configured with an options.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#exampleCreateWithOptions}
 * ----
 *
 * The following configuration is supported by the consul client:
 *
 * `host`:: Consul host. Defaults to `localhost`
 * `port`:: Consul HTTP API port. Defaults to `8500`
 * `timeout`:: Sets the amount of time (in milliseconds) after which if the request does not return any data
 * within the timeout period an failure will be passed to the handler and the request will be closed.
 * `acl_token`:: The ACL token. When provided, the client will use this token when making requests to the Consul
 * by providing the "?token" query parameter. When not provided, the empty token, which maps to the 'anonymous'
 * ACL policy, is used.
 * `dc`:: The datacenter name. When provided, the client will use it when making requests to the Consul
 * by providing the "?dc" query parameter. When not provided, the datacenter of the consul agent is queried.
 *
 * ConsulClient options extends WebClientOptions from `vertx-web-client` module,
 * therefore a lot of settings are available. Please see the documentation.
 *
 * == Using the API
 *
 * The client API is represented by {@link io.vertx.ext.consul.ConsulClient}. The API is very similar to Consul's
 * HTTP API that described in https://www.consul.io/docs/agent/http.html[Consul API docs]
 *
 * === Blocking queries
 *
 * Certain endpoints support a feature called a "blocking query." A blocking query is used to wait for a potential
 * change using long polling. Any endpoint that supports blocking also provide a unique identifier (index) representing
 * the current state of the requested resource. The following configuration is used to perform blocking queries:
 *
 * `index`:: value indicating that the client wishes to wait for any changes subsequent to that index.
 * `wait`:: parameter specifying a maximum duration for the blocking request. This is limited to 10 minutes.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#blockingOptions}
 * ----
 *
 * A critical note is that the return of a blocking request is *no guarantee* of a change.
 * It is possible that the timeout was reached or that there was an idempotent write that does not affect the result of the query.
 *
 * == Key/Value Store
 *
 * The KV endpoints are used to access Consul's simple key/value store, useful for storing service configuration or other metadata.
 * The following endpoints are supported:
 *
 * * To manage updates of individual keys, deletes of individual keys or key prefixes, and fetches of individual keys or key prefixes
 * * To manage updates or fetches of multiple keys inside a single, atomic transaction
 *
 * === Get key-value pair from store
 *
 * Consul client can return the value for certain key
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#getValue}
 * ----
 *
 * ...or it can return all key-value pairs with the given prefix
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#getValues}
 * ----
 *
 * The returned key-value object contains these fields (see https://www.consul.io/docs/agent/http/kv.html#single[documentation]):
 *
 * `createIndex`:: the internal index value that represents when the entry was created.
 * `modifyIndex`:: the last index that modified this key
 * `lockIndex`:: the number of times this key has successfully been acquired in a lock
 * `key`:: the key
 * `flags`:: the flags attached to this entry. Clients can choose to use this however makes
 * sense for their application
 * `value`:: the value
 * `session`:: the session that owns the lock
 *
 * The modify index can be used for blocking queries:
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#blocking}
 * ----
 *
 * === Put key-value pair to store
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#put}
 * ----
 *
 * Put request with options also accepted
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#putWithOptions}
 * ----
 *
 * The list of the query options that can be used with a `PUT` request:
 *
 * `flags`:: This can be used to specify an unsigned value between `0` and `2^64^-1`.
 * Clients can choose to use this however makes sense for their application.
 * `casIndex`:: This flag is used to turn the PUT into a Check-And-Set operation. This is very useful as a building
 * block for more complex synchronization primitives. If the index is `0`, Consul will only put the key if it does
 * not already exist. If the index is non-zero, the key is only set if the index matches the ModifyIndex of that key.
 * `acquireSession`:: This flag is used to turn the PUT into a lock acquisition operation. This is useful
 * as it allows leader election to be built on top of Consul. If the lock is not held and the session is valid,
 * this increments the LockIndex and sets the Session value of the key in addition to updating the key contents.
 * A key does not need to exist to be acquired. If the lock is already held by the given session, then the LockIndex
 * is not incremented but the key contents are updated. This lets the current lock holder update the key contents
 * without having to give up the lock and reacquire it.
 * `releaseSession`:: This flag is used to turn the PUT into a lock release operation. This is useful when paired
 * with `acquireSession` as it allows clients to yield a lock. This will leave the LockIndex unmodified but will clear
 * the associated Session of the key. The key must be held by this session to be unlocked.
 *
 * === Transactions
 *
 * When connected to Consul 0.7 and later, client allows to manage updates or fetches of multiple keys
 * inside a single, atomic transaction. KV is the only available operation type, though other types of operations
 * may be added in future versions of Consul to be mixed with key/value operations
 * (see https://www.consul.io/docs/agent/http/kv.html#txn[documentation]).
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#transaction}
 * ----
 *
 * === Delete key-value pair
 *
 * At last, Consul client allows to delete key-value pair from store:
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#deleteValue}
 * ----
 *
 * ...or all key-value pairs with corresponding key prefix
 *
 * [source,$lang]
 * ----
 * {@link examples.KV#deleteValues}
 * ----
 *
 * == Services
 *
 * One of the main goals of service discovery is to provide a catalog of available services.
 * To that end, the agent provides a simple service definition format to declare the availability of a service
 * and to potentially associate it with a health check.
 *
 * === Service registering
 *
 * A service definition must include a `name` and may optionally provide an `id`, `tags`, `address`, `port`, and `checks`.
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#servicesOpts}
 * ----
 *
 * `name`:: the name of service
 * `id`:: the `id` is set to the `name` if not provided. It is required that all services have a unique ID per node,
 * so if names might conflict then unique IDs should be provided.
 * `tags`:: list of values that are opaque to Consul but can be used to distinguish between primary or secondary nodes,
 * different versions, or any other service level labels.
 * `address`:: used to specify a service-specific IP address. By default, the IP address of the agent is used,
 * and this does not need to be provided.
 * `port`:: used as well to make a service-oriented architecture simpler to configure; this way,
 * the address and port of a service can be discovered.
 * `checks`:: associated health checks
 *
 * These options used to register service in catalog:
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#register}
 * ----
 *
 * === Service discovery
 *
 * Consul client allows to obtain actual list of the nodes providing a service
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#discovery}
 * ----
 *
 * It is possible to obtain this list with the statuses of the associated health checks.
 * The result can be filtered by check status.
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#health}
 * ----
 *
 * There are additional parameters for services queries
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#queryOpts}
 * ----
 *
 * `tag`:: by default, all nodes matching the service are returned.
 * The list can be filtered by tag using the `tag` query parameter
 * `near`:: adding the optional `near` parameter with a node name will sort the node list in ascending order
 * based on the estimated round trip time from that node. Passing `near`=`_agent` will use the agent's node for the sort.
 * `blockingOptions`:: the blocking qyery options
 *
 * Then the request should look like
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#queryWithOptions}
 * ----
 *
 * === Deregister service
 *
 * Service can be deregistered by its ID:
 *
 * [source,$lang]
 * ----
 * {@link examples.Services#deregister}
 * ----
 *
 * == Health Checks
 *
 * One of the primary roles of the agent is management of system-level and application-level health checks.
 * A health check is considered to be application-level if it is associated with a service.
 * If not associated with a service, the check monitors the health of the entire node.
 *
 * [source,$lang]
 * ----
 * {@link examples.Health#checkOpts}
 * ----
 *
 * The list of check options that supported by Consul client is:
 *
 * `id`:: the check ID
 * `name`:: check name
 * `script`:: local path to checking script. Also you should set checking interval
 * `http`:: HTTP address to check. Also you should set checking interval
 * `ttl`:: Time to Live of check
 * `tcp`:: TCP address to check. Also you should set checking interval
 * `interval`:: checking interval in Go's time format which is sequence of decimal numbers,
 * each with optional fraction and a unit suffix, such as "300ms", "-1.5h" or "2h45m".
 * Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h"
 * `notes`:: the check notes
 * `serviceId`:: the service ID to associate the registered check with an existing service provided by the agent.
 * `deregisterAfter`:: deregister timeout. This is optional field, which is a timeout in the same time format as Interval and TTL.
 * If a check is associated with a service and has the critical state for more than this configured value,
 * then its associated service (and all of its associated checks) will automatically be deregistered.
 * The minimum timeout is 1 minute, and the process that reaps critical services runs every 30 seconds,
 * so it may take slightly longer than the configured timeout to trigger the deregistration.
 * This should generally be configured with a timeout that's much, much longer than any expected recoverable outage
 * for the given service.
 * `status`:: the check status to specify the initial state of the health check
 *
 * The `Name` field is mandatory, as is one of `Script`, `HTTP`, `TCP` or `TTL`. `Script`, `TCP` and `HTTP`
 * also require that `Interval` be set. If an `ID` is not provided, it is set to `Name`.
 * You cannot have duplicate ID entries per agent, so it may be necessary to provide an ID.
 *
 * [source,$lang]
 * ----
 * {@link examples.Health#tcpHealth}
 * ----
 *
 * == Events
 *
 * The Consul provides a mechanism to fire a custom user event to an entire datacenter.
 * These events are opaque to Consul, but they can be used to build scripting infrastructure to do automated deploys,
 * restart services, or perform any other orchestration action.
 *
 * To send user event only its name is required
 *
 * [source,$lang]
 * ----
 * {@link examples.Events#sendWithName}
 * ----
 *
 * Also additional options can be specified.
 *
 * `node`:: regular expression to filter recipients by node name
 * `service`:: regular expression to filter recipients by service
 * `tag`:: regular expression to filter recipients by tag
 * `payload`:: an optional body of the event.
 * The body contents are opaque to Consul and become the "payload" of the event
 *
 * [source,$lang]
 * ----
 * {@link examples.Events#sendWithOptions}
 * ----
 *
 * The Consul Client supports queries for obtain the most recent events known by the agent. Events are broadcast using
 * the gossip protocol, so they have no global ordering nor do they make a promise of delivery. Agents only buffer
 * the most recent entries. The current buffer size is 256, but this value could change in the future.
 *
 * [source,$lang]
 * ----
 * {@link examples.Events#list}
 * ----
 *
 * The Consul Index can be used to prepare blocking requests:
 *
 * [source,$lang]
 * ----
 * {@link examples.Events#listWithOptions}
 * ----
 *
 * == Sessions
 *
 * Consul provides a session mechanism which can be used to build distributed locks.
 * Sessions act as a binding layer between nodes, health checks, and key/value data.
 * When a session is constructed, a node name, a list of health checks, a behavior, a TTL, and a lock-delay
 * may be provided.
 *
 * [source,$lang]
 * ----
 * {@link examples.Sessions#sessionOpts}
 * ----
 *
 * `lockDelay`:: can be specified as a duration string using an 's' suffix for seconds. The default is '15s'.
 * `name`:: can be used to provide a human-readable name for the Session.
 * `node`:: must refer to a node that is already registered, if specified. By default, the agent's own node name is used.
 * `checks`:: is used to provide a list of associated health checks. It is highly recommended that,
 * if you override this list, you include the default `serfHealth`.
 * `behavior`:: can be set to either `release` or `delete`. This controls the behavior when a session is invalidated.
 * By default, this is `release`, causing any locks that are held to be released. Changing this to `delete` causes
 * any locks that are held to be deleted. `delete` is useful for creating ephemeral key/value entries.
 * `ttl`:: is a duration string, and like `LockDelay` it can use s as a suffix for seconds. If specified,
 * it must be between 10s and 86400s currently. When provided, the session is invalidated if it is not renewed before the TTL expires.
 *
 * For full info see https://www.consul.io/docs/internals/sessions.html[Consul Sessions internals]
 *
 * The newly constructed session is provided with a named ID that can be used to identify it.
 * This ID can be used with the KV store to acquire locks: advisory mechanisms for mutual exclusion.
 *
 * [source,$lang]
 * ----
 * {@link examples.Sessions#create}
 * ----
 *
 * And also to destroy it
 *
 * [source,$lang]
 * ----
 * {@link examples.Sessions#destroy}
 * ----
 *
 * Lists sessions belonging to a node
 *
 * [source,$lang]
 * ----
 * {@link examples.Sessions#nodeSessions}
 * ----
 *
 * All of the read session endpoints support blocking queries and all consistency modes.
 *
 * [source,$lang]
 * ----
 * {@link examples.Sessions#blockingQuery}
 * ----
 *
 * == Nodes in datacenter
 *
 * [source,$lang]
 * ----
 * {@link examples.Nodes#catalogNodes}
 * ----
 *
 * This endpoint supports blocking queries and sorting by distance from specified node
 *
 * [source,$lang]
 * ----
 * {@link examples.Nodes#blockingQuery}
 * ----
 *
 * == Watches
 *
 * TBD
 * [source,$lang]
 * ----
 * {@link examples.Watches#watchKey}
 * ----
 */
@Document(fileName = "index.adoc")
@ModuleGen(name = "vertx-consul", groupPackage = "io.vertx")
package io.vertx.ext.consul;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
