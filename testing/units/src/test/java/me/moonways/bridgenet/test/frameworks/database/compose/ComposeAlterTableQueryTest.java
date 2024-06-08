package me.moonways.bridgenet.test.frameworks.database.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class ComposeAlterTableQueryTest {

    @Inject
    private DatabaseComposer composer;

    @Ignore
    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useRestorationPattern()
                .container("Players")
                .add(CombinedStructs.styledParameter("SIZE",
                        ParameterStyle.builder()
                                .defaultValue(165)
                                .type(ParameterType.INT)
                                .addons(Collections.singletonList(ParameterAddon.NOTNULL))
                                .build()))
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);
        assertEquals(composedQuery, TestConst.SqlQuery.PLAYERS_ALTER_COLUMN_NATIVE);
    }
}
