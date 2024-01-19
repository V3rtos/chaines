package me.moonways.endpoint.players.social;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.players.social.PlayerSocials;
import me.moonways.bridgenet.model.players.social.SocialContainer;

import java.rmi.RemoteException;
import java.util.UUID;

public class PlayerSocialsStub extends AbstractEndpointDefinition implements PlayerSocials {

    public PlayerSocialsStub() throws RemoteException {
        super();
    }

    @Override
    public SocialContainer lookup(String playerName) {
        return null;
    }

    @Override
    public SocialContainer lookup(UUID playerUuid) {
        return null;
    }

    @Override
    public void export(String playerName, SocialContainer container) {

    }

    @Override
    public void export(UUID playerUuid, SocialContainer container) {

    }
}
