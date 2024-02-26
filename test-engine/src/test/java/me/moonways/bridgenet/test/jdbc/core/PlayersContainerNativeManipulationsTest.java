package me.moonways.bridgenet.test.jdbc.core;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class PlayersContainerNativeManipulationsTest {

    @Inject
    private DatabaseConnection connection;

    @Test
    public void test_nativeManipulations() {
        connection.addObserver(new DatabaseConnectionEventTestObserver());

        connection.call("create table PLAYERS (id int not null unique auto_increment, name varchar(32) not null unique)");

        connection.call("insert into PLAYERS (NAME) values ('lyx')")
                .map(response -> response.find(1))
                .whenCompleted(log::debug);

        connection.call("select * from PLAYERS")
                .whenCompleted(response ->
                        response.forEach(log::debug));
    }
}
