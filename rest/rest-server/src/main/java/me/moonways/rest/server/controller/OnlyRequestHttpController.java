package me.moonways.rest.server.controller;

import me.moonways.rest.server.controller.verify.VerificationConfig;
import org.apache.http.HttpResponse;

public abstract class OnlyRequestHttpController implements HttpController {

    @Override
    public final void processCallback(HttpResponse response, VerificationConfig verificationConfig) {
        response.setStatusCode(200);
    }
}
