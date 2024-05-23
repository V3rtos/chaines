package me.moonways.bridgenet.mtp.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.util.json.AbstractJsonConfig;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.mtp.config.descriptor.NetworkSettingsDescriptor;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;

@Log4j2
@Getter
@ToString
public final class NetworkJsonConfiguration extends AbstractJsonConfig<NetworkSettingsDescriptor> {

    private NetworkSettingsDescriptor settings;
    private MessageEncryption encryption;

    public NetworkJsonConfiguration() {
        super(NetworkSettingsDescriptor.class, ResourcesTypes.MTP_CONFIG_JSON);
    }

    @Override
    protected void doReload(NetworkSettingsDescriptor settings) {
        this.settings = settings;
        this.encryption = new MessageEncryption(settings.getSecurity());
    }
}
