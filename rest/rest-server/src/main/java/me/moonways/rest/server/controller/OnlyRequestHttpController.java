package me.moonways.rest.server.controller;

import org.apache.http.HttpResponse;

public abstract class OnlyRequestHttpController implements HttpController {

    @Override
    public final void processCallback(HttpResponse response) {
        response.setStatusCode(200);
    }
}
