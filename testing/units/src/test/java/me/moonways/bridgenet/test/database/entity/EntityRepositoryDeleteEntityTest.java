package me.moonways.bridgenet.test.database.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.criteria.SearchCriteria;
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

import static org.junit.Assert.assertFalse;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class EntityRepositoryDeleteEntityTest {

    @TestExternal
    private EntityRepositoryInsertEntityTest entityRepositoryInsertEntityTest;

    @Test
    @TestSleeping(750)
    public void test_userDelete() {
        EntityRepository<EntityUser> userRepository = entityRepositoryInsertEntityTest.getUserRepository();

        SearchCriteria<EntityUser> searchCriteria = userRepository.beginCriteria()
                .andEquals(EntityUser::getId, 1)
                .andEquals(EntityUser::getFirstName, EntityRepositoryInsertEntityTest.ENTITY_USER.getFirstName());

        userRepository.delete(searchCriteria);

        Optional<EntityUser> userOptional = userRepository.searchFirst(searchCriteria)
                .blockOptional();

        assertFalse(userOptional.isPresent());
        log.debug("User is deleted successful");
    }

    @Test
    @TestSleeping(750)
    public void test_statusDelete() {
        EntityRepository<EntityStatus> statusRepository = entityRepositoryInsertEntityTest.getStatusRepository();

        SearchCriteria<EntityStatus> searchCriteria = statusRepository.beginCriteria()
                .andEquals(EntityStatus::getId, 1);

        statusRepository.delete(searchCriteria);

        Optional<EntityStatus> statusOptional = statusRepository.searchFirst(searchCriteria)
                .blockOptional();

        assertFalse(statusOptional.isPresent());
        log.debug("Status is deleted successful");
    }
}
