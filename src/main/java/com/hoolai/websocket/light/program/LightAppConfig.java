package com.hoolai.websocket.light.program;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class LightAppConfig implements ServerApplicationConfig{

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		return scanned;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		Set<ServerEndpointConfig> configs = new HashSet<ServerEndpointConfig>();
		configs.add(ServerEndpointConfig.Builder.create(LightLifeCycle.class, "/lightByCode").build());
		return configs;
	}

}
