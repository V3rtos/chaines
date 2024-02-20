package me.moonways.bridgenet.rest.server.controller;

import me.moonways.bridgenet.rest.server.controller.verify.VerificationConfig;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.io.IOException;

public interface HttpController {

    void process(HttpRequest request, VerificationConfig verificationConfig) throws HttpException, IOException;

    void processCallback(HttpResponse response, VerificationConfig verificationConfig) throws HttpException, IOException;
}
