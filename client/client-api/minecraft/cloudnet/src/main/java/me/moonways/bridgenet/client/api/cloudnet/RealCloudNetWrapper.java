package me.moonways.bridgenet.client.api.cloudnet;

import de.dytanic.cloudnet.driver.network.HostAndPort;
import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.wrapper.Wrapper;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public final class RealCloudNetWrapper implements CloudNetWrapper {
    private static final String SERVICE_NAME_FORMAT = "%s-%d";

    private final Wrapper cloudNet;

    private ServiceId getCurrentService() {
        ServiceInfoSnapshot currentServiceInfoSnapshot = cloudNet.getCurrentServiceInfoSnapshot();
        return currentServiceInfoSnapshot.getServiceId();
    }

    private HostAndPort getCurrentServiceAddress() {
        ServiceInfoSnapshot currentServiceInfoSnapshot = cloudNet.getCurrentServiceInfoSnapshot();
        return currentServiceInfoSnapshot.getAddress();
    }

    @Override
    public int getCurrentTaskId() {
        ServiceId currentService = getCurrentService();
        return currentService.getTaskServiceId();
    }

    @Override
    public String getCurrentTaskName() {
        ServiceId currentService = getCurrentService();
        return currentService.getTaskName();
    }

    @Override
    public String getCurrentFullName() {
        int currentTaskId = getCurrentTaskId();
        String currentTaskName = getCurrentTaskName();

        return String.format(SERVICE_NAME_FORMAT, currentTaskName, currentTaskId);
    }

    @Override
    public InetSocketAddress getCurrentAddress() {
        HostAndPort address = getCurrentServiceAddress();
        return new InetSocketAddress(address.getHost(), address.getPort());
    }

    @Override
    public int getCurrentSnapshotPort() {
        HostAndPort address = getCurrentServiceAddress();
        return address.getPort();
    }

    @Override
    public String getCurrentSnapshotHost() {
        HostAndPort address = getCurrentServiceAddress();
        return address.getHost();
    }
}
