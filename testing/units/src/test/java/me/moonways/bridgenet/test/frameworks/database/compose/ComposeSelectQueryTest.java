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
public class ComposeSelectQueryTest {

    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useSearchPattern()
                .container(TestConst.SqlQuery.PLAYERS_TABLE)
                .sort(CombinedStructs.orderedLabel(OrderDirection.DESCENDING, TestConst.SqlQuery.PLAYER_AGE_COLUMN))
                .subjects(composer.subjects()
                        .select(CombinedStructs.label(TestConst.SqlQuery.PLAYER_ID_COLUMN))
                        .average(CombinedStructs.label(TestConst.SqlQuery.PLAYER_AGE_COLUMN, "AVG_AGES"))
                        .min(CombinedStructs.label(TestConst.SqlQuery.PLAYER_ID_COLUMN))
                        .combine())
                .predicates(composer.predicates()
                        .ifMatches(CombinedStructs.field(TestConst.SqlQuery.PLAYER_NAME_COLUMN, "moonways_user"))
                        .or()
                        .ifEqual(CombinedStructs.field(TestConst.SqlQuery.PLAYER_AGE_COLUMN, 20))
                        .bind()
                        .combine())
                .merges(composer.merges()
                        .unscoped(CombinedStructs.merged(TestConst.SqlQuery.PLAYERS_AGES_TABLE, TestConst.SqlQuery.PLAYER_ID_COLUMN))
                        .combine())
                .groups(composer.groups()
                        .with(CombinedStructs.fieldNullable(TestConst.SqlQuery.PLAYER_ID_COLUMN))
                        .combine())
                .limit(2)
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);
        assertEquals(composedQuery, TestConst.SqlQuery.PLAYERS_SELECT_JOINED_ROW_NATIVE);
    }
}
