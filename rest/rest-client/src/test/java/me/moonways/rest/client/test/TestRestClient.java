package me.moonways.rest.client.test;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.rest.client.RestClientProxy;
import me.moonways.bridgenet.rest.client.WrappedHttpClient;
import me.moonways.bridgenet.rest.client.repository.RestClientRepository;
import me.moonways.bridgenet.rest.client.repository.RestRepositoryHelper;
import me.moonways.rest.api.HttpHost;

public class TestRestClient {

    public static void main(String[] args) {
        Gson gson = new Gson();
        DependencyInjection dependencyInjection = new DependencyInjection();
        AnnotationInterceptor annotationInterceptor = new AnnotationInterceptor();

        dependencyInjection.bind(annotationInterceptor);
        dependencyInjection.bind(gson);

        RestRepositoryHelper helper = new RestRepositoryHelper(gson);
        HttpHost httpHost = helper.lookupHost(RestClientRepository.class);

        RestClientRepository repository = (RestClientRepository) annotationInterceptor.createProxy(RestClientRepository.class,
                new RestClientProxy(WrappedHttpClient.create(httpHost), helper));

        // rest processes.
        System.out.println("tryVerifiedPrivateGet: " + repository.tryVerifiedPrivateGet());
        System.out.println("tryUnverifiedPrivateGet: " + repository.tryUnverifiedPrivateGet());

        System.out.println("tryVerifiedPublicGet: " + repository.tryVerifiedPublicGet());
        System.out.println("tryUnverifiedPublicGet: " + repository.tryUnverifiedPublicGet());
        System.out.println("tryUnverifiedPublicPost: " + repository.tryUnverifiedPublicPost());
        System.out.println("tryUnverifiedPublicDelete: " + repository.tryUnverifiedPublicDelete());
    }
}
