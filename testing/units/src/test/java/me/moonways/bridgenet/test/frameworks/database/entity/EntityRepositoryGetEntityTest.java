package me.moonways.bridgenet.test.frameworks.database.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.test.data.EntityStatus;
import me.moonways.bridgenet.test.data.EntityUser;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.TestExternal;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestSleeping;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class EntityRepositoryGetEntityTest {

    @TestExternal
    private EntityRepositoryInsertEntityTest entityRepositoryInsertEntityTest;

    @Test
    @TestSleeping(500)
    public void test_userGet() {
        EntityRepository<EntityUser> userRepository = entityRepositoryInsertEntityTest.getUserRepository();

        Optional<EntityUser> userOptional = userRepository.searchIf(
                userRepository.newSearchMarker()
                        .withGet(EntityUser::getId, 1)
                        .withGet(EntityUser::getFirstName, EntityRepositoryInsertEntityTest.ENTITY_USER.getFirstName()));

        assertTrue(userOptional.isPresent());
        log.debug("Founded user: {}", userOptional.orElse(null));
    }

    @Test
    @TestSleeping(500)
    public void test_statusGet() {
        EntityRepository<EntityStatus> statusRepository = entityRepositoryInsertEntityTest.getStatusRepository();

        Optional<EntityStatus> statusOptional = statusRepository.searchIf(
                statusRepository.newSearchMarker()
                        .withGet(EntityStatus::getId, 1));

        assertTrue(statusOptional.isPresent());
        log.debug("Founded status: {}", statusOptional.orElse(null));
    }
}
