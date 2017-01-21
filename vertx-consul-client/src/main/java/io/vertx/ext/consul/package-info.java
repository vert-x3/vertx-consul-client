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
 * === Key/Value Store
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#kv}
 * ----
 * The modify index can be used for blocking requests:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#kvBlocking}
 * ----
 * === Health Checks
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#tcpHealth}
 * ----
 *
 * === Services
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#services}
 * ----
 *
 * === Events
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#events}
 * ----
 *
 * === Sessions
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#sessions}
 * ----
 *
 * === Nodes in cluster
 *
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
