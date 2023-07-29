package me.moonways.bridgenet.rest.api.test.client;

import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.api.http.client.HttpClient;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageBuilder;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageType;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;

public class TestRestClient {

    private static final String HOSTNAME = "catfact.ninja";
    private static final String CONTEXT = "/fact";

    public static void main(String[] args) {
        firstSolutionTest();
        secondSolutionTest();
    }

    public static void firstSolutionTest() {
        try (HttpClient client = HttpClient.create(HttpHost.create(HOSTNAME))) {

            try (HttpClient factClient = client.openContext(CONTEXT)) {
                RestResponse response = factClient.executeSync(RestMessageType.GET);

                System.out.println(response.getResponseContent());
            }
        }
    }

    public static void secondSolutionTest() {
        try (HttpClient client = HttpClient.create(HttpHost.createSSL(HOSTNAME))) {

            RestResponse response = client.executeSync(
                    RestMessageBuilder.create()
                            .setContext(CONTEXT)
                            .build());

            System.out.println(response.getResponseContent());
        }
    }
}
