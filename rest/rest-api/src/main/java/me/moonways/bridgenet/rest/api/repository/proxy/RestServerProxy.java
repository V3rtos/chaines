package me.moonways.bridgenet.rest.api.repository.proxy;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.rest.api.http.server.HttpServer;

@MethodInterceptor
@RequiredArgsConstructor
public class RestServerProxy {

    private final Gson gson;
    private final HttpServer httpServer;
}
