package me.moonways.bridgenet.rest.server.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rest.server.controller.HttpController;
import me.moonways.bridgenet.rest.server.controller.verify.VerificationConfig;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

@Log4j2
public class GetUserInfo implements HttpController {

    @Override
    public void process(HttpRequest request, VerificationConfig verificationConfig) {
        log.info("process request");
    }

    @Override
    public void processCallback(HttpResponse response, VerificationConfig verificationConfig) {
        log.info("process callback");
    }
}
