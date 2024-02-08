package me.moonways.bridgenet.rest.client;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.MethodPriority;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;
import me.moonways.bridgenet.rest.client.repository.RestRepositoryHelper;
import me.moonways.bridgenet.rest.client.repository.persistence.mapping.*;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageBuilder;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageType;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import me.moonways.bridgenet.rest.client.repository.persistence.header.RestHeaders;
import me.moonways.bridgenet.rest.client.repository.persistence.param.RestAttribute;

import java.lang.reflect.Parameter;

@MethodInterceptor
@RequiredArgsConstructor
public class RestClientProxy {

    private final WrappedHttpClient httpClient;
    private final RestRepositoryHelper helper;

    @MethodPriority(1)
    @MethodHandler(target = GetMapping.class)
    public RestResponse handleGetMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessageClient(
                method.findAnnotation(GetMapping.class).value(),
                RestMessageType.GET, method, args
        );
    }

    @MethodPriority(1)
    @MethodHandler(target = PostMapping.class)
    public RestResponse handlePostMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessageClient(
                method.findAnnotation(PostMapping.class).value(),
                RestMessageType.POST, method, args
        );
    }

    @MethodPriority(1)
    @MethodHandler(target = DeleteMapping.class)
    public RestResponse handleDeleteMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessageClient(
                method.findAnnotation(DeleteMapping.class).value(),
                RestMessageType.DELETE, method, args
        );
    }

    @MethodPriority(1)
    @MethodHandler(target = PutMapping.class)
    public RestResponse handlePutMessage(ProxiedMethod method, Object[] args) {
        return this.executeAnnotatedMessageClient(
                method.findAnnotation(PutMapping.class).value(),
                RestMessageType.PUT, method, args
        );
    }

    @MethodPriority(2)
    @MethodHandler(target = RestJsonEntity.class)
    public Object handleJson(ProxiedMethod method, Object[] args) {
        if (method.isVoid())
            return method.getLastCallReturnedValue();

        return helper.handleJsonEntityAnnotation(method);
    }

    private RestResponse executeAnnotatedMessageClient(String messageContextName, RestMessageType type,
                                                       ProxiedMethod method, Object[] args) {
        RestHeaders headers = method.findAnnotation(RestHeaders.class);
        Parameter[] messageParams = method.getParametersByAnnotation(RestAttribute.class);

        RestMessageBuilder builder = RestMessageBuilder.create()
                .setType(type)
                .setContext(messageContextName);

        helper.addParametersToMessage(builder, messageParams, args);
        if (headers != null) {
            helper.addHeadersToMessage(builder, headers.value());
        }

        RestResponse restResponse = httpClient.executeSync(builder.build());
        return !method.isVoid() ? restResponse : null;
    }
}
