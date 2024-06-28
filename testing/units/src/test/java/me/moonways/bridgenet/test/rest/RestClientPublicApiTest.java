package me.moonways.bridgenet.test.rest;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import me.moonways.bridgenet.rest.client.RestClientProxy;
import me.moonways.bridgenet.rest.client.WrappedHttpClient;
import me.moonways.bridgenet.rest.client.repository.RestClientRepository;
import me.moonways.bridgenet.rest.client.repository.RestRepositoryHelper;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(ModernTestEngineRunner.class)
public class RestClientPublicApiTest {

    @Inject
    private AnnotationInterceptor annotationInterceptor;
    @Inject
    private Gson gson;

    private RestClientRepository subj;

    @BeforeAll
    public void setUp() {
        RestRepositoryHelper helper = new RestRepositoryHelper(gson);
        HttpHost httpHost = helper.lookupHost(RestClientRepository.class);

        subj = (RestClientRepository) annotationInterceptor.createProxy(RestClientRepository.class,
                new RestClientProxy(WrappedHttpClient.create(httpHost), helper));
    }

    @Test
    public void test_verifiedPublicGet() {
        RestResponse response = subj.tryVerifiedPublicGet();

        assertEquals(response.getMethod(), "GET");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void test_unverifiedPublicGet() {
        RestResponse response = subj.tryUnverifiedPublicGet();

        assertEquals(response.getMethod(), "GET");
        assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void test_unverifiedPublicPost() {
        RestResponse response = subj.tryUnverifiedPublicPost();

        assertEquals(response.getMethod(), "POST");
        assertEquals(response.getStatusCode(), 404);
    }

    @Test
    public void test_unverifiedPublicDelete() {
        RestResponse response = subj.tryUnverifiedPublicDelete();

        assertEquals(response.getMethod(), "DELETE");
        assertEquals(response.getStatusCode(), 404);
    }
}
