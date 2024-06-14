package me.moonways.endpoint.mojang;

import me.moonways.bridgenet.model.service.mojang.MojangServiceModel;
import me.moonways.bridgenet.model.service.mojang.Skin;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;
import java.util.Optional;

public final class MojangServiceEndpoint extends EndpointRemoteObject implements MojangServiceModel {
    private static final long serialVersionUID = -4991953439627385251L;

    private final MojangApi mojangApi = new MojangApi();

    public MojangServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        context.bind(mojangApi);
    }

    @Override
    public boolean isPirateNick(String nickname) throws RemoteException {
        return !mojangApi.getId(nickname).isPresent();
    }

    @Override
    public boolean isPirateId(String id) throws RemoteException {
        return !mojangApi.getNick(id).isPresent();
    }

    @Override
    public Optional<String> getNameWithOriginCase(String nickname) throws RemoteException {
        Optional<String> minecraftIdOptional = getMinecraftId(nickname);
        if (minecraftIdOptional.isPresent()) {
            return getMinecraftNick(minecraftIdOptional.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getMinecraftId(String nickname) throws RemoteException {
        return mojangApi.getId(nickname);
    }

    @Override
    public Optional<String> getMinecraftNick(String id) throws RemoteException {
        return mojangApi.getNick(id);
    }

    @Override
    public Optional<Skin> getMinecraftSkinByNick(String nickname) throws RemoteException {
        Optional<String> minecraftIdOptional = getMinecraftId(nickname);
        if (minecraftIdOptional.isPresent()) {
            return getMinecraftSkinById(minecraftIdOptional.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Skin> getMinecraftSkinById(String id) throws RemoteException {
        return mojangApi.getProfile(id).map(profile ->
                Skin.builder()
                        .id(profile.getId())
                        .nickname(profile.getName())
                        .texture(profile.getProperties()[0].getValue())
                        .signature(profile.getProperties()[0].getSignature())
                        .build());
    }
}
