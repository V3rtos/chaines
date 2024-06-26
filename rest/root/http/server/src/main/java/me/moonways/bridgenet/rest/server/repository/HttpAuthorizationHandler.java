package me.moonways.bridgenet.rest.server.repository;

import me.moonways.bridgenet.rest.model.authentication.HttpAuthenticator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Класс для представления обработчика авторизации HTTP-запросов.
 * <p>
 *     Содержит аутентификатор и флаг, указывающий, является ли обработчик асинхронным.
 * </p>
 */
@Getter
@Builder
@ToString
public class HttpAuthorizationHandler {

    private final HttpAuthenticator authenticator;
    private final boolean isAsynchronous;
}
