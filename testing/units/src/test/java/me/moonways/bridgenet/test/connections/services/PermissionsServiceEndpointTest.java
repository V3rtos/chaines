package me.moonways.bridgenet.test.connections.services;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.permissions.PermissionsServiceModel;
import me.moonways.bridgenet.model.permissions.group.GroupTypes;
import me.moonways.bridgenet.model.permissions.group.GroupsManager;
import me.moonways.bridgenet.model.permissions.group.PermissionGroup;
import me.moonways.bridgenet.model.permissions.group.PlayerGroupUpdateEvent;
import me.moonways.bridgenet.model.permissions.permission.Permission;
import me.moonways.bridgenet.model.permissions.permission.PermissionsManager;
import me.moonways.bridgenet.model.permissions.permission.PlayerPermissionPutEvent;
import me.moonways.bridgenet.model.permissions.permission.PlayerPermissionRemoveEvent;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class PermissionsServiceEndpointTest {

    @Inject
    private PermissionsServiceModel serviceModel;

    @Test
    @TestOrdered(1)
    public void test_addPermission() throws RemoteException {
        Optional<PlayerPermissionPutEvent> eventOptional = processPermissionAdd(TestConst.Permissions.TEMP_PERMISSION);
        assertTrue(eventOptional.isPresent());
    }

    @Test
    @TestOrdered(2)
    public void test_addPermissionDuplicate() throws RemoteException {
        Optional<PlayerPermissionPutEvent> eventOptional = processPermissionAdd(TestConst.Permissions.PERMISSION);
        assertFalse(eventOptional.isPresent());
    }

    @Test
    @TestOrdered(3)
    public void test_getPermissions() throws RemoteException {
        PermissionsManager permissions = serviceModel.getPermissions();

        Set<Permission> activePermissions = permissions.getActivePermissions(TestConst.Player.ID);

        assertNotNull(activePermissions);
        assertFalse(activePermissions.isEmpty());
    }

    @Test
    @TestOrdered(3)
    public void test_checkPermissions() throws RemoteException {
        PermissionsManager permissions = serviceModel.getPermissions();

        assertTrue(permissions.hasPermission(TestConst.Player.ID, TestConst.Permissions.PERMISSION_NAME));
        assertTrue(permissions.hasPermission(TestConst.Player.ID, TestConst.Permissions.PERMISSION));
        assertTrue(permissions.hasPermission(TestConst.Player.ID, TestConst.Permissions.TEMP_PERMISSION));
    }

    @Test
    @TestOrdered(4)
    public void test_removePermission() throws RemoteException {
        Optional<PlayerPermissionRemoveEvent> eventOptional = processPermissionRemove(TestConst.Permissions.PERMISSION);
        assertTrue(eventOptional.isPresent());
    }

    @Test
    @TestOrdered(5)
    public void test_removePermissionDuplicate() throws RemoteException {
        Optional<PlayerPermissionRemoveEvent> eventOptional = processPermissionRemove(TestConst.Permissions.TEMP_PERMISSION);
        assertFalse(eventOptional.isPresent());
    }


    private Optional<PlayerPermissionPutEvent> processPermissionAdd(Permission permission) throws RemoteException {
        PermissionsManager permissions = serviceModel.getPermissions();

        Optional<PlayerPermissionPutEvent> eventOptional = permissions.addPermission(TestConst.Player.ID, permission);
        eventOptional.ifPresent(log::debug);

        return eventOptional;
    }

    private Optional<PlayerPermissionRemoveEvent> processPermissionRemove(Permission permission) throws RemoteException {
        PermissionsManager permissions = serviceModel.getPermissions();

        Optional<PlayerPermissionRemoveEvent> eventOptional = permissions.removePermission(TestConst.Player.ID, permission);
        eventOptional.ifPresent(log::debug);

        return eventOptional;
    }

    @Test
    @TestOrdered(6)
    public void test_getGroup() throws RemoteException {
        GroupsManager groups = serviceModel.getGroups();

        Optional<PermissionGroup> permissionGroupOptional = groups.fromPlayerId(TestConst.Player.ID);
        assertTrue(permissionGroupOptional.isPresent());

        PermissionGroup permissionGroup = permissionGroupOptional.get();

        assertTrue(permissionGroup.isDefault());
        assertTrue(groups.isDefault(TestConst.Player.ID));
    }

    @Test
    @TestOrdered(7)
    public void test_updateGroup() throws RemoteException {
        GroupsManager groups = serviceModel.getGroups();

        Optional<PlayerGroupUpdateEvent> eventOptional = groups.updatePlayer(TestConst.Player.ID, GroupTypes.DEVELOPER);

        assertTrue(groups.isTechPersonal(TestConst.Player.ID));
        assertTrue(groups.isPersonal(TestConst.Player.ID));
        assertFalse(groups.isDefault(TestConst.Player.ID));

        assertTrue(eventOptional.isPresent());
        assertTrue(eventOptional.get().getPreviousGroup().isDefault());
    }
}
