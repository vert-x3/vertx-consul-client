require 'vertx/vertx'
require 'vertx-consul/consul_client'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.consul.ConsulService
module VertxConsul
  class ConsulService < ::VertxConsul::ConsulClient
    # @private
    # @param j_del [::VertxConsul::ConsulService] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxConsul::ConsulService] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a proxy to a service that is deployed somewhere on the event bus
    # @param [::Vertx::Vertx] vertx the Vert.x instance
    # @param [String] address the address the service is listening on on the event bus
    # @return [::VertxConsul::ConsulService] the service
    def self.create_event_bus_proxy(vertx=nil,address=nil)
      if vertx.class.method_defined?(:j_del) && address.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtConsul::ConsulService.java_method(:createEventBusProxy, [Java::IoVertxCore::Vertx.java_class,Java::java.lang.String.java_class]).call(vertx.j_del,address),::VertxConsul::ConsulService)
      end
      raise ArgumentError, "Invalid arguments when calling create_event_bus_proxy(vertx,address)"
    end
    # @param [String] key 
    # @yield 
    # @return [self]
    def get_value(key=nil)
      if key.class == String && block_given?
        @j_del.java_method(:getValue, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(key,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_value(key)"
    end
    # @param [String] key 
    # @yield 
    # @return [self]
    def delete_value(key=nil)
      if key.class == String && block_given?
        @j_del.java_method(:deleteValue, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(key,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling delete_value(key)"
    end
    # @param [String] keyPrefix 
    # @yield 
    # @return [self]
    def get_values(keyPrefix=nil)
      if keyPrefix.class == String && block_given?
        @j_del.java_method(:getValues, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(keyPrefix,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_values(keyPrefix)"
    end
    # @param [String] keyPrefix 
    # @yield 
    # @return [self]
    def delete_values(keyPrefix=nil)
      if keyPrefix.class == String && block_given?
        @j_del.java_method(:deleteValues, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(keyPrefix,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling delete_values(keyPrefix)"
    end
    # @param [String] key 
    # @param [String] value 
    # @yield 
    # @return [self]
    def put_value(key=nil,value=nil)
      if key.class == String && value.class == String && block_given?
        @j_del.java_method(:putValue, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(key,value,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling put_value(key,value)"
    end
    # @param [Hash] token 
    # @yield 
    # @return [self]
    def create_acl_token(token=nil)
      if token.class == Hash && block_given?
        @j_del.java_method(:createAclToken, [Java::IoVertxExtConsul::AclToken.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::AclToken.new(::Vertx::Util::Utils.to_json_object(token)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling create_acl_token(token)"
    end
    # @param [String] id 
    # @yield 
    # @return [self]
    def info_acl_token(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:infoAclToken, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling info_acl_token(id)"
    end
    # @param [String] id 
    # @yield 
    # @return [self]
    def destroy_acl_token(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:destroyAclToken, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling destroy_acl_token(id)"
    end
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
