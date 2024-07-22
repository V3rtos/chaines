package me.moonways.bridgenet.rest4j.server.endpoint.v1;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.players.OfflinePlayer;
import me.moonways.bridgenet.model.service.players.Player;
import me.moonways.bridgenet.model.service.players.PlayersServiceModel;
import me.moonways.bridgenet.model.service.servers.EntityServer;
import me.moonways.bridgenet.rest.model.Attributes;
import me.moonways.bridgenet.rest.model.Content;
import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.persistence.HttpAsync;
import me.moonways.bridgenet.rest.persistence.HttpGet;
import me.moonways.bridgenet.rest.persistence.HttpServerListener;
import me.moonways.bridgenet.rest4j.data.ApiErrors;
import me.moonways.bridgenet.rest4j.data.OkPlayerStatus;
import me.moonways.bridgenet.rest4j.data.OkServer;
import me.moonways.bridgenet.rest4j.data.OkTotalOnline;
import me.moonways.bridgenet.rest4j.Api;

import java.rmi.RemoteException;
import java.util.Optional;

@HttpServerListener(prefix = Api.VERSION + Api.PLAYERS_SERVICE)
public class RestV1PlayersEndpoint {

    @Inject
    private PlayersServiceModel playersServiceModel;

    @HttpAsync
    @HttpGet("/totalOnline")
    public HttpResponse doGetOnline(HttpRequest httpRequest) throws RemoteException {
        return HttpResponse.ok(
                Content.fromEntity(OkTotalOnline.builder()
                        .value(playersServiceModel.getTotalOnline())
                        .build())
        );
    }

    @HttpAsync
    @HttpGet("/status")
    public HttpResponse doGetStatus(HttpRequest httpRequest) throws RemoteException {
        Attributes attributes = httpRequest.getAttributes();

        Optional<String> nameOptional = attributes.getString("name");
        if (!nameOptional.isPresent()) {
            return ApiErrors.noAttribute("name")
                    .getAsResponse();
        }

        OfflinePlayer offlinePlayer = playersServiceModel.store().getOffline(nameOptional.get());
        if (offlinePlayer == null) {
            return ApiErrors.notFound("player", nameOptional.get())
                    .getAsResponse();
        }

        return HttpResponse.ok(
                Content.fromEntity(OkPlayerStatus.builder()
                        .name(offlinePlayer.getName())
                        .id(offlinePlayer.getId())
                        .isOnline(offlinePlayer.isOnline())
                        .server(getOfflinePlayerOkServer(offlinePlayer))
                        .build())
        );
    }

    private OkServer getOfflinePlayerOkServer(OfflinePlayer offlinePlayer) throws RemoteException {
        if (!offlinePlayer.isOnline()) {
            return null;
        }

        Player onlinePlayer = ((Player) offlinePlayer);
        Optional<EntityServer> server = onlinePlayer.getConnection().getServer();

        if (!server.isPresent()) {
            return null;
        }

        EntityServer entityServer = server.get();
        return OkServer.builder()
                .name(entityServer.getName())
                .id(entityServer.getUniqueId())
                .online(entityServer.getTotalOnline())
                .build();
    }
}
