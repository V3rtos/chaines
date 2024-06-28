package me.moonways.bridgenet.test.database.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;
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

        SearchMarker<EntityUser> searchMarker = userRepository.newSearchMarker()
                .and(EntityUser::getId, 1)
                .and(EntityUser::getFirstName, EntityRepositoryInsertEntityTest.ENTITY_USER.getFirstName());

        userRepository.deleteIf(searchMarker);

        Optional<EntityUser> userOptional = userRepository.searchIf(searchMarker)
                .blockOptional();

        assertFalse(userOptional.isPresent());
        log.debug("User is deleted successful");
    }

    @Test
    @TestSleeping(750)
    public void test_statusDelete() {
        EntityRepository<EntityStatus> statusRepository = entityRepositoryInsertEntityTest.getStatusRepository();

        SearchMarker<EntityStatus> searchMarker = statusRepository.newSearchMarker()
                .and(EntityStatus::getId, 1);

        statusRepository.deleteIf(searchMarker);

        Optional<EntityStatus> statusOptional = statusRepository.searchIf(searchMarker)
                .blockOptional();

        assertFalse(statusOptional.isPresent());
        log.debug("Status is deleted successful");
    }
}
