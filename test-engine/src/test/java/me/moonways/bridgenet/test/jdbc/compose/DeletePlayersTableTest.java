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
public class DeletePlayersTableTest {

    private static final String COMPLETED_QUERY_NATIVE = "DROP TABLE Players";

    @Inject
    private DatabaseProvider provider;
    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_composeQuery() {
        String composedQuery = composer.useEjectionPattern()
                .entity(StorageType.CONTAINER)
                .name("Players")
                .combine()
                .toNativeQuery()
                .orElse(null);

        log.debug(composedQuery);

        assertEquals(COMPLETED_QUERY_NATIVE, composedQuery);
    }
}
