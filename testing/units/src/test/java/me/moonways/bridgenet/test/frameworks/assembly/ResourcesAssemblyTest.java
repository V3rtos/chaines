package me.moonways.bridgenet.test.frameworks.assembly;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesFileSystem;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.jdbc.provider.BridgenetJdbcProvider;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
public class ResourcesAssemblyTest {

    @Inject
    private ResourcesAssembly assembly;

    @Test
    public void test_readAnyGlobalConfig() {
        String jdbcSettingsJsonContent = assembly.readResourceFullContent(ResourcesTypes.JDBC_JSON);

        log.debug(jdbcSettingsJsonContent);

        assertFalse(jdbcSettingsJsonContent.isEmpty());
    }

    @Test
    public void test_readAnyGlobalConfigAsJson() {
        BridgenetJdbcProvider.JdbcSettingsConfig jdbcSettingsConfig = assembly.readJsonAtEntity(ResourcesTypes.JDBC_JSON,
                BridgenetJdbcProvider.JdbcSettingsConfig.class);

        log.debug(jdbcSettingsConfig);

        assertNotNull(jdbcSettingsConfig);
        assertNotNull(jdbcSettingsConfig.getUsername());
    }

    @Test
    public void test_findAnyConfigFile() {
        ResourcesFileSystem fileSystem = assembly.getFileSystem();

        Path absolutePath = fileSystem.findAsFile(ResourcesTypes.JDBC_JSON)
                .toPath();

        log.debug(absolutePath);

        assertNotNull(absolutePath);
        assertTrue(Files.exists(absolutePath));
    }

    @Test
    public void test_findAnyGlobalFile() {
        ResourcesFileSystem fileSystem = assembly.getFileSystem();

        Path absolutePath = fileSystem.findPathAtProject("assembly");

        log.debug(absolutePath);

        assertNotNull(absolutePath);
        assertTrue(Files.exists(absolutePath));
    }
}
