package me.moonways.bridgenet.test.jdbc.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class GetPlayerTest {

    private static final String PLAYERS_TABLE = "Players";
    private static final String PLAYERS_AGES_TABLE = "PlayersAges";

    private static final String PLAYER_ID_COLUMN = "ID";
    private static final String PLAYER_NAME_COLUMN = "NAME";
    private static final String PLAYER_AGE_COLUMN = "AGE";

    private static final String COMPLETED_QUERY_NATIVE = "SELECT AVG(AGE) AS AVG_AGES, MIN(ID), ID FROM Players  OUTER JOIN PlayersAges ON ID = PlayersAges.ID WHERE NAME LIKE 'moonways_user' OR AGE = 5 GROUP BY ID ORDER BY AGE DESC LIMIT 2";

    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useSearchPattern()
                .container(PLAYERS_TABLE)
                .sort(CombinedStructs.orderedLabel(OrderDirection.DESCENDING, PLAYER_AGE_COLUMN))
                .subjects(composer.subjects()
                        .select(CombinedStructs.label(PLAYER_ID_COLUMN))
                        .average(CombinedStructs.label(PLAYER_AGE_COLUMN, "AVG_AGES"))
                        .min(CombinedStructs.label(PLAYER_ID_COLUMN))
                        .combine())
                .predicates(composer.predicates()
                        .ifMatches(CombinedStructs.field(PLAYER_NAME_COLUMN, "moonways_user"))
                        .or()
                        .ifEqual(CombinedStructs.field(PLAYER_AGE_COLUMN, 5))
                        .bind()
                        .combine())
                .merges(composer.merges()
                        .unscoped(CombinedStructs.merged(PLAYERS_AGES_TABLE, PLAYER_ID_COLUMN))
                        .combine())
                .groups(composer.groups()
                        .with(CombinedStructs.fieldNullable(PLAYER_ID_COLUMN))
                        .combine())
                .limit(2)
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);

        assertEquals(COMPLETED_QUERY_NATIVE, composedQuery);
    }
}
