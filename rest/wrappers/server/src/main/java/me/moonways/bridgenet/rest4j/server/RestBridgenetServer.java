package me.moonways.bridgenet.rest4j.server;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.model.authentication.Authentication;
import me.moonways.bridgenet.rest.model.authentication.defaults.HttpBearerAuthenticator;
import me.moonways.bridgenet.rest.server.HttpServer;
import me.moonways.bridgenet.rest4j.data.ApiErrors;
import me.moonways.bridgenet.rest4j.server.endpoint.RestPlayersEndpoint;
import me.moonways.bridgenet.rest4j.server.endpoint.RestServersEndpoint;

import java.net.InetSocketAddress;

/**
 * Класс для создания и управления HTTP-сервером для API Bridgenet4j.
 * <p>
 * Этот класс отвечает за настройку и запуск HTTP-сервера, регистрацию конечных
 * точек API и аутентификацию запросов.
 */
@Log4j2
@Autobind
public final class RestBridgenetServer {

    // todo: From config.
    private static final Object[] ENDPOINTS_ARR =
            {
                    new RestPlayersEndpoint(),
                    new RestServersEndpoint(),
                    // ...
            };

    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private BeansService beansService;

    /**
     * Запускает HTTP-сервер.
     * <p>
     * Настраивает сервер, регистрирует конечные точки и устанавливает
     * аутентификатор для Bearer токенов.
     * </p>
     */
    public void start() {
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 80); // todo: From config.
        HttpServer httpServer = HttpServer.builder()
                .executorService(Threads.newCachedThreadPool())
                .socketAddress(socketAddress)
                .notFoundListener(this::handleNotFoundRequest)
                .build();

        registerEndpoints(httpServer);
        httpServer.addAuthenticator(Authentication.BEARER,
                HttpBearerAuthenticator.single("440as08g13btj47yud6455sd6789fas"));

        httpServer.bind();
        log.info("HTTP-server has listening on §6{}", socketAddress);
    }

    /**
     * Обрабатывает запросы, для которых не найден соответствующий обработчик.
     *
     * @param httpRequest запрос, который не был обработан ни одним из зарегистрированных
     *                    конечных точек.
     * @return HTTP-ответ с сообщением об ошибке "Путь не найден".
     */
    private HttpResponse handleNotFoundRequest(HttpRequest httpRequest) {
        return ApiErrors.badRequestPath(httpRequest.getPath())
                .getAsResponse(); // todo: Message from config.
    }

    /**
     * Регистрирует конечные точки API на сервере.
     *
     * @param httpServer HTTP-сервер, на котором будут зарегистрированы конечные точки.
     */
    private void registerEndpoints(HttpServer httpServer) {
        for (Object endpointObj : ENDPOINTS_ARR) {
            beansService.inject(endpointObj);
            httpServer.registerRepository(endpointObj);
        }
    }
}
