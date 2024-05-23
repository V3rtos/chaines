package me.moonways.bridgenet.test.engine.module.impl;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.servers.ServersServiceModel;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.rsi.service.RemoteServicesManagement;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Arrays;

@Log4j2
public class RmiServicesModule extends TestEngineModuleAdapter {

    private static final String SERVICES_MODEL_PACKAGE = "me.moonways.bridgenet.model";

    @Inject
    private BridgenetNetworkChannel channel;
    @Inject
    private RemoteServicesManagement remoteServicesManagement;

    public RmiServicesModule() {
        super(TestModuleBeans.builder()
                .dependencies(
                        Arrays.asList(
                                SchedulersModule.class,
                                EventsModule.class,
                                CommandsModule.class,
                                DatabasesModule.class,
                                MtpModule.class
                        ))
                .packagesToScanning(
                        Arrays.asList(
                                SERVICES_MODEL_PACKAGE,
                                fromClassPackage(RemoteServicesManagement.class)
                        ))
                .build());
    }

    @Override
    public void onInstall(TestFlowContext context) {
        remoteServicesManagement.subscribeOnExported(ServersServiceModel.class,
                serversServiceModel -> {

                    log.debug("Pulling testing Server instance Handshake...");
                    channel.pull(new Handshake(Handshake.Type.SERVER, TestConst.Server.DESC.toProperties()));
                });

        remoteServicesManagement.subscribeOnExported(PlayersServiceModel.class,
                playersServiceModel -> {

                    log.debug("Pulling testing Player instance Handshake...");
                    channel.pull(new Handshake(Handshake.Type.PLAYER, TestConst.Player.DESC.toProperties()));
                });
    }
}
