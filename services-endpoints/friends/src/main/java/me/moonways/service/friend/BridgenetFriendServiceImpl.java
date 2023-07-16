package me.moonways.service.friend;

import me.moonways.service.api.friends.BridgenetFriendsService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BridgenetFriendServiceImpl extends UnicastRemoteObject implements BridgenetFriendsService {

    private static final long serialVersionUID = 2821269145226318681L;

    public BridgenetFriendServiceImpl() throws RemoteException {
        super();
    }
}
