package me.moonways.bridgenet.services.connection.player;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "users")
public interface OfflinePlayerRepository {

    @ConvenoQuery(sql = "select * from ${table} where id = ${id}")
    ConvenoResponse getOfflinePlayerByID(@ConvenoParam("id") int id);

    @ConvenoQuery(sql = "select * from ${table} where name = ${name}")
    ConvenoResponse getOfflinePlayerByName(@ConvenoParam("name") String name);

    @ConvenoQuery(sql = "create table if not exists ${table} (" +
            "id int not null primary key auto_increment, " +
            "name varchar not null, " +
            "experience int not null, " +
            "group int not null)")
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void validateTableExists();
}
