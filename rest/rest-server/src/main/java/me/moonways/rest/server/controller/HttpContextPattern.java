package me.moonways.rest.server.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class HttpContextPattern {

    private final String name;
    private final String method;

    private final HttpController controller;
}
