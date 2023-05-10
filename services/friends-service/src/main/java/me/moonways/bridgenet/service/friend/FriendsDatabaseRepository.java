package me.moonways.bridgenet.service.friend;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

@ConvenoRepository(jdbc = "",
        username = "root",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "friends")
public interface FriendsDatabaseRepository {

    @ConvenoQuery(sql = "create table if not exists ${table} (" +
            "id int not null primary key," +
            "friend int not null)")
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void validateTableExists();

    @ConvenoQuery(sql = "select friend from ${table} where id = ${i0}")
    @ConvenoAsynchronous(onlySubmit = true)
    ConvenoResponse getFriendsList(@ConvenoParam("id") int playerID);

    @ConvenoTransaction({
            @ConvenoQuery(sql = "insert into ${table} values (${id}, ${friend})"),
            @ConvenoQuery(sql = "insert into ${table} values (${friend}, ${id})"),
    })
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void addFriend(@ConvenoParam("id") int playerID,
                   @ConvenoParam("friend") int friendPlayerID);

    @ConvenoTransaction({
            @ConvenoQuery(sql = "delete from ${table} where id = ${id} and friend = ${friend})"),
            @ConvenoQuery(sql = "delete from ${table} where id = ${friend} and friend = ${id})"),
    })
    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    void removeFriend(@ConvenoParam("id") int playerID,
                      @ConvenoParam("friend") int friendPlayerID);
}
