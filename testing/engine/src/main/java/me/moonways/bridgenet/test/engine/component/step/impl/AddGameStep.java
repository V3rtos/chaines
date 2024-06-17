package me.moonways.bridgenet.test.engine.component.step.impl;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
import me.moonways.bridgenet.client.api.BridgenetGamesSync;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.model.service.games.GamesServiceModel;
import me.moonways.bridgenet.model.service.servers.ServersServiceModel;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.data.management.ExampleClientConnection;
import me.moonways.bridgenet.test.engine.component.step.StepAdapter;
import me.moonways.bridgenet.test.engine.component.step.StepConfig;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

import java.util.Arrays;

public class AddGameStep extends StepAdapter {
    public AddGameStep() {
        super(StepConfig.builder()
                .beansDependencies(
                        Arrays.asList(
                                ServersServiceModel.class,
                                GamesServiceModel.class
                        ))
                .build());
    }

    @Inject
    private ExampleClientConnection exampleClientConnection;

    @Override
    public void execute(TestFlowContext context) {
        BridgenetServerSync bridgenetServerSync = createServerSync();
        BridgenetGamesSync bridgenetGamesSync = createGamesSync(bridgenetServerSync);

        Handshake.Result handshakeResult = bridgenetServerSync.exportClientHandshake(TestConst.Server.DESC);

        if (handshakeResult instanceof Handshake.Success) {
            bridgenetGamesSync.gameCreate(TestConst.Game.GAME_DTO);
        }
    }

    private BridgenetServerSync createServerSync() {
        BridgenetServerSync bridgenetServerSync = new BridgenetServerSync();

        ReflectionUtils.setField(bridgenetServerSync, "channel", exampleClientConnection.getChannel());
        return bridgenetServerSync;
    }

    private BridgenetGamesSync createGamesSync(BridgenetServerSync bridgenetServerSync) {
        return new BridgenetGamesSync(bridgenetServerSync);
    }
}
