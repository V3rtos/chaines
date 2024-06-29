package me.moonways.bridgenet.rest4j;

import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.model.ResponseCode;

public interface Error {

    ResponseCode getCode();

    String getMessage();

    HttpResponse getAsResponse();
}
