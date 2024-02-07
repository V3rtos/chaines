package me.moonways.bridgenet.rest.client.repository;

import me.moonways.bridgenet.rest.client.repository.markers.mapping.DeleteMapping;
import me.moonways.bridgenet.rest.client.repository.markers.mapping.PostMapping;
import me.moonways.bridgenet.rest.api.StandardHeaders;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import me.moonways.bridgenet.rest.client.repository.markers.RestClient;
import me.moonways.bridgenet.rest.client.repository.markers.header.Header;
import me.moonways.bridgenet.rest.client.repository.markers.header.RestHeaders;
import me.moonways.bridgenet.rest.client.repository.markers.mapping.GetMapping;

@RestClient(host = "127.0.0.1:4590")
public interface RestClientRepository {

    @RestHeaders({
            @Header(key = StandardHeaders.Key.BRIDGENET_APIKEY, value = "mwr3stjutIh9B5tmEzh7BO0yalqDkbAClGbOQ2l8oRJjfybTm0mJ3v9v7jBiJcVWt9kT5jLqMBZwpyhxtBJd8AY3BIe5f84ir4BM8KMyKQ7G6JQRGFIrZO5qjYjEcwTh"),
    })
    @GetMapping("/users")
    RestResponse tryVerifiedPublicGet();

    @GetMapping("/users")
    RestResponse tryUnverifiedPublicGet();

    @RestHeaders({
            @Header(key = StandardHeaders.Key.BRIDGENET_USERNAME, value = "bridgenet_admin"),
            @Header(key = StandardHeaders.Key.BRIDGENET_PASSWORD, value = "$threadPool_10056"),
            @Header(key = StandardHeaders.Key.BRIDGENET_APIKEY, value = "mwr3stTsXHpTsti4QClKKnCgRH90wjauv4FbgWSOk9g1N4YVXVaII3U209jG6MtBhm7weoOgNszIOTT9SYapYg71bIS2fxlYRplAFoq6nMEVUuLtZLWvRPjyOHLlMZBe"),
    })
    @GetMapping("/users")
    RestResponse tryVerifiedPrivateGet();

    @RestHeaders({
            @Header(key = StandardHeaders.Key.BRIDGENET_USERNAME, value = "bridgenet_admin"),
            @Header(key = StandardHeaders.Key.BRIDGENET_APIKEY, value = "mwr3stTsXHpTsti4QClKKnCgRH90wjauv4FbgWSOk9g1N4YVXVaII3U209jG6MtBhm7weoOgNszIOTT9SYapYg71bIS2fxlYRplAFoq6nMEVUuLtZLWvRPjyOHLlMZBe"),
    })
    @GetMapping("/users")
    RestResponse tryUnverifiedPrivateGet();

    @PostMapping("/adduser")
    RestResponse tryUnverifiedPublicPost();

    @DeleteMapping("/deleteuser")
    RestResponse tryUnverifiedPublicDelete();
}
