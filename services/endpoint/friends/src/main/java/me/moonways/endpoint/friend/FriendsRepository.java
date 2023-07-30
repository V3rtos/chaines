package me.moonways.endpoint.friend;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

import java.util.UUID;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "friends")
public interface FriendsRepository {

    @ConvenoQuery(sql = "create table if not exists ${table} (" +
            "uuid varchar(40) not null," +
            "friend varchar(40) not null)")
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void executeTableValid();

    @ConvenoQuery(sql = "select friend from ${table} where uuid = ${0}")
    ConvenoResponse findFriendsList(@ConvenoParam("0") UUID playerID);

    @ConvenoNonResponse
    @ConvenoTransaction({
            @ConvenoQuery(sql = "insert into ${table} values (${0}, ${1})"),
            @ConvenoQuery(sql = "insert into ${table} values (${1}, ${0})"),
    })
    void addFriend(@ConvenoParam("0") UUID playerID,
                   @ConvenoParam("1") UUID friendPlayerID);

    @ConvenoNonResponse
    @ConvenoTransaction({
            @ConvenoQuery(sql = "delete from ${table} where uuid = ${0} and friend = ${1}"),
            @ConvenoQuery(sql = "delete from ${table} where uuid = ${1} and friend = ${0}"),
    })
    void removeFriend(@ConvenoParam("0") UUID playerID,
                      @ConvenoParam("1") UUID friendPlayerID);
}
