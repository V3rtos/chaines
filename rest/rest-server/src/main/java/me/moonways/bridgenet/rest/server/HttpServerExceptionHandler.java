package me.moonways.bridgenet.rest.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.ExceptionLogger;

import java.net.SocketException;

@Log4j2
@RequiredArgsConstructor
public class HttpServerExceptionHandler implements ExceptionLogger {

    private static final String INTERNAL_ERROR_MSG = "§4HttpServer throws internal error: §c{}";
    private final boolean printStackTrace;

    @Override
    public void log(Exception exception) {
        if (exception instanceof SocketException) {
            return;
        }

        log.error(INTERNAL_ERROR_MSG, exception.toString());

        if (printStackTrace) {
            exception.printStackTrace();
        }
    }
}
