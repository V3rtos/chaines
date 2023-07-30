package me.moonways.endpoint.players.offline;

import java.util.UUID;

import me.moonways.model.players.offline.OfflinePlayerData;
import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "players")
public interface OfflinePlayerRepository {

    @ConvenoQuery(sql = "create table if not exists ${table} (" +
        "uuid varchar(36) not null, " +
        "name varchar(16) not null, " +
        "experience int not null default 0, " +
        "group int not null default 0," +
        "primary key (uuid, name))")
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void executeTableValid();

    @ConvenoQuery(sql = "select * from ${table} where id = ${uuid}")
    ConvenoResponse getByUUID(@ConvenoParam("uuid") UUID uuid);

    @ConvenoQuery(sql = "select * from ${table} where name = lower(${name})")
    ConvenoResponse getByName(@ConvenoParam("name") String name);

    @ConvenoQuery(sql = "select * from ${table} where name like lower(${name})")
    ConvenoResponse matchByName(@ConvenoParam("name") String name);

    @ConvenoQuery(sql = "select * from ${table} where group = ${group}")
    ConvenoResponse getByGroup(@ConvenoParam("group") int groupLevel);

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "insert into ${table} values (${data}.$uuid, ${data}.$name, ${data}.$experience, ${data}.$groupLevel) ON DUPLICATE KEY UPDATE "
        + "experience = ${data}.$experience, group = ${data}.$groupLevel")
    void validatePlayer(@ConvenoParam("data") OfflinePlayerData playerData);

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "update ${table} set group = ${group} where uuid = ${uuid}")
    void updateGroupByUUID(@ConvenoParam("uuid") UUID uuid, @ConvenoParam("group") int groupLevel);

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "update ${table} set group = ${group} where name = lower(${name})")
    void updateGroupByName(@ConvenoParam("name") String name, @ConvenoParam("group") int groupLevel);
}
