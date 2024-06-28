package me.moonways.bridgenet.test.database.query;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.ExternalAcceptationType;
import me.moonways.bridgenet.test.engine.persistance.TestExternal;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class NativeQueryCallTransactionalTest {

    @Inject
    private DatabaseConnection connection;

    @TestExternal(acceptType = ExternalAcceptationType.BEFORE_UNIT)
    private NativeQueryCallTest nativeQueryCallTest;

    @Test
    public void test_repeatableReadTransactionGet() {
        connection.ofTransactionalGet(TransactionIsolation.REPEATABLE_READ, nativeQueryCallTest::getPlayersResponse)
                .whenCompleted(System.out::println);
    }
}
