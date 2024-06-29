package me.moonways.bridgenet.test.database.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.test.data.EntityStatus;
import me.moonways.bridgenet.test.data.EntityUser;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.DatabasesModule;
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

        Optional<EntityUser> userOptional = userRepository.searchFirst(
                userRepository.beginCriteria()
                        .andEquals(EntityUser::getId, 1)
                        .andEquals(EntityUser::getFirstName, EntityRepositoryInsertEntityTest.ENTITY_USER.getFirstName()))
                .blockOptional();

        assertTrue(userOptional.isPresent());
        log.debug("Founded user: {}", userOptional.orElse(null));
    }

    @Test
    @TestSleeping(500)
    public void test_statusGet() {
        EntityRepository<EntityStatus> statusRepository = entityRepositoryInsertEntityTest.getStatusRepository();

        Optional<EntityStatus> statusOptional = statusRepository.searchFirst(
                statusRepository.beginCriteria()
                        .andEquals(EntityStatus::getId, 1))
                .blockOptional();

        assertTrue(statusOptional.isPresent());
        log.debug("Founded status: {}", statusOptional.orElse(null));
    }
}
