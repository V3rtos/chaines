package me.moonways.bridgenet.rest4j.data;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.rest.model.ResponseCode;
import me.moonways.bridgenet.rest4j.Api;
import me.moonways.bridgenet.rest4j.Error;

@UtilityClass
public class ApiErrors {

    public Error badRequestPath(String path) {
        return new ErrorData(ResponseCode.BAD_REQUEST, String.format("api %s is not handling path: %s", Api.VERSION, path));
    }

    public Error noAttribute(String attr) {
        return new ErrorData(ResponseCode.BAD_REQUEST, String.format("attribute `?%s` is not found", attr));
    }

    public Error notFound(String type, Object id) {
        return new ErrorData(ResponseCode.NOT_FOUND, String.format("%s \"%s\" is not found", type, id));
    }

    public Error forbidden() {
        return new ErrorData(ResponseCode.FORBIDDEN, "auth credentials is incorrectly: `api-key`");
    }
}
