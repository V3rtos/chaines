package me.moonways.bridgenet.test.jdbc.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.objects.jdbc.StatusEntity;
import me.moonways.bridgenet.test.engine.objects.jdbc.UserEntity;
import me.moonways.bridgenet.test.engine.persistance.PutTestUnit;
import me.moonways.bridgenet.test.engine.persistance.SleepExecution;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.*;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class EntityRepositoryGetEntityTest {

    @PutTestUnit
    private EntityRepositoryInsertEntityTest entityRepositoryInsertEntityTest;

    @Test
    @SleepExecution(duration = 500)
    public void test_userGet() {
        EntityRepository<UserEntity> userRepository = entityRepositoryInsertEntityTest.getUserRepository();

        Optional<UserEntity> userOptional = userRepository.searchIf(
                userRepository.newSearchMarker()
                        .withGet(UserEntity::getId, 1)
                        .withGet(UserEntity::getFirstName, EntityRepositoryInsertEntityTest.TEST_USER.getFirstName()));

        assertTrue(userOptional.isPresent());
        log.debug("Founded user: {}", userOptional.orElse(null));
    }

    @Test
    @SleepExecution(duration = 500)
    public void test_statusGet() {
        EntityRepository<StatusEntity> statusRepository = entityRepositoryInsertEntityTest.getStatusRepository();

        Optional<StatusEntity> statusOptional = statusRepository.searchIf(
                statusRepository.newSearchMarker()
                        .withGet(StatusEntity::getId, 1));

        assertTrue(statusOptional.isPresent());
        log.debug("Founded status: {}", statusOptional.orElse(null));
    }
}
