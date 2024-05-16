package me.moonways.endpoint.permissions;

import me.moonways.bridgenet.model.permissions.PermissionsServiceModel;
import me.moonways.bridgenet.model.permissions.group.GroupsManager;
import me.moonways.bridgenet.model.permissions.permission.PermissionsManager;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.endpoint.permissions.manager.GroupsManagerStub;
import me.moonways.endpoint.permissions.manager.PermissionsManagerStub;

import java.rmi.RemoteException;

public final class PermissionsServiceEndpoint extends EndpointRemoteObject implements PermissionsServiceModel {
    private static final long serialVersionUID = -5975978081916259048L;

    private final GroupsManager groupsManager = new GroupsManagerStub();
    private final PermissionsManager permissionsManager = new PermissionsManagerStub();

    public PermissionsServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        context.inject(groupsManager);
        context.inject(permissionsManager);
    }

    @Override
    public GroupsManager getGroups() throws RemoteException {
        return groupsManager;
    }

    @Override
    public PermissionsManager getPermissions() throws RemoteException {
        return permissionsManager;
    }
}
