package me.moonways.bridgenet.test.jdbc.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.jdbc.entity.StatusEntity;
import me.moonways.bridgenet.test.engine.jdbc.entity.UserEntity;
import me.moonways.bridgenet.test.engine.persistance.PutTestUnit;
import me.moonways.bridgenet.test.engine.persistance.SleepExecution;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.*;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class EntityRepositoryDeleteEntityTest {

    @PutTestUnit
    private EntityRepositoryInsertEntityTest entityRepositoryInsertEntityTest;

    @Test
    @SleepExecution(duration = 750)
    public void test_userDelete() {
        EntityRepository<UserEntity> userRepository = entityRepositoryInsertEntityTest.getUserRepository();

        SearchMarker<UserEntity> searchMarker = userRepository.newSearchMarker()
                .withGet(UserEntity::getId, 1)
                .withGet(UserEntity::getFirstName, EntityRepositoryInsertEntityTest.TEST_USER.getFirstName());

        userRepository.deleteIf(searchMarker);

        Optional<UserEntity> userOptional = userRepository.searchIf(searchMarker);

        assertFalse(userOptional.isPresent());
        log.debug("User is deleted successful");
    }

    @Test
    @SleepExecution(duration = 750)
    public void test_statusDelete() {
        EntityRepository<StatusEntity> statusRepository = entityRepositoryInsertEntityTest.getStatusRepository();

        SearchMarker<StatusEntity> searchMarker = statusRepository.newSearchMarker()
                .withGet(StatusEntity::getId, 1);

        statusRepository.deleteIf(searchMarker);

        Optional<StatusEntity> statusOptional = statusRepository.searchIf(searchMarker);

        assertFalse(statusOptional.isPresent());
        log.debug("Status is deleted successful");
    }
}
