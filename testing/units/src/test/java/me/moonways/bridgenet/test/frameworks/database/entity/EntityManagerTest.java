package me.moonways.bridgenet.test.frameworks.database.entity;

import me.moonways.bridgenet.jdbc.entity.modern.DatabaseEntityManager;
import me.moonways.bridgenet.jdbc.entity.modern.Descriptor;
import me.moonways.bridgenet.jdbc.entity.modern.Element;
import me.moonways.bridgenet.test.data.EmployeeEntity;
import org.junit.Test;

public class EntityManagerTest {

    private final DatabaseEntityManager entityManager = new DatabaseEntityManager();

    @Test
    public void test() {
        Descriptor<EmployeeEntity> descriptor = entityManager.descriptor(EmployeeEntity.class);

        Element idElement = descriptor.get("id");
    }
}
