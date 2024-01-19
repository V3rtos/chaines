package me.moonways.endpoint.players.permission;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.players.permission.PermissionGroup;
import me.moonways.bridgenet.model.players.permission.PlayerPermissions;
import net.conveno.jdbc.ConvenoRouter;

import java.rmi.RemoteException;
import java.util.UUID;

public class PlayerPermissionsStub extends AbstractEndpointDefinition implements PlayerPermissions {

    private GroupsRepository groupsRepository;
    private PermissionsRepository permissionsRepository;

    public PlayerPermissionsStub() throws RemoteException {
        super();
    }

    public void initRepositories(ConvenoRouter router) {
        groupsRepository = router.getRepository(GroupsRepository.class);
        permissionsRepository = router.getRepository(PermissionsRepository.class);

        groupsRepository.executeTableValid();
        permissionsRepository.executeTableValid();
    }

    @Override
    public PermissionGroup findGroupByID(int groupId) throws RemoteException {
        return null;
    }

    @Override
    public PermissionGroup findGroupByName(String groupName) throws RemoteException {
        return null;
    }

    @Override
    public PermissionGroup findGroupByPlayer(String playerName) throws RemoteException {
        return null;
    }

    @Override
    public PermissionGroup findGroupByPlayer(UUID playerUuid) throws RemoteException {
        return null;
    }

    @Override
    public String lookupChatPrefix(String playerName) throws RemoteException {
        return null;
    }

    @Override
    public String lookupChatPrefix(UUID playerUuid) throws RemoteException {
        return null;
    }

    @Override
    public String lookupTablistPrefix(String playerName) throws RemoteException {
        return null;
    }

    @Override
    public String lookupTablistPrefix(UUID playerUuid) throws RemoteException {
        return null;
    }

    @Override
    public boolean hasPermission(String playerName, String permission) throws RemoteException {
        return false;
    }

    @Override
    public boolean hasPermission(UUID playerUuid, String permission) throws RemoteException {
        return false;
    }

    @Override
    public boolean isDefaultPlayer(String playerName) throws RemoteException {
        return false;
    }

    @Override
    public boolean isDefaultPlayer(UUID playerUuid) throws RemoteException {
        return false;
    }

    @Override
    public boolean isStaffPlayer(String playerName) throws RemoteException {
        return false;
    }

    @Override
    public boolean isStaffPlayer(UUID playerUuid) throws RemoteException {
        return false;
    }

    @Override
    public boolean isGroupExpired(String playerName) throws RemoteException {
        return false;
    }

    @Override
    public boolean isGroupExpired(UUID playerUuid) throws RemoteException {
        return false;
    }
}
