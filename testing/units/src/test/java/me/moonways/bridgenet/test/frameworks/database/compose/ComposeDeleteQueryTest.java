package me.moonways.bridgenet.test.frameworks.database.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class ComposeDeleteQueryTest {

    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useDeletionPattern()
                .container("Players")
                .predicates(composer.predicates()
                        .ifEqual(CombinedStructs.field("NAME", "moonways_user")).bind()
                        .combine())
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);
        assertEquals(composedQuery, TestConst.SqlQuery.PLAYERS_DELETE_ROW_NATIVE);
    }
}
