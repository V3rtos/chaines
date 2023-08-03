package me.moonways.rest.server.controller.verify;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpExpectationVerifier;

@Log4j2
@RequiredArgsConstructor
public class HttpExpectationVerifyHandler implements HttpExpectationVerifier {

    private final VerifyHelper verifyHelper;

    @Override
    public void verify(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) {
        verifyHelper.process(httpRequest, httpResponse);
    }
}
