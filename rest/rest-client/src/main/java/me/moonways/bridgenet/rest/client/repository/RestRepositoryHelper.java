package me.moonways.bridgenet.rest.client.repository;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.UnsafeFactory;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;
import me.moonways.bridgenet.rest.client.repository.persistence.RestCertificatesSecurity;
import me.moonways.bridgenet.rest.client.repository.persistence.RestClient;
import me.moonways.bridgenet.rest.client.repository.persistence.header.Header;
import me.moonways.bridgenet.rest.client.repository.persistence.mapping.RestJsonEntity;
import me.moonways.bridgenet.rest.client.repository.persistence.param.RestAttribute;
import me.moonways.bridgenet.rest.client.repository.persistence.param.RestEntity;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.bridgenet.rest.api.exchange.entity.type.MultipartEntity;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageBuilder;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RestRepositoryHelper {

    private final Gson gson;
    private final BeanFactory beanFactory = new UnsafeFactory();

    public void addParametersToMessage(RestMessageBuilder builder, Parameter[] parameters, Object[] args) {
        int index = 0;
        List<ExchangeableEntity> entityBodiesList = new ArrayList<>();

        for (Parameter parameter : parameters) {

            RestAttribute param = parameter.getDeclaredAnnotation(RestAttribute.class);
            if (param != null) {
                addParameter(builder, param, args[index]);
            }

            RestEntity body = parameter.getDeclaredAnnotation(RestEntity.class);
            if (body != null) {
                ExchangeableEntity entityBody = createBody(body, args[index]);
                entityBodiesList.add(entityBody);
            }

            index++;
        }

        if (entityBodiesList.isEmpty()) {
            return;
        }

        if (entityBodiesList.size() == 1) {
            builder.setEntity(entityBodiesList.get(0));
            return;
        }

        final MultipartEntity.Builder multipartBuilder = MultipartEntity
                .newMultipartBuilder();

        for (ExchangeableEntity entityBody : entityBodiesList) {
            multipartBuilder.addPart(entityBody);
        }

        builder.setEntity(multipartBuilder.build());
    }

    public void addParameter(RestMessageBuilder builder, RestAttribute annotation, Object value) {
        String name = annotation.value();
        builder.addParameter(name, value.toString());
    }

    public ExchangeableEntity createBody(RestEntity annotation, Object value) {
        Class<? extends ExchangeableEntity> entityType = annotation.value();

        ExchangeableEntity entity = beanFactory.create(entityType);
        entity.setContent(value);

        return entity;
    }

    public void addHeadersToMessage(RestMessageBuilder builder, Header[] headers) {
        for (Header header : headers) {
            builder.addHeader(header.key(), header.value());
        }
    }

    public Object handleJsonEntityAnnotation(ProxiedMethod method) {
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

    public HttpHost lookupHost(@NotNull Class<?> repositoryClass) {
        boolean isUseSSL = repositoryClass.isAnnotationPresent(RestCertificatesSecurity.class);

        String hostname = null;
        int hostport = 0;

        if (isMarkedAsClient(repositoryClass)) {
            RestClient declaredAnnotation = repositoryClass.getDeclaredAnnotation(RestClient.class);

            hostname = declaredAnnotation.host();
            hostport = declaredAnnotation.port();
        }

        return isUseSSL ? HttpHost.createSSL(hostname) : HttpHost.create(hostname, hostport);
    }

    private boolean isMarkedAsClient(@NotNull Class<?> repositoryClass) {
        return repositoryClass.isAnnotationPresent(RestClient.class);
    }
}
