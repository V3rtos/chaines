package me.moonways.bridgenet.test.engine.component.step.impl;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.model.service.players.PlayersServiceModel;
import me.moonways.bridgenet.test.data.ExampleClient;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.component.module.impl.ClientModule;
import me.moonways.bridgenet.test.engine.component.step.StepAdapter;
import me.moonways.bridgenet.test.engine.component.step.StepConfig;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

import java.util.Collections;

@Log4j2
public class JoinPlayerStep extends StepAdapter {
    public JoinPlayerStep() {
        super(StepConfig.builder()
                .beansDependencies(
                        Collections.singletonList(
                                PlayersServiceModel.class
                        ))
                .modulesDependencies(
                        Collections.singletonList(
                                ClientModule.class
                        ))
                .build());
    }

    @Inject
    private ExampleClient exampleClient;

    @Override
    public void execute(TestFlowContext context) {
        BridgenetServerSync bridgenetServerSync = exampleClient.getBridgenetServerSync();

        bridgenetServerSync.exportUserHandshake(TestConst.Player.DESC);
        bridgenetServerSync.exportUserRedirectToHere(TestConst.Player.ID);
    }
}
