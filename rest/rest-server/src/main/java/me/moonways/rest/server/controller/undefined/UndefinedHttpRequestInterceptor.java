package me.moonways.rest.server.controller.undefined;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

@Log4j2
@RequiredArgsConstructor
public class UndefinedHttpRequestInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

    private final UndefinedHttpController undefinedHttpController;

    @Override
    public void process(HttpRequest request, HttpContext context) {
        undefinedHttpController.process(request);
    }

    @Override
    public void process(HttpResponse response, HttpContext context) {
        undefinedHttpController.processCallback(response);
    }
}
