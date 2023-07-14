package me.moonways.services.api.entities;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.services.api.entities.player.BridgenetPlayers;
import me.moonways.services.api.entities.server.BridgenetServers;

public interface BridgenetEntitiesService extends RemoteService {

    BridgenetPlayers getPlayers();

    BridgenetServers getServers();
}
