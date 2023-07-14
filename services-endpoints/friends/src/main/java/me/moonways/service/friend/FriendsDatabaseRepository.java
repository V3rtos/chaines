package me.moonways.service.friend;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "friends")
public interface FriendsDatabaseRepository {

    @ConvenoQuery(sql = "create table if not exists ${table} (" +
            "id int not null," +
            "friend int not null" +
            ")")
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void validateTableExists();

    @ConvenoQuery(sql = "select friend from ${table} where id = ${0}")
    ConvenoResponse getFriendsList(@ConvenoParam("0") int playerID);

    @ConvenoNonResponse
    @ConvenoTransaction({
            @ConvenoQuery(sql = "insert into ${table} values (${0}, ${1})"),
            @ConvenoQuery(sql = "insert into ${table} values (${1}, ${0})"),
    })
    void addFriend(@ConvenoParam("0") int playerID,
                   @ConvenoParam("1") int friendPlayerID);

    @ConvenoNonResponse
    @ConvenoTransaction({
            @ConvenoQuery(sql = "delete from ${table} where id = ${0} and friend = ${1}"),
            @ConvenoQuery(sql = "delete from ${table} where id = ${1} and friend = ${0}"),
    })
    void removeFriend(@ConvenoParam("0") int playerID,
                      @ConvenoParam("1") int friendPlayerID);
}
