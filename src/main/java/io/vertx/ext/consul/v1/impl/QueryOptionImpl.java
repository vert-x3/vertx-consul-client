package io.vertx.ext.consul.v1.impl;

import io.vertx.ext.consul.v1.QueryOptions;

import java.util.Map;

public class QueryOptionImpl implements QueryOptions {

  // Providing a datacenter overwrites the DC provided
  // by the Config
  private String datacenter;

  // AllowStale allows any Consul server (non-leader) to service
  // a read. This allows for lower latency and higher throughput
  private boolean allowStale;

  // RequireConsistent forces the read to be fully consistent.
  // This is more expensive but prevents ever performing a stale
  // read.
  private boolean requireConsistent;

  // UseCache requests that the agent cache results locally. See
  // https://www.consul.io/api/index.html#agent-caching for more details on the
  // semantics.
  private boolean useCache;

  // MaxAge limits how old a cached value will be returned if UseCache is true.
  // If there is a cached response that is older than the MaxAge, it is treated
  // as a cache miss and a new fetch invoked. If the fetch fails, the error is
  // returned. Clients that wish to allow for stale results on error can set
  // StaleIfError to a longer duration to change this behavior. It is ignored
  // if the endpoint supports background refresh caching. See
  // https://www.consul.io/api/index.html#agent-caching for more details.
  private long maxAgeSeconds;

  // StaleIfError specifies how stale the client will accept a cached response
  // if the servers are unavailable to fetch a fresh one. Only makes sense when
  // UseCache is true and MaxAge is set to a lower, non-zero value. It is
  // ignored if the endpoint supports background refresh caching. See
  // https://www.consul.io/api/index.html#agent-caching for more details.
  private long staleIfErrorSeconds;

  // WaitIndex is used to enable a blocking query. Waits
  // until the timeout or the next index is reached
  private long waitIndex;

  // WaitHash is used by some endpoints instead of WaitIndex to perform blocking
  // on state based on a hash of the response rather than a monotonic index.
  // This is required when the state being blocked on is not stored in Raft, for
  // example agent-local proxy configuration.
  private String waitHash;

  // WaitTime is used to bound the duration of a wait.
  // Defaults to that of the Config, but can be overridden.
  private long waitTimeMillis;

  // Token is used to provide a per-request ACL token
  // which overrides the agent's default token.
  private String token;

  // Near is used to provide a node name that will sort the results
  // in ascending order based on the estimated round trip time from
  // that node. Setting this to "_agent" will use the agent's node
  // for the sort.
  private String near;

  // NodeMeta is used to filter results by nodes with the given
  // metadata key/value pairs. Currently, only one key/value pair can
  // be provided for filtering.
  private Map<String, String> nodeMeta;

  // RelayFactor is used in keyring operations to cause responses to be
  // relayed back to the sender through N other random nodes. Must be
  // a value from 0 to 5 (inclusive).
  private byte relayFactor;

  // Connect filters prepared query execution to only include Connect-capable
  // services. This currently affects prepared query execution.
  private boolean connect;

  // Filter requests filtering data prior to it being returned. The string
  // is a go-bexpr compatible expression.
  private String filter;

  @Override
  public String datacenter() {
    return datacenter;
  }

  @Override
  public boolean allowStale() {
    return allowStale;
  }

  @Override
  public boolean requireConsistent() {
    return requireConsistent;
  }

  @Override
  public boolean useCache() {
    return useCache;
  }

  @Override
  public long maxAgeSeconds() {
    return maxAgeSeconds;
  }

  @Override
  public long staleIfErrorSeconds() {
    return staleIfErrorSeconds;
  }

  @Override
  public long waitIndex() {
    return waitIndex;
  }

  @Override
  public String waitHash() {
    return waitHash;
  }

  @Override
  public long waitTimeMillis() {
    return waitTimeMillis;
  }

  @Override
  public String token() {
    return token;
  }

  @Override
  public String near() {
    return near;
  }

  @Override
  public Map<String, String> nodeMeta() {
    return nodeMeta;
  }

  @Override
  public byte relayFactor() {
    return relayFactor;
  }

  @Override
  public boolean connect() {
    return connect;
  }

  @Override
  public String filter() {
    return filter;
  }

  @Override
  public QueryOptions setDatacenter(String datacenter) {
    this.datacenter = datacenter;
    return this;
  }

  @Override
  public QueryOptions setAllowStale(boolean allowStale) {
    this.allowStale = allowStale;
    return this;
  }

  @Override
  public QueryOptions setRequireConsistent(boolean requireConsistent) {
    this.requireConsistent = requireConsistent;
    return this;
  }

  @Override
  public QueryOptions setUseCache(boolean useCache) {
    this.useCache = useCache;
    return this;
  }

  @Override
  public QueryOptions setMaxAgeSeconds(long maxAgeSeconds) {
    this.maxAgeSeconds = maxAgeSeconds;
    return this;
  }

  @Override
  public QueryOptions setStaleIfErrorSeconds(long staleIfErrorSeconds) {
    this.staleIfErrorSeconds = staleIfErrorSeconds;
    return this;
  }

  @Override
  public QueryOptions setWaitIndex(long waitIndex) {
    this.waitIndex = waitIndex;
    return this;
  }

  @Override
  public QueryOptions setWaitHash(String waitHash) {
    this.waitHash = waitHash;
    return this;
  }

  @Override
  public QueryOptions setWaitTimeMillis(long waitTimeMillis) {
    this.waitTimeMillis = waitTimeMillis;
    return this;
  }

  @Override
  public QueryOptions setToken(String token) {
    this.token = token;
    return this;
  }

  @Override
  public QueryOptions setNear(String near) {
    this.near = near;
    return this;
  }

  @Override
  public QueryOptions setNodeMeta(Map<String, String> nodeMeta) {
    this.nodeMeta = nodeMeta;
    return this;
  }

  @Override
  public QueryOptions setRelayFactor(byte relayFactor) {
    this.relayFactor = relayFactor;
    return this;
  }

  @Override
  public QueryOptions setConnect(boolean connect) {
    this.connect = connect;
    return this;
  }

  @Override
  public QueryOptions setFilter(String filter) {
    this.filter = filter;
    return this;
  }
}
