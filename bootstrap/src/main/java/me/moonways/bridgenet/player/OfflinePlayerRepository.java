package me.moonways.bridgenet.player;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

@ConvenoTable(name = "users")
@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "root",
        password = "${system.jdbc.h2.password}")
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
