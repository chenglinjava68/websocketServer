package com.hoolai.websocket.echo.program;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class EchoAppConfig implements ServerApplicationConfig{

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		return scanned;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		Set<ServerEndpointConfig> configs = new HashSet<ServerEndpointConfig>();
		configs.add(ServerEndpointConfig.Builder.create(EchoServer.class, "/echoByCode").build());
		return configs;
	}

}
