package me.moonways.bridgenet.test.frameworks.database.entity;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.entity.EntityID;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.test.data.EntityStatus;
import me.moonways.bridgenet.test.data.EntityUser;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class EntityRepositoryInsertEntityTest {

    public static final EntityStatus ENTITY_STATUS = EntityStatus.builder()
            .name(TestConst.Entity.STATUS_NAME)
            .build();

    public static final EntityUser ENTITY_USER = EntityUser.builder()
            .firstName(TestConst.Entity.FIRST_NAME)
            .lastName(TestConst.Entity.LAST_NAME)
            .age(TestConst.Entity.AGE)
            .status(ENTITY_STATUS)
            .build();

    @Getter
    private EntityRepository<EntityStatus> statusRepository;
    @Getter
    private EntityRepository<EntityUser> userRepository;

    @Inject
    private EntityRepositoryFactory entityRepositoryFactory;

    @Before
    public void setUp() {
        this.statusRepository = entityRepositoryFactory.fromEntityType(EntityStatus.class);
        this.userRepository = entityRepositoryFactory.fromEntityType(EntityUser.class);
    }

    @Test
    public void test_success() {
        EntityID entityID = userRepository.insert(ENTITY_USER);
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
