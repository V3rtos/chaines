package me.moonways.bridgenet.mtp.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.config.descriptor.NetworkSettingsDescriptor;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;

@Getter
@ToString
@RequiredArgsConstructor
public final class NetworkJsonConfiguration {

    private final NetworkSettingsDescriptor settings;
    private final MessageEncryption encryption;
}
