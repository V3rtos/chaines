package me.moonways.bridgenet.mtp.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.util.json.AbstractJsonConfig;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;

@Log4j2
@Getter
@ToString
public final class MTPConfiguration extends AbstractJsonConfig<Settings> {

    private Settings settings;
    private MessageEncryption encryption;

    public MTPConfiguration() {
        super(Settings.class, ResourcesTypes.MTP_SETTINGS_JSON);
    }

    @Override
    protected void doReload(Settings settings) {
        this.settings = settings;
        this.encryption = new MessageEncryption(settings.getSecurity());
    }
}
