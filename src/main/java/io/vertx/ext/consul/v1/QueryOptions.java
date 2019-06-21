package io.vertx.ext.consul.v1;

import io.vertx.codegen.annotations.VertxGen;

import java.util.Map;

@VertxGen
public interface QueryOptions {

  // Providing a datacenter overwrites the DC provided
  // by the Config
  String datacenter();

  // AllowStale allows any Consul server (non-leader) to service
  // a read. This allows for lower latency and higher throughput
  boolean allowStale();

  // RequireConsistent forces the read to be fully consistent.
  // This is more expensive but prevents ever performing a stale
  // read.
  boolean requireConsistent();

  // UseCache requests that the agent cache results locally. See
  // https://www.consul.io/api/index.html#agent-caching for more details on the
  // semantics.
  boolean useCache();

  // MaxAge limits how old a cached value will be returned if UseCache is true.
  // If there is a cached response that is older than the MaxAge, it is treated
  // as a cache miss and a new fetch invoked. If the fetch fails, the error is
  // returned. Clients that wish to allow for stale results on error can set
  // StaleIfError to a longer duration to change this behavior. It is ignored
  // if the endpoint supports background refresh caching. See
  // https://www.consul.io/api/index.html#agent-caching for more details.
  long maxAgeSeconds();

  // StaleIfError specifies how stale the client will accept a cached response
  // if the servers are unavailable to fetch a fresh one. Only makes sense when
  // UseCache is true and MaxAge is set to a lower, non-zero value. It is
  // ignored if the endpoint supports background refresh caching. See
  // https://www.consul.io/api/index.html#agent-caching for more details.
  long staleIfErrorSeconds();

  // WaitIndex is used to enable a blocking query. Waits
  // until the timeout or the next index is reached
  long waitIndex();

  // WaitHash is used by some endpoints instead of WaitIndex to perform blocking
  // on state based on a hash of the response rather than a monotonic index.
  // This is required when the state being blocked on is not stored in Raft, for
  // example agent-local proxy configuration.
  String waitHash();

  // WaitTime is used to bound the duration of a wait.
  // Defaults to that of the Config, but can be overridden.
  long waitTimeMillis();

  // Token is used to provide a per-request ACL token
  // which overrides the agent's default token.
  String token();

  // Near is used to provide a node name that will sort the results
  // in ascending order based on the estimated round trip time from
  // that node. Setting this to "_agent" will use the agent's node
  // for the sort.
  String near();

  // NodeMeta is used to filter results by nodes with the given
  // metadata key/value pairs. Currently, only one key/value pair can
  // be provided for filtering.
  Map<String, String> nodeMeta();

  // RelayFactor is used in keyring operations to cause responses to be
  // relayed back to the sender through N other random nodes. Must be
  // a value from 0 to 5 (inclusive).
  byte relayFactor();

  // Connect filters prepared query execution to only include Connect-capable
  // services. This currently affects prepared query execution.
  boolean connect();

  // Filter requests filtering data prior to it being returned. The string
  // is a go-bexpr compatible expression.
  String filter();

  QueryOptions setDatacenter(String datacenter);

  QueryOptions setAllowStale(boolean allowStale);

  QueryOptions setRequireConsistent(boolean requireConsistent);

  QueryOptions setUseCache(boolean useCache);

  QueryOptions setMaxAgeSeconds(long maxAgeSeconds);

  QueryOptions setStaleIfErrorSeconds(long staleIfErrorSeconds);

  QueryOptions setWaitIndex(long waitIndex);

  QueryOptions setWaitHash(String waitHash);

  QueryOptions setWaitTimeMillis(long waitTimeMillis);

  QueryOptions setToken(String token);

  QueryOptions setNear(String near);

  QueryOptions setNodeMeta(Map<String, String> nodeMeta);

  QueryOptions setRelayFactor(byte relayFactor);

  QueryOptions setConnect(boolean connect);

  QueryOptions setFilter(String filter);
}
