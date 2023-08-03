package me.moonways.rest.server.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.rest.server.controller.HttpController;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

@Log4j2
public class GetUserInfo implements HttpController {

    @Override
    public void process(HttpRequest request) {
        log.info("process request");
    }

    @Override
    public void processCallback(HttpResponse response) {
        log.info("process callback");
    }
}
