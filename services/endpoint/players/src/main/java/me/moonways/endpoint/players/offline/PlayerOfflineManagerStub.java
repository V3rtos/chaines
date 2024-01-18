package me.moonways.endpoint.players.offline;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.players.offline.OfflineDao;
import me.moonways.bridgenet.model.players.offline.OfflineEntityPlayer;
import me.moonways.bridgenet.model.players.offline.PlayerOfflineManager;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerOfflineManagerStub extends AbstractEndpointDefinition implements PlayerOfflineManager {

    private static final int UUID_RESPONSE_INDEX = 1;
    private static final int NAME_RESPONSE_INDEX = 1;
    private static final int EXPERIENCE_RESPONSE_INDEX = 1;
    private static final int GROUP_RESPONSE_INDEX = 1;

    private OfflinePlayerRepository offlinePlayerRepository;
    private OfflineMessageRepository offlineMessageRepository;

    public PlayerOfflineManagerStub() throws RemoteException {
        super();
    }

    @PostConstruct
    public void injectRepositories() {
        DependencyInjection injector = getInjector();

        ConvenoRouter convenoRouter = (ConvenoRouter) injector.getContainer().findInstance(ConvenoRouter.class);

        offlinePlayerRepository = convenoRouter.getRepository(OfflinePlayerRepository.class);
        offlinePlayerRepository.executeTableValid();

        offlineMessageRepository = convenoRouter.getRepository(OfflineMessageRepository.class);
        offlineMessageRepository.executeTableValid();
    }

    private OfflineDao lookupDaoByUUID(UUID playerUUID) {
        ConvenoResponse offlinePlayerResponse = offlinePlayerRepository.getByUUID(playerUUID);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            throw new NullPointerException("player response by id");
        }

        return read(first);
    }

    private OfflineDao lookupDaoByName(String playerName) {
        ConvenoResponse offlinePlayerResponse = offlinePlayerRepository.getByName(playerName);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            return null;
        }

        return read(first);
    }

    private OfflineDao read(ConvenoResponseLine response) {
        if (response == null || response.isEmpty()) {
            throw new NullPointerException("response");
        }

        UUID uuid = UUID.fromString(response.getNullableString(UUID_RESPONSE_INDEX));

        String name = response.getNullableString(NAME_RESPONSE_INDEX);

        int experience = response.getNullableInt(EXPERIENCE_RESPONSE_INDEX);
        int groupId = response.getNullableInt(GROUP_RESPONSE_INDEX);

        return OfflineDao.newBuilder()
                .setUuid(uuid)
                .setName(name)
                .setExperience(experience)
                .setGroupId(groupId)
                .build();
    }

    @Override
    public OfflineEntityPlayer lookup(@NotNull UUID playerUUID) {
        OfflineDao offlinePlayerData = lookupDaoByUUID(playerUUID);
        return new OfflineEntityPlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    @Override
    public OfflineEntityPlayer lookup(@NotNull String playerName) {
        OfflineDao offlinePlayerData = lookupDaoByName(playerName);
        return new OfflineEntityPlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    @Override
    public OfflineDao readData(String playerName) throws RemoteException {
        return lookupDaoByName(playerName);
    }

    @Override
    public OfflineDao readData(UUID playerUuid) throws RemoteException {
        return lookupDaoByUUID(playerUuid);
    }

    @Override
    public CompletableFuture<Boolean> pushOfflineMessage(String playerName, String message) throws RemoteException {
        OfflineDao offlineDao = readData(playerName);
        return pushOfflineMessage(offlineDao.getUuid(), message);
    }

    @Override
    public CompletableFuture<Boolean> pushOfflineMessage(UUID playerUuid, String message) throws RemoteException {
        offlineMessageRepository.offer(playerUuid, message);
        return CompletableFuture.completedFuture(true);
    }
}
