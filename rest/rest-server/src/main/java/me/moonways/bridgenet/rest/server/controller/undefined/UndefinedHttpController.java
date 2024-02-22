package me.moonways.bridgenet.rest.server.controller.undefined;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rest.server.controller.verify.VerificationConfig;
import me.moonways.bridgenet.rest.server.HttpServerConfig;
import me.moonways.bridgenet.rest.server.controller.HttpContextPattern;
import me.moonways.bridgenet.rest.server.controller.HttpController;
import org.apache.http.*;
import org.apache.http.client.entity.EntityBuilder;

import java.net.HttpURLConnection;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public final class UndefinedHttpController implements HttpController {

    private static final HttpEntity NOTHING_NOT_FOUND_ENTITY = EntityBuilder.create()
            .setText("Nothing not found")
            .build();

    private final HttpServerConfig config;
    private final boolean force;

    private boolean continuesResponseHandling;

    private boolean isActually(HttpRequest request) {
        final String uri = request.getRequestLine().getUri();
        final Map<String, HttpContextPattern> map = config.getControllerPatternsMap();

        return force || !map.containsKey(uri);
    }

    @Override
    public void process(HttpRequest request, VerificationConfig verificationConfig) {
        if (!isActually(request)) {
            return;
        }

        this.continuesResponseHandling = true;

        final RequestLine state = request.getRequestLine();
        final ProtocolVersion protocolVersion = state.getProtocolVersion();

        log.warn("§4HttpServer (v{}) has received UNDEFINED request: (method='{}', uri='{}')",
                String.format("%d.%d", protocolVersion.getMajor(), protocolVersion.getMinor()),
                state.getMethod(), state.getUri());
    }

    @Override
    public void processCallback(HttpResponse response, VerificationConfig verificationConfig) {
        if (continuesResponseHandling) {
            response.setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
            response.setEntity(NOTHING_NOT_FOUND_ENTITY);

            this.continuesResponseHandling = false;
        }
    }
}
