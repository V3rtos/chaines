package me.moonways.bridgenet.test.jdbc.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.jdbc.core.compose.CombinedStructs;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class AddPlayerTest {

    private static final String COMPLETED_QUERY_NATIVE = "INSERT INTO Players ( NAME, AGE ) VALUES ( 'moonways_user', 1 ) ON DUPLICATE KEY UPDATE NAME = 'moonways_user'";

    @Inject
    private DatabaseProvider provider;
    @Inject
    private DatabaseComposer composer;
    
    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useInsertionPattern()
                .container("Players")
                .withValue(CombinedStructs.field("NAME", "moonways_user"))
                .withValue(CombinedStructs.field("AGE", 1))
                .useDuplicationReduce()
                .updateOnConflict(CombinedStructs.field("NAME", "moonways_user"))
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);

        assertEquals(COMPLETED_QUERY_NATIVE, composedQuery);
    }
}
