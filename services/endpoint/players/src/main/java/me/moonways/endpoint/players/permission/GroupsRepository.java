package me.moonways.endpoint.players.permission;

import net.conveno.jdbc.*;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "groups")
public interface GroupsRepository {

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "create table if not exists ${table} (uuid varchar(36) not null primary key, group int not null default 0)")
    void executeTableValid();


}
