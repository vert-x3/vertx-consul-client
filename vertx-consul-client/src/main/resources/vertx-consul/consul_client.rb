require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.consul.ConsulClient
module VertxConsul
  #  A Vert.x service used to interact with Consul.
  class ConsulClient
    # @private
    # @param j_del [::VertxConsul::ConsulClient] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxConsul::ConsulClient] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a Consul client.
    # @param [::Vertx::Vertx] vertx the Vert.x instance
    # @param [Hash{String => Object}] config the configuration
    # @return [::VertxConsul::ConsulClient] the client
    def self.create(vertx=nil,config=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && config == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtConsul::ConsulClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxConsul::ConsulClient)
      elsif vertx.class.method_defined?(:j_del) && config.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtConsul::ConsulClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(vertx.j_del,::Vertx::Util::Utils.to_json_object(config)),::VertxConsul::ConsulClient)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,config)"
    end
    #  Returns the configuration and member information of the local agent
    # @yield will be provided with the configuration and member information of the local agent
    # @return [self]
    def agent_info
      if block_given?
        @j_del.java_method(:agentInfo, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling agent_info()"
    end
    #  Returns the LAN network coordinates for all nodes in a given DC
    # @yield will be provided with network coordinates of nodes in datacenter
    # @return [self]
    def coordinate_nodes
      if block_given?
        @j_del.java_method(:coordinateNodes, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling coordinate_nodes()"
    end
    #  Returns the WAN network coordinates for all Consul servers, organized by DCs
    # @yield will be provided with network coordinates for all Consul servers
    # @return [self]
    def coordinate_datacenters
      if block_given?
        @j_del.java_method(:coordinateDatacenters, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling coordinate_datacenters()"
    end
    #  Returns key/value pair that corresponding to the specified key
    # @param [String] key the key
    # @yield will be provided with key/value pair
    # @return [self]
    def get_value(key=nil)
      if key.class == String && block_given?
        @j_del.java_method(:getValue, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(key,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_value(key)"
    end
    #  Returns key/value pair that corresponding to the specified key.
    #  This is blocking query unlike {::VertxConsul::ConsulClient#get_value}
    # @param [String] key the key
    # @param [Hash] options the blocking options
    # @yield will be provided with key/value pair
    # @return [self]
    def get_value_blocking(key=nil,options=nil)
      if key.class == String && options.class == Hash && block_given?
        @j_del.java_method(:getValueBlocking, [Java::java.lang.String.java_class,Java::IoVertxExtConsul::BlockingQueryOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(key,Java::IoVertxExtConsul::BlockingQueryOptions.new(::Vertx::Util::Utils.to_json_object(options)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_value_blocking(key,options)"
    end
    #  Remove the key/value pair that corresponding to the specified key
    # @param [String] key the key
    # @yield will be called on complete
    # @return [self]
    def delete_value(key=nil)
      if key.class == String && block_given?
        @j_del.java_method(:deleteValue, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(key,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling delete_value(key)"
    end
    #  Returns the list of key/value pairs that corresponding to the specified key prefix.
    # @param [String] keyPrefix the prefix
    # @yield will be provided with list of key/value pairs
    # @return [self]
    def get_values(keyPrefix=nil)
      if keyPrefix.class == String && block_given?
        @j_del.java_method(:getValues, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(keyPrefix,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_values(keyPrefix)"
    end
    #  Returns the list of key/value pairs that corresponding to the specified key prefix.
    #  This is blocking query unlike {::VertxConsul::ConsulClient#get_values}
    # @param [String] keyPrefix the prefix
    # @param [Hash] options the blocking options
    # @yield will be provided with list of key/value pairs
    # @return [self]
    def get_values_blocking(keyPrefix=nil,options=nil)
      if keyPrefix.class == String && options.class == Hash && block_given?
        @j_del.java_method(:getValuesBlocking, [Java::java.lang.String.java_class,Java::IoVertxExtConsul::BlockingQueryOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(keyPrefix,Java::IoVertxExtConsul::BlockingQueryOptions.new(::Vertx::Util::Utils.to_json_object(options)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_values_blocking(keyPrefix,options)"
    end
    #  Removes all the key/value pair that corresponding to the specified key prefix
    # @param [String] keyPrefix the prefix
    # @yield will be called on complete
    # @return [self]
    def delete_values(keyPrefix=nil)
      if keyPrefix.class == String && block_given?
        @j_del.java_method(:deleteValues, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(keyPrefix,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling delete_values(keyPrefix)"
    end
    #  Adds specified key/value pair
    # @param [String] key the key
    # @param [String] value the value
    # @yield will be provided with success of operation
    # @return [self]
    def put_value(key=nil,value=nil)
      if key.class == String && value.class == String && block_given?
        @j_del.java_method(:putValue, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(key,value,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling put_value(key,value)"
    end
    # @param [String] key the key
    # @param [String] value the value
    # @param [Hash] options options used to push pair
    # @yield will be provided with success of operation
    # @return [self]
    def put_value_with_options(key=nil,value=nil,options=nil)
      if key.class == String && value.class == String && options.class == Hash && block_given?
        @j_del.java_method(:putValueWithOptions, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxExtConsul::KeyValueOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(key,value,Java::IoVertxExtConsul::KeyValueOptions.new(::Vertx::Util::Utils.to_json_object(options)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling put_value_with_options(key,value,options)"
    end
    #  Create new Acl token
    # @param [Hash] token properties of the token
    # @yield will be provided with ID of created token
    # @return [self]
    def create_acl_token(token=nil)
      if token.class == Hash && block_given?
        @j_del.java_method(:createAclToken, [Java::IoVertxExtConsul::AclToken.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::AclToken.new(::Vertx::Util::Utils.to_json_object(token)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling create_acl_token(token)"
    end
    #  Update Acl token
    # @param [Hash] token properties of the token to be updated
    # @yield will be provided with ID of updated
    # @return [self]
    def update_acl_token(token=nil)
      if token.class == Hash && block_given?
        @j_del.java_method(:updateAclToken, [Java::IoVertxExtConsul::AclToken.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::AclToken.new(::Vertx::Util::Utils.to_json_object(token)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling update_acl_token(token)"
    end
    #  Clone Acl token
    # @param [String] id the ID of token to be cloned
    # @yield will be provided with ID of cloned token
    # @return [self]
    def clone_acl_token(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:cloneAclToken, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling clone_acl_token(id)"
    end
    #  Get list of Acl token
    # @yield will be provided with list of tokens
    # @return [self]
    def list_acl_tokens
      if block_given?
        @j_del.java_method(:listAclTokens, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling list_acl_tokens()"
    end
    #  Get info of Acl token
    # @param [String] id the ID of token
    # @yield will be provided with token
    # @return [self]
    def info_acl_token(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:infoAclToken, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling info_acl_token(id)"
    end
    #  Destroy Acl token
    # @param [String] id the ID of token
    # @yield will be called on complete
    # @return [self]
    def destroy_acl_token(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:destroyAclToken, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling destroy_acl_token(id)"
    end
    #  Fires a new user event
    # @param [String] name name of event
    # @yield will be provided with properties of event
    # @return [self]
    def fire_event(name=nil)
      if name.class == String && block_given?
        @j_del.java_method(:fireEvent, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(name,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling fire_event(name)"
    end
    #  Fires a new user event
    # @param [String] name name of event
    # @param [Hash] options options used to create event
    # @yield will be provided with properties of event
    # @return [self]
    def fire_event_with_options(name=nil,options=nil)
      if name.class == String && options.class == Hash && block_given?
        @j_del.java_method(:fireEventWithOptions, [Java::java.lang.String.java_class,Java::IoVertxExtConsul::EventOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(name,Java::IoVertxExtConsul::EventOptions.new(::Vertx::Util::Utils.to_json_object(options)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling fire_event_with_options(name,options)"
    end
    #  Returns the most recent events known by the agent
    # @yield will be provided with list of events
    # @return [self]
    def list_events
      if block_given?
        @j_del.java_method(:listEvents, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling list_events()"
    end
    #  Adds a new service, with an optional health check, to the local agent.
    # @param [Hash] serviceOptions the options of new service
    # @yield will be called when complete
    # @return [self]
    def register_service(serviceOptions=nil)
      if serviceOptions.class == Hash && block_given?
        @j_del.java_method(:registerService, [Java::IoVertxExtConsul::ServiceOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::ServiceOptions.new(::Vertx::Util::Utils.to_json_object(serviceOptions)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_service(serviceOptions)"
    end
    #  Places a given service into "maintenance mode"
    # @param [Hash] maintenanceOptions the maintenance options
    # @yield will be called when complete
    # @return [self]
    def maintenance_service(maintenanceOptions=nil)
      if maintenanceOptions.class == Hash && block_given?
        @j_del.java_method(:maintenanceService, [Java::IoVertxExtConsul::MaintenanceOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::MaintenanceOptions.new(::Vertx::Util::Utils.to_json_object(maintenanceOptions)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling maintenance_service(maintenanceOptions)"
    end
    #  Remove a service from the local agent. The agent will take care of deregistering the service with the Catalog.
    #  If there is an associated check, that is also deregistered.
    # @param [String] id the ID of service
    # @yield will be called when complete
    # @return [self]
    def deregister_service(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:deregisterService, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling deregister_service(id)"
    end
    #  Returns the nodes providing a service
    # @param [String] service name of service
    # @yield will be provided with list of nodes providing given service
    # @return [self]
    def catalog_service_nodes(service=nil)
      if service.class == String && block_given?
        @j_del.java_method(:catalogServiceNodes, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(service,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling catalog_service_nodes(service)"
    end
    #  Returns the nodes providing a service, filtered by tag
    # @param [String] service name of service
    # @param [String] tag service tag
    # @yield will be provided with list of nodes providing given service
    # @return [self]
    def catalog_service_nodes_with_tag(service=nil,tag=nil)
      if service.class == String && tag.class == String && block_given?
        @j_del.java_method(:catalogServiceNodesWithTag, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(service,tag,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling catalog_service_nodes_with_tag(service,tag)"
    end
    #  Return all the datacenters that are known by the Consul server
    # @yield will be provided with list of datacenters
    # @return [self]
    def catalog_datacenters
      if block_given?
        @j_del.java_method(:catalogDatacenters, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling catalog_datacenters()"
    end
    #  Returns the nodes registered in a datacenter
    # @yield will be provided with list of nodes
    # @return [self]
    def catalog_nodes
      if block_given?
        @j_del.java_method(:catalogNodes, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling catalog_nodes()"
    end
    #  Returns the services registered in a datacenter
    # @yield will be provided with list of services
    # @return [self]
    def catalog_services
      if block_given?
        @j_del.java_method(:catalogServices, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling catalog_services()"
    end
    #  Returns the node's registered services
    # @param [String] node node name
    # @yield will be provided with list of services
    # @return [self]
    def catalog_node_services(node=nil)
      if node.class == String && block_given?
        @j_del.java_method(:catalogNodeServices, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(node,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling catalog_node_services(node)"
    end
    #  Returns list of services registered with the local agent.
    # @yield will be provided with list of services
    # @return [self]
    def local_services
      if block_given?
        @j_del.java_method(:localServices, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling local_services()"
    end
    #  Return all the checks that are registered with the local agent.
    # @yield will be provided with list of checks
    # @return [self]
    def local_checks
      if block_given?
        @j_del.java_method(:localChecks, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling local_checks()"
    end
    #  Add a new check to the local agent. The agent is responsible for managing the status of the check
    #  and keeping the Catalog in sync.
    # @param [Hash] checkOptions options used to register new check
    # @yield will be called when complete
    # @return [self]
    def register_check(checkOptions=nil)
      if checkOptions.class == Hash && block_given?
        @j_del.java_method(:registerCheck, [Java::IoVertxExtConsul::CheckOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::CheckOptions.new(::Vertx::Util::Utils.to_json_object(checkOptions)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_check(checkOptions)"
    end
    #  Remove a check from the local agent. The agent will take care of deregistering the check from the Catalog.
    # @param [String] checkId the ID of check
    # @yield will be called when complete
    # @return [self]
    def deregister_check(checkId=nil)
      if checkId.class == String && block_given?
        @j_del.java_method(:deregisterCheck, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling deregister_check(checkId)"
    end
    #  Set status of the check to "passing". Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @yield will be called when complete
    # @return [self]
    def pass_check(checkId=nil)
      if checkId.class == String && block_given?
        @j_del.java_method(:passCheck, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling pass_check(checkId)"
    end
    #  Set status of the check to "passing". Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @param [String] note a human-readable message with the status of the check
    # @yield will be called when complete
    # @return [self]
    def pass_check_with_note(checkId=nil,note=nil)
      if checkId.class == String && note.class == String && block_given?
        @j_del.java_method(:passCheckWithNote, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,note,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling pass_check_with_note(checkId,note)"
    end
    #  Set status of the check to "warning". Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @yield will be called when complete
    # @return [self]
    def warn_check(checkId=nil)
      if checkId.class == String && block_given?
        @j_del.java_method(:warnCheck, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling warn_check(checkId)"
    end
    #  Set status of the check to "warning". Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @param [String] note a human-readable message with the status of the check
    # @yield will be called when complete
    # @return [self]
    def warn_check_with_note(checkId=nil,note=nil)
      if checkId.class == String && note.class == String && block_given?
        @j_del.java_method(:warnCheckWithNote, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,note,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling warn_check_with_note(checkId,note)"
    end
    #  Set status of the check to "critical". Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @yield will be called when complete
    # @return [self]
    def fail_check(checkId=nil)
      if checkId.class == String && block_given?
        @j_del.java_method(:failCheck, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling fail_check(checkId)"
    end
    #  Set status of the check to "critical". Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @param [String] note a human-readable message with the status of the check
    # @yield will be called when complete
    # @return [self]
    def fail_check_with_note(checkId=nil,note=nil)
      if checkId.class == String && note.class == String && block_given?
        @j_del.java_method(:failCheckWithNote, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,note,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling fail_check_with_note(checkId,note)"
    end
    #  Set status of the check to given status. Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @param [:PASSING,:WARNING,:CRITICAL] status new status of check
    # @yield will be called when complete
    # @return [self]
    def update_check(checkId=nil,status=nil)
      if checkId.class == String && status.class == Symbol && block_given?
        @j_del.java_method(:updateCheck, [Java::java.lang.String.java_class,Java::IoVertxExtConsul::CheckStatus.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,Java::IoVertxExtConsul::CheckStatus.valueOf(status),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling update_check(checkId,status)"
    end
    #  Set status of the check to given status. Used with a check that is of the TTL type. The TTL clock will be reset.
    # @param [String] checkId the ID of check
    # @param [:PASSING,:WARNING,:CRITICAL] status new status of check
    # @param [String] note a human-readable message with the status of the check
    # @yield will be called when complete
    # @return [self]
    def update_check_with_note(checkId=nil,status=nil,note=nil)
      if checkId.class == String && status.class == Symbol && note.class == String && block_given?
        @j_del.java_method(:updateCheckWithNote, [Java::java.lang.String.java_class,Java::IoVertxExtConsul::CheckStatus.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(checkId,Java::IoVertxExtConsul::CheckStatus.valueOf(status),note,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling update_check_with_note(checkId,status,note)"
    end
    #  Get the Raft leader for the datacenter in which the agent is running.
    #  It returns an address in format "<code>10.1.10.12:8300</code>"
    # @yield will be provided with address of cluster leader
    # @return [self]
    def leader_status
      if block_given?
        @j_del.java_method(:leaderStatus, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling leader_status()"
    end
    #  Retrieves the Raft peers for the datacenter in which the the agent is running.
    #  It returns a list of addresses "<code>10.1.10.12:8300</code>", "<code>10.1.10.13:8300</code>"
    # @yield will be provided with list of peers
    # @return [self]
    def peers_status
      if block_given?
        @j_del.java_method(:peersStatus, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling peers_status()"
    end
    #  Initialize a new session
    # @yield will be provided with ID of new session
    # @return [self]
    def create_session
      if block_given?
        @j_del.java_method(:createSession, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling create_session()"
    end
    #  Initialize a new session
    # @param [Hash] options options used to create session
    # @yield will be provided with ID of new session
    # @return [self]
    def create_session_with_options(options=nil)
      if options.class == Hash && block_given?
        @j_del.java_method(:createSessionWithOptions, [Java::IoVertxExtConsul::SessionOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtConsul::SessionOptions.new(::Vertx::Util::Utils.to_json_object(options)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling create_session_with_options(options)"
    end
    #  Returns the requested session information
    # @param [String] id the ID of requested session
    # @yield will be provided with info of requested session
    # @return [self]
    def info_session(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:infoSession, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling info_session(id)"
    end
    #  Renews the given session. This is used with sessions that have a TTL, and it extends the expiration by the TTL
    # @param [String] id the ID of session that should be renewed
    # @yield will be provided with info of renewed session
    # @return [self]
    def renew_session(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:renewSession, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling renew_session(id)"
    end
    #  Returns the active sessions
    # @yield will be provided with list of sessions
    # @return [self]
    def list_sessions
      if block_given?
        @j_del.java_method(:listSessions, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling list_sessions()"
    end
    #  Returns the active sessions for a given node
    # @param [String] nodeId the ID of node
    # @yield will be provided with list of sessions
    # @return [self]
    def list_node_sessions(nodeId=nil)
      if nodeId.class == String && block_given?
        @j_del.java_method(:listNodeSessions, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(nodeId,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling list_node_sessions(nodeId)"
    end
    #  Destroys the given session
    # @param [String] id the ID of session
    # @yield will be called when complete
    # @return [self]
    def destroy_session(id=nil)
      if id.class == String && block_given?
        @j_del.java_method(:destroySession, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling destroy_session(id)"
    end
    #  Close the client and release its resources
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
