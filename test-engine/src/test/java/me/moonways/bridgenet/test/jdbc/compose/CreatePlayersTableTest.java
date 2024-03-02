package me.moonways.bridgenet.test.jdbc.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.element.Encoding;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class CreatePlayersTableTest {

    private static final String COMPLETED_QUERY_NATIVE = "CREATE TABLE IF NOT EXISTS Players ( ID BIGINT AUTO_INCREMENT NOT NULL UNIQUE, NAME VARCHAR(32) UNIQUE NOT NULL CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci, AGE INT NOT NULL DEFAULT 1 ) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

    @Inject
    private DatabaseProvider provider;
    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useCreationPattern()
                .encoding(Encoding.builder()
                        .characterStyle("utf8mb4")
                        .collate("utf8mb4_unicode_ci")
                        .build())
                .entity(StorageType.CONTAINER)
                .name("Players")
                .signature(composer.signature()
                        .with(CombinedStructs.styledParameter("ID",
                                ParameterStyle.builder()
                                        .type(ParameterType.BIGINT)
                                        .addons(Arrays.asList(
                                                ParameterAddon.INCREMENTING,
                                                ParameterAddon.NOTNULL,
                                                ParameterAddon.UNIQUE))
                                        .build()))
                        .with(CombinedStructs.styledParameter("NAME",
                                ParameterStyle.builder()
                                        .length(32)
                                        .type(ParameterType.STRING)
                                        .addons(Arrays.asList(
                                                ParameterAddon.UNIQUE,
                                                ParameterAddon.NOTNULL))
                                        .encoding(Encoding.builder()
                                                .characterStyle("utf8mb4")
                                                .collate("utf8mb4_unicode_ci")
                                                .build())
                                        .build()))
                        .with(CombinedStructs.styledParameter("AGE",
                                ParameterStyle.builder()
                                        .type(ParameterType.INT)
                                        .addons(Collections.singletonList(
                                                ParameterAddon.NOTNULL))
                                        .defaultValue(1)
                                        .build()))
                        .combine())
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);

        assertEquals(COMPLETED_QUERY_NATIVE, composedQuery);
    }
}
