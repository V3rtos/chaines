package me.moonways.bridgenet.test.services;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.model.service.settings.PlayerSettingsServiceModel;
import me.moonways.bridgenet.model.service.settings.Setting;
import me.moonways.bridgenet.model.service.settings.SettingID;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Collection;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class SettingsServiceEndpointTest {

    @Inject
    private PlayerSettingsServiceModel serviceModel;

    @Test
    public void test_totalSettings() throws RemoteException {
        Collection<SettingID<?>> totalSettings = serviceModel.getTotalSettings();

        totalSettings.forEach(settingID ->
                log.debug("Setting type: {}", settingID));

        assertNotNull(totalSettings);
        assertFalse(totalSettings.isEmpty());
    }

    @Test
    public void test_individualSettingGet() throws RemoteException {
        Setting<ChatColor> setting = getExampleSetting();

        log.debug(setting);

        assertNotNull(setting);
        assertFalse(setting.isEnabled());
    }

    @Test
    public void test_individualSettingUpdate() throws RemoteException {
        Setting<ChatColor> setting = getExampleSetting();

        setting.set(ChatColor.GREEN);

        log.debug(setting);

        assertNotNull(setting);
        assertTrue(setting.isEnabled());
    }

    public Setting<ChatColor> getExampleSetting() throws RemoteException {
        return serviceModel.getSetting(TestConst.Player.ID, SettingID.GLOWING_COLOR);
    }
}
