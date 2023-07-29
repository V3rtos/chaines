package me.moonways.bridgenet.rest.api.test.repository;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.rest.api.repository.RestRepositoryFactory;

public class RandomCatFactStarter {

    public static void main(String[] args) {
        RestRepositoryFactory repositoryFactory = new RestRepositoryFactory();
        DependencyInjection dependencyInjection = new DependencyInjection();

        dependencyInjection.bind(new AnnotationInterceptor());
        dependencyInjection.bind(repositoryFactory);
        dependencyInjection.bind(new Gson());

        TestRestClientRepository repository = repositoryFactory.lookupClientRepository(TestRestClientRepository.class);

        for (int i = 0; i < 5; i++) {
            System.out.println(repository.getRandomCatFact());
        }
    }
}
