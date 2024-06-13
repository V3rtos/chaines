package me.moonways.bridgenet.model.service.mojang;

import me.moonways.bridgenet.rmi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

public interface MojangServiceModel extends RemoteService {

    boolean isPirate(String nickname) throws RemoteException;

    boolean isPirate(UUID id) throws RemoteException;

    Optional<UUID> getMinecraftId(String nickname) throws RemoteException;

    Optional<Skin> getMinecraftSkin(String nickname) throws RemoteException;

    Optional<Skin> getMinecraftSkin(UUID id) throws RemoteException;

    Optional<Cape> getMinecraftCape(String nickname) throws RemoteException;

    Optional<Cape> getMinecraftCape(UUID id) throws RemoteException;
}
