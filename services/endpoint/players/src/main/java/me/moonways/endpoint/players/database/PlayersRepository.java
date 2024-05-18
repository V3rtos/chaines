package me.moonways.endpoint.players.database;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;

import java.util.Optional;
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
        playersRepository.delete(player);
        playersRepository.insert(player);
    }

    public Optional<EntityPlayer> get(UUID id) {
        return namespacesRepository.searchIf(
                namespacesRepository.newSearchMarker()
                        .withGet(EntityNamespace::getUuid, id))
                .flatMap(entityNamespace -> playersRepository
                        .searchIf(playersRepository.newSearchMarker()
                                .withGet(EntityPlayer::getId, entityNamespace.getId())));
    }
}
