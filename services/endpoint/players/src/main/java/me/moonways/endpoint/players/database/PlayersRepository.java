package me.moonways.endpoint.players.database;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.jdbc.entity.SingleFuture;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;

import java.util.UUID;

@Autobind
public final class PlayersRepository {

    private EntityRepository<EntityPlayer> playersRepository;
    private EntityRepository<EntityNamespace> namespacesRepository;

    @Inject
    private EntityRepositoryFactory entityRepositoryFactory;

    @PostConstruct
    private void init() {
        playersRepository = entityRepositoryFactory.fromEntityType(EntityPlayer.class);
        namespacesRepository = entityRepositoryFactory.fromEntityType(EntityNamespace.class);
    }

    public void insert(EntityPlayer player) {
        playersRepository.insert(player);
    }

    public SingleFuture<EntityPlayer> get(UUID id) {
        return namespacesRepository.searchFirst(
                        namespacesRepository.beginCriteria()
                                .andEquals(EntityNamespace::getUuid, id))
                .map(this::toEntityPlayer);
    }

    public SingleFuture<EntityPlayer> get(String name) {
        return namespacesRepository.searchFirst(
                        namespacesRepository.beginCriteria()
                                .andEquals(EntityNamespace::getName, name.toLowerCase()))
                .map(this::toEntityPlayer);
    }

    private EntityPlayer toEntityPlayer(EntityNamespace entityNamespace) {
        return playersRepository.searchFirst(
                playersRepository.beginCriteria()
                        .andEquals(EntityPlayer::getNamespace, entityNamespace.getId()))
                .block();
    }
}
