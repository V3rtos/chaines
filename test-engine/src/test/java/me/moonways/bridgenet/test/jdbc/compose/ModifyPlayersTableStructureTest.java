package me.moonways.bridgenet.test.jdbc.compose;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.compose.*;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class ModifyPlayersTableStructureTest {

    private static final String COMPLETED_QUERY_NATIVE = "";

    @Inject
    private DatabaseComposer composer;

    @Test
    public void test_composeQuery() {
        // TODO: doesn't work still.
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

        assertEquals(COMPLETED_QUERY_NATIVE, composedQuery);
    }
}
