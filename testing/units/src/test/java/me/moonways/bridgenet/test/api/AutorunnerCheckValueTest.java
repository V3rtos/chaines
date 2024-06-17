package me.moonways.bridgenet.test.api;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.data.management.ExampleSetPropertyRunner;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.AutorunnersModule;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestSleeping;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;

import static org.junit.Assert.assertNotEquals;

@RunWith(ModernTestEngineRunner.class)
@TestModules(AutorunnersModule.class)
public class AutorunnerCheckValueTest {

    private String property;

    @Inject
    private Properties properties;
    @Inject
    private ExampleSetPropertyRunner runner; // <--- Autorunner for that test-class

    @BeforeAll
    public void setUp() {
        property = properties.getProperty(ExampleSetPropertyRunner.KEY);
    }

    @Test
    @TestSleeping(2000)
    public void test_successAssert() {
        assertNotEquals(property, properties.getProperty(ExampleSetPropertyRunner.KEY));
    }
}
