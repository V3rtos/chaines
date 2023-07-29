package me.moonways.bridgenet.rest.api.test.repository;

import me.moonways.bridgenet.rest.api.repository.*;

@RestRepository
@RestCertificatesSecurity
@RestClient(host = "catfact.ninja")
public interface TestRestClientRepository {

    @RestJsonEntity(RandomCatFact.class)
    @RestHeaders({
            @Header(key = "Content-Type", value = "application/json"),
            @Header(key = "Content-Length", value = "0"),
    })
    @GetMapping("/fact")
    RandomCatFact getRandomCatFact();
}
