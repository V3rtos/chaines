package me.moonways.bridgenet.rest4j.server.endpoint.v1;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.servers.EntityServer;
import me.moonways.bridgenet.model.service.servers.ServersServiceModel;
import me.moonways.bridgenet.rest.model.Attributes;
import me.moonways.bridgenet.rest.model.Content;
import me.moonways.bridgenet.rest.model.HttpRequest;
import me.moonways.bridgenet.rest.model.HttpResponse;
import me.moonways.bridgenet.rest.persistence.HttpAsync;
import me.moonways.bridgenet.rest.persistence.HttpGet;
import me.moonways.bridgenet.rest.persistence.HttpServerListener;
import me.moonways.bridgenet.rest4j.*;
import me.moonways.bridgenet.rest4j.data.ApiErrors;
import me.moonways.bridgenet.rest4j.data.OkServer;
import me.moonways.bridgenet.rest4j.data.OkServersList;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@HttpServerListener(prefix = (Api.VERSION + Api.SERVERS_SERVICE))
public class RestV1ServersEndpoint {

    @Inject
    private ServersServiceModel serversServiceModel;

    @HttpAsync
    @HttpGet("/list")
    public HttpResponse doGetList(HttpRequest httpRequest) throws RemoteException {
        return HttpResponse.ok(Content.fromEntity(
                OkServersList.builder().list(getActiveServerNames()).build()));
    }

    @HttpAsync
    @HttpGet("/server")
    public HttpResponse doGetServerInfo(HttpRequest httpRequest) throws RemoteException {
        Attributes attributes = httpRequest.getAttributes();

        Optional<String> nameOptional = attributes.getString("name");
        if (!nameOptional.isPresent()) {
            return ApiErrors.noAttribute("name")
                    .getAsResponse();
        }

        Optional<EntityServer> serverOptional = serversServiceModel.getServer(nameOptional.get());
        if (!serverOptional.isPresent()) {
            return ApiErrors.notFound("server", nameOptional.get())
                    .getAsResponse();
        }

        EntityServer entityServer = serverOptional.get();
        return HttpResponse.ok(
                Content.fromEntity(
                        OkServer.builder()
                                .name(entityServer.getName())
                                .id(entityServer.getUniqueId())
                                .online(entityServer.getTotalOnline())
                                .build())
        );
    }

    private List<String> getActiveServerNames() throws RemoteException {
        return serversServiceModel.getTotalServers()
                .stream().map(entityServer -> {
                    try {
                        return entityServer.getName();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}
