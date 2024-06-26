package me.moonways.bridgenet.rest.wrapper.server.endpoint;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.players.PlayersServiceModel;
import me.moonways.bridgenet.rest.model.Content;
import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.persistence.HttpGet;
import me.moonways.bridgenet.rest.persistence.HttpServer;
import me.moonways.bridgenet.rest.wrapper.TotalOnline;

import java.rmi.RemoteException;

@HttpServer
public class RestPlayersEndpoint {

    @Inject
    private PlayersServiceModel playersServiceModel;

    @HttpGet("/v1/online")
    public HttpResponse doGetPlayer(HttpRequest httpRequest) throws RemoteException {
        return HttpResponse.ok(Content.fromEntity(
                TotalOnline.builder()
                        .value(playersServiceModel.getTotalOnline())
                        .build()));
    }
}
