package me.moonways.service.entities;

import lombok.Getter;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.service.api.bus.BridgenetBusService;
import me.moonways.service.api.entities.BridgenetEntitiesService;
import me.moonways.service.entities.player.PlayerManager;
import me.moonways.service.entities.server.ServerManager;
import net.conveno.jdbc.ConvenoRouter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Getter
public class EntitiesService extends UnicastRemoteObject implements BridgenetEntitiesService {

    private static final long serialVersionUID = 7424683335588215086L;

    private final PlayerManager players = new PlayerManager();
    private final ServerManager servers = new ServerManager();

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private ConvenoRouter convenoRouter;

    @Inject
    private BridgenetBusService busService;

    public EntitiesService() throws RemoteException {
        super();
    }

    @PostFactoryMethod
    public void init() {
        dependencyInjection.injectFields(players);
        dependencyInjection.injectFields(servers);

        players.injectRepository(convenoRouter);

        System.out.println(busService);
    }
}
