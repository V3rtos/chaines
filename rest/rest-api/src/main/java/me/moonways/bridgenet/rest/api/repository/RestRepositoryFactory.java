package me.moonways.bridgenet.rest.api.repository;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.api.http.client.HttpClient;
import me.moonways.bridgenet.rest.api.exception.RestRepositoryException;
import me.moonways.bridgenet.rest.api.repository.proxy.RestClientProxy;
import me.moonways.bridgenet.rest.api.repository.proxy.RestServerProxy;
import me.moonways.bridgenet.rest.api.http.server.HttpServer;
import org.jetbrains.annotations.NotNull;

@Component
public class RestRepositoryFactory {

    @Inject
    private Gson gson;

    @Inject
    private AnnotationInterceptor annotationInterceptor;

    private HttpHost lookupHost(@NotNull Class<?> repositoryClass) {
        boolean isUseSSL = repositoryClass.isAnnotationPresent(RestCertificatesSecurity.class);

        String hostname = null;
        int hostport = 0;

        if (isMarkedAsClient(repositoryClass)) {
            hostname = repositoryClass.getDeclaredAnnotation(RestClient.class).host();
        }
        if (isMarkedAsServer(repositoryClass)) {
            RestServer declaredAnnotation = repositoryClass.getDeclaredAnnotation(RestServer.class);

            hostname = declaredAnnotation.host();
            hostport = declaredAnnotation.port();
        }

        return isUseSSL ? HttpHost.createSSL(hostname) : HttpHost.create(hostname, hostport);
    }

    public HttpClient lookupClient(@NotNull Class<?> repositoryClass) {
        if (!matchesAsRepository(repositoryClass)) {
            throw new RestRepositoryException("Class " + repositoryClass.getName() + " is not valid as repository");
        }

        HttpHost httpHost = lookupHost(repositoryClass);
        return HttpClient.create(httpHost);
    }

    public HttpServer lookupServer(@NotNull Class<?> repositoryClass) {
        if (!matchesAsRepository(repositoryClass)) {
            throw new RestRepositoryException("Class " + repositoryClass.getName() + " is not valid as repository");
        }

        HttpHost httpHost = lookupHost(repositoryClass);
        return null;
    }

    private boolean matchesAsRepository(@NotNull Class<?> repositoryClass) {
        return repositoryClass.isAnnotationPresent(RestRepository.class) &&
                (isMarkedAsClient(repositoryClass) || isMarkedAsServer(repositoryClass));
    }

    private boolean isMarkedAsClient(@NotNull Class<?> repositoryClass) {
        return repositoryClass.isAnnotationPresent(RestClient.class);
    }

    private boolean isMarkedAsServer(@NotNull Class<?> repositoryClass) {
        return repositoryClass.isAnnotationPresent(RestServer.class);
    }

    public <I> I lookupClientRepository(Class<I> cls) {
        HttpClient httpClient = lookupClient(cls);
        RestClientProxy proxy = new RestClientProxy(gson, httpClient);

        return annotationInterceptor.createProxyChecked(cls, proxy);
    }

    public <I> I lookupServerRepository(Class<I> cls) {
        HttpServer httpServer = lookupServer(cls);
        RestServerProxy proxy = new RestServerProxy(gson, httpServer);

        return annotationInterceptor.createProxyChecked(cls, proxy);
    }

    public <I> I lookupServerRepository(I obj) {
        HttpServer httpServer = lookupServer(obj.getClass());
        RestServerProxy proxy = new RestServerProxy(gson, httpServer);

        return annotationInterceptor.createProxyChecked(obj, proxy);
    }
}
