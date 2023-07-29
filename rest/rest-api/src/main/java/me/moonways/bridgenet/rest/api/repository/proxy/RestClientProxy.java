package me.moonways.bridgenet.rest.api.repository.proxy;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.MethodPriority;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;
import me.moonways.bridgenet.rest.api.http.client.HttpClient;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageBuilder;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageType;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import me.moonways.bridgenet.rest.api.repository.*;

import java.lang.reflect.Parameter;

@MethodInterceptor
@RequiredArgsConstructor
public class RestClientProxy {

    private final Gson gson;
    private final HttpClient httpClient;

    @MethodPriority(2)
    @MethodHandler(target = RestJsonEntity.class)
    public Object handleJson(ProxiedMethod method, Object[] args) {
        Object lastCall = method.getLastCallReturnedValue();
        if (lastCall == null) {
            return null;
        }

        if (!lastCall.getClass().isAssignableFrom(RestResponse.class)) {
            return lastCall;
        }

        RestResponse lastResponse = (RestResponse) lastCall;
        String responseContent = lastResponse.getResponseContent();

        RestJsonEntity annotation = method.findAnnotation(RestJsonEntity.class);
        Class<?> parsingType = annotation.value();

        if (String.class.isAssignableFrom(parsingType)) {
            return responseContent;
        }

        return gson.fromJson(responseContent, parsingType);
    }

    private void addParametersToMessage(RestMessageBuilder builder, Parameter[] parameters, Object[] args) {
        int index = 0;
        for (Parameter parameter : parameters) {

            RestParameter param = parameter.getDeclaredAnnotation(RestParameter.class);
            if (param != null) {
                addParameter(builder, param, args[index]);
            }

            RestParameterBody body = parameter.getDeclaredAnnotation(RestParameterBody.class);
            if (body != null) {
                addBody(builder, body, args[index]);
            }

            index++;
        }
    }

    private void addParameter(RestMessageBuilder builder, RestParameter annotation, Object value) {
        String name = annotation.value();
        builder.addParameter(name, value.toString());
    }

    private void addBody(RestMessageBuilder builder, RestParameterBody annotation, Object value) {
        // todo
    }

    private void addHeadersToMessage(RestMessageBuilder builder, Header[] headers) {
        for (Header header : headers) {
            builder.addHeader(header.key(), header.value());
        }
    }

    private RestResponse executeAnnotatedMessage(String messageContextName,
                                                 RestMessageType type,
                                                 ProxiedMethod method, Object[] args) {

        RestHeaders headers = method.findAnnotation(RestHeaders.class);
        Parameter[] messageParams = method.getParametersByAnnotation(RestParameter.class);

        RestMessageBuilder builder = RestMessageBuilder.create()
                .setType(type)
                .setContext(messageContextName);

        addParametersToMessage(builder, messageParams, args);
        if (headers != null) {
            addHeadersToMessage(builder, headers.value());
        }

        RestResponse restResponse = httpClient.executeSync(builder.build());
        return !method.isVoid() ? restResponse : null;
    }

    @MethodPriority(1)
    @MethodHandler(target = GetMapping.class)
    public RestResponse handleGetMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessage(
                method.findAnnotation(GetMapping.class).value(),
                RestMessageType.GET, method, args
        );
    }

    @MethodPriority(1)
    @MethodHandler(target = PostMapping.class)
    public RestResponse handlePostMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessage(
                method.findAnnotation(PostMapping.class).value(),
                RestMessageType.POST, method, args
        );
    }

    @MethodPriority(1)
    @MethodHandler(target = DeleteMapping.class)
    public RestResponse handleDeleteMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessage(
                method.findAnnotation(DeleteMapping.class).value(),
                RestMessageType.DELETE, method, args
        );
    }

    @MethodPriority(1)
    @MethodHandler(target = PutMapping.class)
    public RestResponse handlePutMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessage(
                method.findAnnotation(PutMapping.class).value(),
                RestMessageType.PUT, method, args
        );
    }
}
