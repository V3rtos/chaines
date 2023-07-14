package me.moonways.service.entity;

import lombok.Getter;
import me.moonways.service.entity.player.PlayerManager;
import me.moonways.service.entity.server.ServerManager;
import me.moonways.services.api.entities.BridgenetEntitiesService;
import me.moonways.services.api.entities.player.BridgenetPlayers;
import me.moonways.services.api.entities.server.BridgenetServers;

@Getter
public class EntitiesService implements BridgenetEntitiesService {

    private final BridgenetPlayers players = new PlayerManager();
    private final BridgenetServers servers = new ServerManager();
}
