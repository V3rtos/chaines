package me.moonways.rest.server.controller;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.io.IOException;

public interface HttpController {

    void process(HttpRequest request) throws HttpException, IOException;

    void processCallback(HttpResponse response) throws HttpException, IOException;
}
