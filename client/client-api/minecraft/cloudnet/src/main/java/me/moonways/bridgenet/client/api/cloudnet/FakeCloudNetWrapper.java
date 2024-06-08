package me.moonways.bridgenet.client.api.cloudnet;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.assembly.ini.IniConfig;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public final class FakeCloudNetWrapper implements CloudNetWrapper {

    private static final String SERVICE_NAME_FORMAT = "%s-%d";

    private IniConfig clientInfoConfig;

    @Inject
    private ResourcesAssembly assembly;

    @PostConstruct
    private void initConfig() {
        assembly.copyLocalResource(ResourcesTypes.FAKE_CLIENT_INI);
        clientInfoConfig = assembly.readIniConfig(ResourcesTypes.FAKE_CLIENT_INI);
    }

    @Override
    public int getCurrentTaskId() {
        return clientInfoConfig.readInt("task", "id", 0);
    }

    @Override
    public String getCurrentTaskName() {
        return clientInfoConfig.readString("task", "name", "");
    }

    @Override
    public String getCurrentFullName() {
        int currentTaskId = getCurrentTaskId();
        String currentTaskName = getCurrentTaskName();

        return String.format(SERVICE_NAME_FORMAT, currentTaskName, currentTaskId);
    }

    @Override
    public InetSocketAddress getCurrentAddress() {
        return new InetSocketAddress(getCurrentSnapshotHost(), getCurrentSnapshotPort());
    }

    @Override
    public int getCurrentSnapshotPort() {
        return clientInfoConfig.readInt("snapshot", "port", 0);
    }

    @Override
    public String getCurrentSnapshotHost() {
        return clientInfoConfig.readString("snapshot", "host", "127.0.0.1");
    }
}
