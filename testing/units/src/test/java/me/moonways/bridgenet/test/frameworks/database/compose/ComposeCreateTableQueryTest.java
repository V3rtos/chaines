package me.moonways.bridgenet.test.frameworks.database.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.element.Encoding;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class ComposeCreateTableQueryTest {

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
                                                ParameterSignature.AUTO_GENERATED,
                                                ParameterSignature.NOTNULL,
                                                ParameterSignature.UNIQUE,
                                                ParameterSignature.PRIMARY))
                                        .build()))
                        .with(CombinedStructs.styledParameter("NAME",
                                ParameterStyle.builder()
                                        .length(32)
                                        .type(ParameterType.STRING)
                                        .addons(Arrays.asList(
                                                ParameterSignature.UNIQUE,
                                                ParameterSignature.NOTNULL,
                                                ParameterSignature.PRIMARY))
                                        .encoding(Encoding.builder()
                                                .characterStyle("utf8mb4")
                                                .collate("utf8mb4_unicode_ci")
                                                .build())
                                        .build()))
                        .with(CombinedStructs.styledParameter("AGE",
                                ParameterStyle.builder()
                                        .type(ParameterType.INT)
                                        .addons(Collections.singletonList(
                                                ParameterSignature.NOTNULL))
                                        .defaultValue(1)
                                        .build()))
                        .combine())
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);
        assertEquals(composedQuery, TestConst.SqlQuery.PLAYERS_CREATE_ENCODED_TABLE_NATIVE);
    }
}
