package me.moonways.bridgenet.rest.wrapper.server;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.server.HttpServer;
import me.moonways.bridgenet.rest.wrapper.server.endpoint.RestPlayersEndpoint;

import java.net.InetSocketAddress;

@Autobind
public final class BridgenetRestServer {

    // todo: From config.
    private static final Object[] ENDPOINTS_ARR =
            {
                    new RestPlayersEndpoint(),
                    // ...
            };

    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private BeansService beansService;

    @PostConstruct
    private void doStart() {
        HttpServer httpServer = HttpServer.builder()
                .socketAddress(new InetSocketAddress(80)) // todo: From config.
                .notFoundListener(this::handleNotFoundRequest)
                .build();

        registerEndpoints(httpServer);
        httpServer.bind();
    }

    private HttpResponse handleNotFoundRequest(HttpRequest httpRequest) {
        return HttpResponse.notFound(); // todo: From config.
    }

    private void registerEndpoints(HttpServer httpServer) {
        for (Object endpointObj : ENDPOINTS_ARR) {

            beansService.inject(endpointObj);
            httpServer.registerRepository(endpointObj);
        }
    }
}
