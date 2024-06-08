package me.moonways.bridgenet.rest.server.controller;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rest.server.controller.verify.VerificationConfig;
import org.apache.http.HttpRequest;

@Log4j2
public abstract class OnlyResponseHttpController implements HttpController {

    protected boolean canLogging() {
        return true; // override me.
    }

    @Override
    public final void process(HttpRequest request, VerificationConfig verificationConfig) {
        if (canLogging()) {
            log.debug("");
        }
    }
}
