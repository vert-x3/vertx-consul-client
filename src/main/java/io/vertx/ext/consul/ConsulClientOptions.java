/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.consul;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.VertxException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.*;
import io.vertx.ext.web.client.WebClientOptions;

import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Options used to create Consul client.
 *
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
@DataObject(generateConverter = true)
public class ConsulClientOptions extends WebClientOptions {

  private static final String CONSUL_DEFAULT_HOST = "localhost";
  private static final int CONSUL_DEFAULT_PORT = 8500;

  private String aclToken;
  private String dc;
  private long timeoutMs;

  /**
   * Default constructor
   */
  public ConsulClientOptions() {
    super();
    setDefaultHost(CONSUL_DEFAULT_HOST);
    setDefaultPort(CONSUL_DEFAULT_PORT);
  }

  /**
   * Copy constructor
   *
   * @param options the one to copy
   */
  public ConsulClientOptions(ConsulClientOptions options) {
    super(options);
    setHost(options.getHost());
    setPort(options.getPort());
    setAclToken(options.getAclToken());
    setDc(options.getDc());
    setTimeout(options.getTimeout());
  }

  /**
   * Constructor from JSON
   *
   * @param json the JSON
   */
  public ConsulClientOptions(JsonObject json) {
    super(json);
    ConsulClientOptionsConverter.fromJson(json, this);
    if (json.getValue("host") instanceof String) {
      setHost((String)json.getValue("host"));
    } else {
      setHost(CONSUL_DEFAULT_HOST);
    }
    if (json.getValue("port") instanceof Number) {
      setPort(((Number)json.getValue("port")).intValue());
    } else {
      setPort(CONSUL_DEFAULT_PORT);
    }
  }

  /**
   * Constructor from {@link URI}.
   * The datacenter and the acl token can be defined in the query; the scheme will be ignored.
   *
   * For example:
   * <p><code>
   * consul://consul.example.com/?dc=dc1&amp;acl=00000000-0000-0000-0000-000000000000
   * </code></p>
   * @param uri the URI
   */
  public ConsulClientOptions(URI uri) {
    setHost(uri.getHost());
    setPort(uri.getPort() < 0 ? CONSUL_DEFAULT_PORT : uri.getPort());
    Map<String, List<String>> params = params(uri);
    setDc(getParam(params, "dc"));
    setAclToken(getParam(params, "acl", "aclToken"));
  }

  private static String getParam(Map<String, List<String>> params, String... keyVariants) {
    for (String key : keyVariants) {
      if (params.containsKey(key)) {
        List<String> values = params.get(key);
        return values.get(0);
      }
    }
    return null;
  }

  private static Map<String, List<String>> params(URI uri) {
    if (uri.getQuery() == null || uri.getQuery().isEmpty()) {
      return Collections.emptyMap();
    }
    final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
    final String[] pairs = uri.getQuery().split("&");
    for (String pair : pairs) {
      try {
        final int idx = pair.indexOf("=");
        final String key = idx > 0
          ? URLDecoder.decode(pair.substring(0, idx), "UTF-8")
          : pair;
        final String value = idx > 0 && pair.length() > idx + 1
          ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
          : null;
        List<String> list = queryPairs.computeIfAbsent(key, k -> new ArrayList<>());
        list.add(value);
      } catch (Exception e) {
        throw new VertxException(e);
      }
    }
    return queryPairs;
  }

  /**
   * Convert to JSON
   *
   * @return the JSON
   */
  public JsonObject toJson() {
    JsonObject json = super.toJson();
    ConsulClientOptionsConverter.toJson(this, json);
    if (getHost() != null) {
      json.put("host", getHost());
    }
    json.put("port", getPort());
    return json;
  }

  /**
   * Get Consul host.
   *
   * @return consul host
   */
  @GenIgnore
  public String getHost() {
    return getDefaultHost();
  }

  /**
   * Get Consul HTTP API port.
   *
   * @return consul port
   */
  @GenIgnore
  public int getPort() {
    return getDefaultPort();
  }

  /**
   * Get the ACL token.
   *
   * @return the ACL token.
   */
  public String getAclToken() {
    return aclToken;
  }

  /**
   * Get the datacenter name
   *
   * @return the datacenter name
   */
  public String getDc() {
    return dc;
  }

  /**
   * Get timeout in milliseconds
   *
   * @return timeout in milliseconds
   */
  public long getTimeout() {
    return timeoutMs;
  }

  /**
   * Set Consul host. Defaults to `localhost`
   *
   * @param host consul host
   * @return reference to this, for fluency
   */
  @GenIgnore
  public ConsulClientOptions setHost(String host) {
    setDefaultHost(host);
    return this;
  }

  /**
   * Set Consul HTTP API port. Defaults to `8500`
   *
   * @param port Consul HTTP API port
   * @return reference to this, for fluency
   */
  @GenIgnore
  public ConsulClientOptions setPort(int port) {
    setDefaultPort(port);
    return this;
  }

  /**
   * Set the ACL token. When provided, the client will use this token when making requests to the Consul
   * by providing the "?token" query parameter. When not provided, the empty token, which maps to the 'anonymous'
   * ACL policy, is used.
   *
   * @param aclToken the ACL token
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setAclToken(String aclToken) {
    this.aclToken = aclToken;
    return this;
  }

  /**
   * Set the datacenter name. When provided, the client will use it when making requests to the Consul
   * by providing the "?dc" query parameter. When not provided, the datacenter of the consul agent is queried.
   *
   * @param dc the datacenter name
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setDc(String dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Sets the amount of time (in milliseconds) after which if the request does not return any data
   * within the timeout period an failure will be passed to the handler and the request will be closed.
   *
   * @param timeoutMs timeout in milliseconds
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setTimeout(long timeoutMs) {
    this.timeoutMs = timeoutMs;
    return this;
  }

  /**
   * Set the TCP send buffer size
   *
   * @param sendBufferSize  the buffers size, in bytes
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setSendBufferSize(int sendBufferSize) {
    return (ConsulClientOptions) super.setSendBufferSize(sendBufferSize);
  }

  /**
   * Set the TCP receive buffer size
   *
   * @param receiveBufferSize  the buffers size, in bytes
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setReceiveBufferSize(int receiveBufferSize) {
    return (ConsulClientOptions) super.setReceiveBufferSize(receiveBufferSize);
  }

  /**
   * Set the value of reuse address
   * @param reuseAddress  the value of reuse address
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setReuseAddress(boolean reuseAddress) {
    return (ConsulClientOptions) super.setReuseAddress(reuseAddress);
  }

  /**
   * Set the value of reuse port.
   * <p/>
   * This is only supported by native transports.
   *
   * @param reusePort  the value of reuse port
   * @return a reference to this, so the API can be used fluently
   */
  public ConsulClientOptions setReusePort(boolean reusePort) {
    return (ConsulClientOptions) super.setReusePort(reusePort);
  }

  /**
   * Set the value of traffic class
   *
   * @param trafficClass  the value of traffic class
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setTrafficClass(int trafficClass) {
    return (ConsulClientOptions) super.setTrafficClass(trafficClass);
  }

  /**
   * Set whether TCP no delay is enabled
   *
   * @param tcpNoDelay true if TCP no delay is enabled (Nagle disabled)
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setTcpNoDelay(boolean tcpNoDelay) {
    return (ConsulClientOptions) super.setTcpNoDelay(tcpNoDelay);
  }

  /**
   * Set whether TCP keep alive is enabled
   *
   * @param tcpKeepAlive true if TCP keep alive is enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setTcpKeepAlive(boolean tcpKeepAlive) {
    return (ConsulClientOptions) super.setTcpKeepAlive(tcpKeepAlive);
  }

  /**
   * Enable the {@code TCP_CORK} option - only with linux native transport.
   *
   * @param tcpCork the cork value
   * @return a reference to this, so the API can be used fluently
   */
  public ConsulClientOptions setTcpCork(boolean tcpCork) {
    return (ConsulClientOptions) super.setTcpCork(tcpCork);
  }

  /**
   * Enable the {@code TCP_QUICKACK} option - only with linux native transport.
   *
   * @param tcpQuickAck the quick ack value
   * @return a reference to this, so the API can be used fluently
   */
  public ConsulClientOptions setTcpQuickAck(boolean tcpQuickAck) {
    return (ConsulClientOptions) super.setTcpQuickAck(tcpQuickAck);
  }

  /**
   * Enable the {@code TCP_FASTOPEN} option - only with linux native transport.
   *
   * @param tcpFastOpen the fast open value
   * @return a reference to this, so the API can be used fluently
   */
  public ConsulClientOptions setTcpFastOpen(boolean tcpFastOpen) {
    return (ConsulClientOptions) super.setTcpFastOpen(tcpFastOpen);
  }

  /**
   * Set whether SO_linger keep alive is enabled
   *
   * @param soLinger true if SO_linger is enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setSoLinger(int soLinger) {
    return (ConsulClientOptions) super.setSoLinger(soLinger);
  }

  /**
   * Set whether Netty pooled buffers are enabled
   *
   * @param usePooledBuffers true if pooled buffers enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setUsePooledBuffers(boolean usePooledBuffers) {
    return (ConsulClientOptions) super.setUsePooledBuffers(usePooledBuffers);
  }

  /**
   * Set the idle timeout, in seconds. zero means don't timeout.
   * This determines if a connection will timeout and be closed if no data is received within the timeout.
   *
   * @param idleTimeout  the timeout, in seconds
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setIdleTimeout(int idleTimeout) {
    return (ConsulClientOptions) super.setIdleTimeout(idleTimeout);
  }

  /**
   * Set the idle timeout unit. If not specified, default is seconds.
   *
   * @param idleTimeoutUnit specify time unit.
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setIdleTimeoutUnit(TimeUnit idleTimeoutUnit) {
    return (ConsulClientOptions) super.setIdleTimeoutUnit(idleTimeoutUnit);
  }

  /**
   * Set whether SSL/TLS is enabled
   *
   * @param ssl  true if enabled
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setSsl(boolean ssl) {
    super.setSsl(ssl);
    return this;
  }

  /**
   * Set the key/cert options.
   *
   * @param options the key store options
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setKeyCertOptions(KeyCertOptions options) {
    return (ConsulClientOptions) super.setKeyCertOptions(options);
  }

  /**
   * Set the key/cert options in jks format, aka Java keystore.
   * @param options the key store in jks format
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setKeyStoreOptions(JksOptions options) {
    return (ConsulClientOptions) super.setKeyStoreOptions(options);
  }

  /**
   * Set the key/cert options in pfx format.
   * @param options the key cert options in pfx format
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setPfxKeyCertOptions(PfxOptions options) {
    return (ConsulClientOptions) super.setPfxKeyCertOptions(options);
  }

  /**
   * Set the trust options.
   * @param options the trust options
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setTrustOptions(TrustOptions options) {
    return (ConsulClientOptions) super.setTrustOptions(options);
  }

  /**
   * Set the key/cert store options in pem format.
   * @param options the options in pem format
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setPemKeyCertOptions(PemKeyCertOptions options) {
    return (ConsulClientOptions) super.setPemKeyCertOptions(options);
  }

  /**
   * Set the trust options in jks format, aka Java truststore
   * @param options the trust options in jks format
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setTrustStoreOptions(JksOptions options) {
    return (ConsulClientOptions) super.setTrustStoreOptions(options);
  }

  /**
   * Set the trust options in pfx format
   * @param options the trust options in pfx format
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setPfxTrustOptions(PfxOptions options) {
    return (ConsulClientOptions) super.setPfxTrustOptions(options);
  }

  /**
   * Set whether all server certificates should be trusted
   *
   * @param trustAll true if all should be trusted
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setTrustAll(boolean trustAll) {
    super.setTrustAll(trustAll);
    return this;
  }

  /**
   * Set the trust options.
   *
   * @param pemTrustOptions the trust options
   * @return reference to this, for fluency
   */
  public ConsulClientOptions setPemTrustOptions(PemTrustOptions pemTrustOptions) {
    super.setPemTrustOptions(pemTrustOptions);
    return this;
  }

  /**
   * Set the connect timeout
   *
   * @param connectTimeout  connect timeout, in ms
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setConnectTimeout(int connectTimeout) {
    return (ConsulClientOptions) super.setConnectTimeout(connectTimeout);
  }

  /**
   * Set the maximum pool size for connections
   *
   * @param maxPoolSize  the maximum pool size
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxPoolSize(int maxPoolSize) {
    return (ConsulClientOptions) super.setMaxPoolSize(maxPoolSize);
  }

  /**
   * Set a client limit of the number concurrent streams for each HTTP/2 connection, this limits the number
   * of streams the client can create for a connection. The effective number of streams for a
   * connection is the min of this value and the server's initial settings.
   * <p/>
   * Setting the value to {@code -1} means to use the value sent by the server's initial settings.
   * {@code -1} is the default value.
   *
   * @param limit the maximum concurrent for an HTTP/2 connection
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setHttp2MultiplexingLimit(int limit) {
    return (ConsulClientOptions) super.setHttp2MultiplexingLimit(limit);
  }

  /**
   * Set the maximum pool size for HTTP/2 connections
   *
   * @param max  the maximum pool size
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setHttp2MaxPoolSize(int max) {
    return (ConsulClientOptions) super.setHttp2MaxPoolSize(max);
  }

  /**
   * Set the default HTTP/2 connection window size. It overrides the initial window
   * size set by {@link Http2Settings#getInitialWindowSize}, so the connection window size
   * is greater than for its streams, in order the data throughput.
   * <p/>
   * A value of {@code -1} reuses the initial window size setting.
   *
   * @param http2ConnectionWindowSize the window size applied to the connection
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setHttp2ConnectionWindowSize(int http2ConnectionWindowSize) {
    return (ConsulClientOptions) super.setHttp2ConnectionWindowSize(http2ConnectionWindowSize);
  }

  /**
   * Set whether keep alive is enabled on the client
   *
   * @param keepAlive  true if enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setKeepAlive(boolean keepAlive) {
    return (ConsulClientOptions) super.setKeepAlive(keepAlive);
  }

  /**
   * Set whether pipe-lining is enabled on the client
   *
   * @param pipelining  true if enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setPipelining(boolean pipelining) {
    return (ConsulClientOptions) super.setPipelining(pipelining);
  }

  /**
   * Set the limit of pending requests a pipe-lined HTTP/1 connection can send.
   *
   * @param limit the limit of pending requests
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setPipeliningLimit(int limit) {
    return (ConsulClientOptions) super.setPipeliningLimit(limit);
  }

  /**
   * Set whether hostname verification is enabled
   *
   * @param verifyHost  true if enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setVerifyHost(boolean verifyHost) {
    return (ConsulClientOptions) super.setVerifyHost(verifyHost);
  }

  /**
   * Set whether compression is enabled
   *
   * @param tryUseCompression  true if enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setTryUseCompression(boolean tryUseCompression) {
    return (ConsulClientOptions) super.setTryUseCompression(tryUseCompression);
  }

  /**
   * Set true when the client wants to skip frame masking.
   * You may want to set it true on server by server websocket communication: In this case you are by passing RFC6455 protocol.
   * It's false as default.
   *
   * @param sendUnmaskedFrames  true if enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setSendUnmaskedFrames(boolean sendUnmaskedFrames) {
    return (ConsulClientOptions) super.setSendUnmaskedFrames(sendUnmaskedFrames);
  }

  /**
   * Set the max websocket frame size
   *
   * @param maxWebsocketFrameSize  the max frame size, in bytes
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxWebsocketFrameSize(int maxWebsocketFrameSize) {
    return (ConsulClientOptions) super.setMaxWebsocketFrameSize(maxWebsocketFrameSize);
  }

  /**
   * Set the default host name to be used by this client in requests if none is provided when making the request.
   *
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setDefaultHost(String defaultHost) {
    return (ConsulClientOptions) super.setDefaultHost(defaultHost);
  }

  /**
   * Set the default port to be used by this client in requests if none is provided when making the request.
   *
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setDefaultPort(int defaultPort) {
    return (ConsulClientOptions) super.setDefaultPort(defaultPort);
  }

  /**
   * Set the protocol version.
   *
   * @param protocolVersion the protocol version
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setProtocolVersion(HttpVersion protocolVersion) {
    return (ConsulClientOptions) super.setProtocolVersion(protocolVersion);
  }

  /**
   * Set the maximum HTTP chunk size
   * @param maxChunkSize the maximum chunk size
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxChunkSize(int maxChunkSize) {
    return (ConsulClientOptions) super.setMaxChunkSize(maxChunkSize);
  }

  /**
   * Set the maximum length of the initial line for HTTP/1.x (e.g. {@code "HTTP/1.1 200 OK"})
   *
   * @param maxInitialLineLength the new maximum initial length
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxInitialLineLength(int maxInitialLineLength) {
    return (ConsulClientOptions) super.setMaxInitialLineLength(maxInitialLineLength);
  }

  /**
   * Set the maximum length of all headers for HTTP/1.x .
   *
   * @param maxHeaderSize the new maximum length
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxHeaderSize(int maxHeaderSize) {
    return (ConsulClientOptions) super.setMaxHeaderSize(maxHeaderSize);
  }

  /**
   * Set the maximum requests allowed in the wait queue, any requests beyond the max size will result in
   * a ConnectionPoolTooBusyException.  If the value is set to a negative number then the queue will be unbounded.
   * @param maxWaitQueueSize the maximum number of waiting requests
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxWaitQueueSize(int maxWaitQueueSize) {
    return (ConsulClientOptions) super.setMaxWaitQueueSize(maxWaitQueueSize);
  }

  /**
   * Set the HTTP/2 connection settings immediately sent by to the server when the client connects.
   *
   * @param settings the settings value
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setInitialSettings(Http2Settings settings) {
    return (ConsulClientOptions) super.setInitialSettings(settings);
  }

  /**
   * Set the ALPN usage.
   *
   * @param useAlpn true when Application-Layer Protocol Negotiation should be used
   */
  @Override
  public ConsulClientOptions setUseAlpn(boolean useAlpn) {
    return (ConsulClientOptions) super.setUseAlpn(useAlpn);
  }

  /**
   * Set to use SSL engine implementation to use.
   *
   * @param sslEngineOptions the ssl engine to use
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setSslEngineOptions(SSLEngineOptions sslEngineOptions) {
    return (ConsulClientOptions) super.setSslEngineOptions(sslEngineOptions);
  }

  @Override
  public ConsulClientOptions setJdkSslEngineOptions(JdkSSLEngineOptions sslEngineOptions) {
    return (ConsulClientOptions) super.setJdkSslEngineOptions(sslEngineOptions);
  }

  @Override
  public ConsulClientOptions setOpenSslEngineOptions(OpenSSLEngineOptions sslEngineOptions) {
    return (ConsulClientOptions) super.setOpenSslEngineOptions(sslEngineOptions);
  }

  /**
   * Set the list of protocol versions to provide to the server during the Application-Layer Protocol Negotiation.
   * When the list is empty, the client provides a best effort list according to {@link #setProtocolVersion}:
   *
   * <ul>
   *   <li>{@link HttpVersion#HTTP_2}: [ "h2", "http/1.1" ]</li>
   *   <li>otherwise: [{@link #getProtocolVersion()}]</li>
   * </ul>
   *
   * @param alpnVersions the versions
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setAlpnVersions(List<HttpVersion> alpnVersions) {
    return (ConsulClientOptions) super.setAlpnVersions(alpnVersions);
  }

  /**
   * Set to {@code true} when an <i>h2c</i> connection is established using an HTTP/1.1 upgrade request, and {@code false}
   * when an <i>h2c</i> connection is established directly (with prior knowledge).
   *
   * @param value the upgrade value
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setHttp2ClearTextUpgrade(boolean value) {
    return (ConsulClientOptions) super.setHttp2ClearTextUpgrade(value);
  }

  /**
   * Set to {@code maxRedirects} the maximum number of redirection a request can follow.
   *
   * @param maxRedirects the maximum number of redirection
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxRedirects(int maxRedirects) {
    return (ConsulClientOptions) super.setMaxRedirects(maxRedirects);
  }

  /**
   * Set the metrics name identifying the reported metrics, useful for grouping metrics
   * with the same name.
   *
   * @param metricsName the metrics name
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMetricsName(String metricsName) {
    return (ConsulClientOptions) super.setMetricsName(metricsName);
  }

  /**
   * Set proxy options for connections via CONNECT proxy (e.g. Squid) or a SOCKS proxy.
   *
   * @param proxyOptions proxy options object
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setProxyOptions(ProxyOptions proxyOptions) {
    return (ConsulClientOptions) super.setProxyOptions(proxyOptions);
  }

  /**
   * Set the local interface to bind for network connections. When the local address is null,
   * it will pick any local address, the default local address is null.
   *
   * @param localAddress the local address
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setLocalAddress(String localAddress) {
    return (ConsulClientOptions) super.setLocalAddress(localAddress);
  }

  /**
   * Set to true to enabled network activity logging: Netty's pipeline is configured for logging on Netty's logger.
   *
   * @param logActivity true for logging the network activity
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setLogActivity(boolean logActivity) {
    return (ConsulClientOptions) super.setLogActivity(logActivity);
  }

  /**
   * Sets whether the Web Client should send a user agent header. Defaults to true.
   *
   * @param userAgentEnabled true to send a user agent header, false otherwise
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setUserAgentEnabled(boolean userAgentEnabled) {
    return (ConsulClientOptions) super.setUserAgentEnabled(userAgentEnabled);
  }

  /**
   * Sets the Web Client user agent header. Defaults to Vert.x-WebClient/&lt;version&gt;.
   *
   * @param userAgent user agent header value
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setUserAgent(String userAgent) {
    return (ConsulClientOptions) super.setUserAgent(userAgent);
  }

  /**
   * Configure the default behavior of the client to follow HTTP {@code 30x} redirections.
   *
   * @param followRedirects true when a redirect is followed
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setFollowRedirects(boolean followRedirects) {
    return (ConsulClientOptions) super.setFollowRedirects(followRedirects);
  }

  /**
   * Set the max websocket message size
   *
   * @param maxWebsocketMessageSize  the max message size, in bytes
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setMaxWebsocketMessageSize(int maxWebsocketMessageSize) {
    return (ConsulClientOptions) super.setMaxWebsocketMessageSize(maxWebsocketMessageSize);
  }

  /**
   * Add an enabled cipher suite, appended to the ordered suites.
   *
   * @param suite  the suite
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions addEnabledCipherSuite(String suite) {
    return (ConsulClientOptions) super.addEnabledCipherSuite(suite);
  }

  /**
   * Add an enabled SSL/TLS protocols, appended to the ordered protocols.
   *
   * @param protocol  the SSL/TLS protocol do enabled
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions addEnabledSecureTransportProtocol(String protocol) {
    return (ConsulClientOptions) super.addEnabledSecureTransportProtocol(protocol);
  }

  /**
   * Add a CRL path
   * @param crlPath  the path
   * @return a reference to this, so the API can be used fluently
   * @throws NullPointerException
   */
  @Override
  public ConsulClientOptions addCrlPath(String crlPath) throws NullPointerException {
    return (ConsulClientOptions) super.addCrlPath(crlPath);
  }

  /**
   * Add a CRL value
   *
   * @param crlValue  the value
   * @return a reference to this, so the API can be used fluently
   * @throws NullPointerException
   */
  @Override
  public ConsulClientOptions addCrlValue(Buffer crlValue) throws NullPointerException {
    return (ConsulClientOptions) super.addCrlValue(crlValue);
  }

  /**
   * By default, the server name is only sent for Fully Qualified Domain Name (FQDN), setting
   * this property to {@code true} forces the server name to be always sent.
   *
   * @param forceSni true when the client should always use SNI on TLS/SSL connections
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setForceSni(boolean forceSni) {
    return (ConsulClientOptions) super.setForceSni(forceSni);
  }

  /**
   * set to {@code initialBufferSizeHttpDecoder} the initial buffer of the HttpDecoder.
   * @param decoderInitialBufferSize the initial buffer size
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  public ConsulClientOptions setDecoderInitialBufferSize(int decoderInitialBufferSize) {
    return (ConsulClientOptions) super.setDecoderInitialBufferSize(decoderInitialBufferSize);
  }

  @Override
  public ConsulClientOptions removeEnabledSecureTransportProtocol(String protocol) {
    return (ConsulClientOptions) super.removeEnabledSecureTransportProtocol(protocol);
  }

  @Override
  public ConsulClientOptions setHttp2KeepAliveTimeout(int keepAliveTimeout) {
    return (ConsulClientOptions) super.setHttp2KeepAliveTimeout(keepAliveTimeout);
  }

  @Override
  public ConsulClientOptions setKeepAliveTimeout(int keepAliveTimeout) {
    return (ConsulClientOptions) super.setKeepAliveTimeout(keepAliveTimeout);
  }

  @Override
  public ConsulClientOptions setPoolCleanerPeriod(int poolCleanerPeriod) {
    return (ConsulClientOptions) super.setPoolCleanerPeriod(poolCleanerPeriod);
  }

  @Override
  public ConsulClientOptions setEnabledSecureTransportProtocols(Set<String> enabledSecureTransportProtocols) {
    return (ConsulClientOptions) super.setEnabledSecureTransportProtocols(enabledSecureTransportProtocols);
  }


}
