package me.moonways.bridgenet.test.jdbc.entity;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.entity.EntityID;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.jdbc.entity.StatusEntity;
import me.moonways.bridgenet.test.engine.jdbc.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class EntityRepositoryInsertEntityTest {

    public static final StatusEntity TEST_STATUS = StatusEntity.builder()
            .name("StandUp")
            .build();

    public static final UserEntity TEST_USER = UserEntity.builder()
            .firstName("Oleg")
            .lastName("Saburov")
            .age(36)
            .statusEntity(TEST_STATUS)
            .build();

    @Getter
    private EntityRepository<StatusEntity> statusRepository;
    @Getter
    private EntityRepository<UserEntity> userRepository;

    @Inject
    private EntityRepositoryFactory entityRepositoryFactory;

    @Before
    public void setUp() {
        this.statusRepository = entityRepositoryFactory.fromEntityType(StatusEntity.class);
        this.userRepository = entityRepositoryFactory.fromEntityType(UserEntity.class);
    }

    @Test
    public void test_success() {
        EntityID entityID = userRepository.insert(TEST_USER);
        assertEquals(1, entityID.getId());

        log.debug("Inserted user identify: {}", entityID);

        if (!entityID.isValid()) {

            if (entityID.isNotFound()) {
                log.debug("entity id is not found");
            }

            if (entityID.isNotGenerated()) {
                log.debug("entity id is not auto-generated");
            }
        }
    }
}
