package me.moonways.endpoint.language;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public final class PlayerLanguagesRepository {

    @Inject
    private EntityRepositoryFactory entityRepositoryFactory;

    private EntityRepository<EntityLanguage> languagesRepository;

    private void checkRepository() {
        if (languagesRepository == null) {
            languagesRepository = entityRepositoryFactory.fromEntityType(EntityLanguage.class);
        }
    }

    public void update(EntityLanguage entityLanguage) {
        checkRepository();
        languagesRepository.insert(entityLanguage);
    }

    public Optional<EntityLanguage> get(UUID playerId) {
        checkRepository();
        return languagesRepository.searchFirst(
                languagesRepository.beginCriteria()
                        .andEquals(EntityLanguage::getPlayerId, playerId))
                .blockOptional();
    }
}
