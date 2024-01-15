package me.moonways.endpoint.players.offline;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

import java.util.UUID;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "players")
public interface OfflineMessageRepository {

    @ConvenoQuery(sql = "create table if not exists ${table} (" +
        "uuid varchar(36) not null primary key, " +
        "message longtext not null)")
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void executeTableValid();

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "insert into ${table} values (${uuid}, ${message})")
    void offer(@ConvenoParam("uuid") UUID uuid,
               @ConvenoParam("message") String message);

    @ConvenoAsynchronous
    @ConvenoQuery(sql = "select message from ${table} where uuid = ${uuid}")
    ConvenoResponse findMessages(@ConvenoParam("uuid") UUID uuid);
}
