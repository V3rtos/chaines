package me.moonways.bridgenet.model.service.mojang;

import me.moonways.bridgenet.rmi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.Optional;

public interface MojangServiceModel extends RemoteService {

    boolean isPirateNick(String nickname) throws RemoteException;

    boolean isPirateId(String id) throws RemoteException;

    Optional<String> getNameWithOriginCase(String nickname) throws RemoteException;

    Optional<String> getMinecraftId(String nickname) throws RemoteException;

    Optional<String> getMinecraftNick(String id) throws RemoteException;

    Optional<Skin> getMinecraftSkinByNick(String nickname) throws RemoteException;

    Optional<Skin> getMinecraftSkinById(String id) throws RemoteException;
}
