package me.moonways.bridgenet.test.services;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.mojang.MojangServiceModel;
import me.moonways.bridgenet.model.service.mojang.Skin;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Optional;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class MojangServiceEndpointTest {

    @Inject
    private MojangServiceModel subj;

    @Test
    public void test_checkPiratesNicknames() throws RemoteException {
        assertFalse(subj.isPirateNick(TestConst.Mojang.LICENSED_NICK));
        assertTrue(subj.isPirateNick(TestConst.Mojang.PIRATE_NICK));
    }

    @Test
    public void test_getMinecraftId() throws RemoteException {
        Optional<String> idOptional = subj.getMinecraftId(TestConst.Mojang.LICENSED_NICK);

        assertTrue(idOptional.isPresent());
        assertEquals(idOptional.get(), TestConst.Mojang.LICENSED_ID);

        log.debug(idOptional.get());
    }

    @Test
    public void test_getOriginNameCase() throws RemoteException {
        Optional<String> nameWithOriginCaseOptional = subj.getNameWithOriginCase(TestConst.Mojang.LICENSED_NICK.toUpperCase());

        assertTrue(nameWithOriginCaseOptional.isPresent());
        assertNotEquals(nameWithOriginCaseOptional.get(), TestConst.Mojang.LICENSED_NICK.toUpperCase());

        log.debug(nameWithOriginCaseOptional.get());
    }

    @Test
    public void test_getSkin() throws RemoteException {
        Optional<Skin> skinOptional = subj.getMinecraftSkinByNick(TestConst.Mojang.LICENSED_NICK);

        assertTrue(skinOptional.isPresent());
        assertEquals(skinOptional.get().getNickname(), TestConst.Mojang.LICENSED_NICK);

        log.debug(skinOptional.get());
    }
}
