package me.moonways.bridgenet.test.database.query;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.ResponseStream;
import me.moonways.bridgenet.jdbc.core.util.result.Result;
import me.moonways.bridgenet.test.data.ExampleDatabaseObserver;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.DatabasesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(DatabasesModule.class)
public class NativeQueryCallTest {

    @Inject
    private DatabaseConnection connection;

    @Before
    public void setUp() {
        connection.addObserver(new ExampleDatabaseObserver());
    }

    public Result<ResponseStream> getPlayersResponse() {
        return connection.call(TestConst.SqlQuery.PLAYERS_SELECT_ROW_NATIVE);
    }

    @Test
    public void test_nativeManipulations() {
        connection.call(TestConst.SqlQuery.PLAYERS_CREATE_TABLE_NATIVE);
        connection.call(TestConst.SqlQuery.PLAYERS_INSERT_ROW_NATIVE)
                .map(response -> response.find(1))
                .whenCompleted(System.out::println);

        getPlayersResponse()
                .whenCompleted(response ->
                        response.forEach(System.out::println));
    }
}
