package me.moonways.bridgenet.rest4j.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rest.model.Content;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.model.ResponseCode;
import me.moonways.bridgenet.rest4j.Error;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ErrorData implements Error {

    private final ResponseCode code;
    private final String message;

    @Override
    public HttpResponse getAsResponse() {
        return HttpResponse.of(code, Content.fromEntity(this));
    }
}
