package me.moonways.bridgenet.test.api.inject;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Property;
import me.moonways.bridgenet.api.inject.WrappedProperty;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
public class WrappedPropertyTest {

    @Property("system.version")
    private WrappedProperty systemVersionProperty;

    @Test
    public void test_notNull() {
        log.debug(systemVersionProperty);
        log.debug(systemVersionProperty.getAsString().orElse(""));

        assertNotNull(systemVersionProperty.getProperty());
        assertTrue(systemVersionProperty.getAsString().isPresent());
    }
}
