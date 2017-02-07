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
 * == Health Checks
 *
 * TBD
 * [source,$lang]
 * ----
 * {@link examples.Examples#tcpHealth}
 * ----
 *
 * == Services
 *
 * TBD
 * [source,$lang]
 * ----
 * {@link examples.Examples#services}
 * ----
 *
 * == Events
 *
 * TBD
 * [source,$lang]
 * ----
 * {@link examples.Examples#events}
 * ----
 *
 * == Sessions
 *
 * TBD
 * [source,$lang]
 * ----
 * {@link examples.Examples#sessions}
 * ----
 *
 * == Nodes in cluster
 *
 * TBD
 * [source,$lang]
 * ----
 * {@link examples.Examples#nodes}
 * ----
 */
@Document(fileName = "index.adoc")
@ModuleGen(name = "vertx-consul", groupPackage = "io.vertx")
package io.vertx.ext.consul;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
