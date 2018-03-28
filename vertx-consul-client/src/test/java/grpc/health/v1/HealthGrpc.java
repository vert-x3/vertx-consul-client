package grpc.health.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.0)",
    comments = "Source: HealthCheck.proto")
public final class HealthGrpc {

  private HealthGrpc() {}

  private static <T> io.grpc.stub.StreamObserver<T> toObserver(final io.vertx.core.Handler<io.vertx.core.AsyncResult<T>> handler) {
    return new io.grpc.stub.StreamObserver<T>() {
      private volatile boolean resolved = false;
      @Override
      public void onNext(T value) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture(value));
        }
      }

      @Override
      public void onError(Throwable t) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.failedFuture(t));
        }
      }

      @Override
      public void onCompleted() {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture());
        }
      }
    };
  }

  public static final String SERVICE_NAME = "grpc.health.v1.Health";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCheckMethod()} instead. 
  public static final io.grpc.MethodDescriptor<grpc.health.v1.HealthCheck.HealthCheckRequest,
      grpc.health.v1.HealthCheck.HealthCheckResponse> METHOD_CHECK = getCheckMethod();

  private static volatile io.grpc.MethodDescriptor<grpc.health.v1.HealthCheck.HealthCheckRequest,
      grpc.health.v1.HealthCheck.HealthCheckResponse> getCheckMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<grpc.health.v1.HealthCheck.HealthCheckRequest,
      grpc.health.v1.HealthCheck.HealthCheckResponse> getCheckMethod() {
    io.grpc.MethodDescriptor<grpc.health.v1.HealthCheck.HealthCheckRequest, grpc.health.v1.HealthCheck.HealthCheckResponse> getCheckMethod;
    if ((getCheckMethod = HealthGrpc.getCheckMethod) == null) {
      synchronized (HealthGrpc.class) {
        if ((getCheckMethod = HealthGrpc.getCheckMethod) == null) {
          HealthGrpc.getCheckMethod = getCheckMethod = 
              io.grpc.MethodDescriptor.<grpc.health.v1.HealthCheck.HealthCheckRequest, grpc.health.v1.HealthCheck.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "grpc.health.v1.Health", "Check"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.health.v1.HealthCheck.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.health.v1.HealthCheck.HealthCheckResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new HealthMethodDescriptorSupplier("Check"))
                  .build();
          }
        }
     }
     return getCheckMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HealthStub newStub(io.grpc.Channel channel) {
    return new HealthStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HealthBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new HealthBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HealthFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new HealthFutureStub(channel);
  }

  /**
   * Creates a new vertx stub that supports all call types for the service
   */
  public static HealthVertxStub newVertxStub(io.grpc.Channel channel) {
    return new HealthVertxStub(channel);
  }

  /**
   */
  public static abstract class HealthImplBase implements io.grpc.BindableService {

    /**
     */
    public void check(grpc.health.v1.HealthCheck.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<grpc.health.v1.HealthCheck.HealthCheckResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCheckMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.health.v1.HealthCheck.HealthCheckRequest,
                grpc.health.v1.HealthCheck.HealthCheckResponse>(
                  this, METHODID_CHECK)))
          .build();
    }
  }

  /**
   */
  public static final class HealthStub extends io.grpc.stub.AbstractStub<HealthStub> {
    public HealthStub(io.grpc.Channel channel) {
      super(channel);
    }

    public HealthStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthStub(channel, callOptions);
    }

    /**
     */
    public void check(grpc.health.v1.HealthCheck.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<grpc.health.v1.HealthCheck.HealthCheckResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HealthBlockingStub extends io.grpc.stub.AbstractStub<HealthBlockingStub> {
    public HealthBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    public HealthBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.health.v1.HealthCheck.HealthCheckResponse check(grpc.health.v1.HealthCheck.HealthCheckRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HealthFutureStub extends io.grpc.stub.AbstractStub<HealthFutureStub> {
    public HealthFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    public HealthFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.health.v1.HealthCheck.HealthCheckResponse> check(
        grpc.health.v1.HealthCheck.HealthCheckRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request);
    }
  }

  /**
   */
  public static abstract class HealthVertxImplBase implements io.grpc.BindableService {

    /**
     */
    public void check(grpc.health.v1.HealthCheck.HealthCheckRequest request,
        io.vertx.core.Future<grpc.health.v1.HealthCheck.HealthCheckResponse> response) {
      asyncUnimplementedUnaryCall(getCheckMethod(), HealthGrpc.toObserver(response.completer()));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCheckMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                grpc.health.v1.HealthCheck.HealthCheckRequest,
                grpc.health.v1.HealthCheck.HealthCheckResponse>(
                  this, METHODID_CHECK)))
          .build();
    }
  }

  /**
   */
  public static final class HealthVertxStub extends io.grpc.stub.AbstractStub<HealthVertxStub> {
    public HealthVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    public HealthVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthVertxStub(channel, callOptions);
    }

    /**
     */
    public void check(grpc.health.v1.HealthCheck.HealthCheckRequest request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<grpc.health.v1.HealthCheck.HealthCheckResponse>> response) {
      asyncUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request, HealthGrpc.toObserver(response));
    }
  }

  private static final int METHODID_CHECK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HealthImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HealthImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK:
          serviceImpl.check((grpc.health.v1.HealthCheck.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<grpc.health.v1.HealthCheck.HealthCheckResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class VertxMethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HealthVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(HealthVertxImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK:
          serviceImpl.check((grpc.health.v1.HealthCheck.HealthCheckRequest) request,
              (io.vertx.core.Future<grpc.health.v1.HealthCheck.HealthCheckResponse>) io.vertx.core.Future.<grpc.health.v1.HealthCheck.HealthCheckResponse>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<grpc.health.v1.HealthCheck.HealthCheckResponse>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class HealthBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HealthBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.health.v1.HealthCheck.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Health");
    }
  }

  private static final class HealthFileDescriptorSupplier
      extends HealthBaseDescriptorSupplier {
    HealthFileDescriptorSupplier() {}
  }

  private static final class HealthMethodDescriptorSupplier
      extends HealthBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HealthMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HealthGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HealthFileDescriptorSupplier())
              .addMethod(getCheckMethod())
              .build();
        }
      }
    }
    return result;
  }
}
