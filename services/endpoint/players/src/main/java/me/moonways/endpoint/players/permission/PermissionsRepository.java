package me.moonways.endpoint.players.permission;

import net.conveno.jdbc.*;
import net.conveno.jdbc.response.ConvenoResponse;

import java.util.UUID;

@ConvenoRepository(jdbc = "jdbc:h2:mem:default",
        username = "${system.jdbc.username}",
        password = "${system.jdbc.password}")
@ConvenoTable(name = "permissions")
public interface PermissionsRepository {

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "create table of not exists ${table} (uuid varchar(36) not null primary key, permission text not null)")
    void executeTableValid();

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "insert into ${table} values (${uuid}, ${permission}) on duplicate key update permission = ${permission}")
    void addPermission(@ConvenoParam("uuid") UUID uuid,
                       @ConvenoParam("permission") String permission);

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "delete from ${table} where uuid = ${uuid} and permission = ${permission}")
    void removePermission(@ConvenoParam("uuid") UUID uuid,
                          @ConvenoParam("permission") String permission);

    @ConvenoNonResponse
    @ConvenoAsynchronous(onlySubmit = true)
    @ConvenoQuery(sql = "delete from ${table} where uuid = ${uuid}")
    void removeAllPermissions(@ConvenoParam("uuid") UUID uuid);

    @ConvenoNonResponse
    @ConvenoAsynchronous
    @ConvenoQuery(sql = "select permission from ${table} where uuid = ${uuid} and permission like ${permission}")
    ConvenoResponse findPermission(@ConvenoParam("uuid") UUID uuid,
                                   @ConvenoParam("permission") String permission);

    @ConvenoNonResponse
    @ConvenoAsynchronous
    @ConvenoQuery(sql = "select permission from ${table} where uuid = ${uuid}")
    ConvenoResponse getAllPermissions(@ConvenoParam("uuid") UUID uuid);
}
