package me.moonways.bridgenet.connector.cloudnet;

import de.dytanic.cloudnet.driver.network.HostAndPort;
import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.moonways.bridgenet.service.inject.Component;

import java.net.InetSocketAddress;

@Component
public final class CloudnetWrapper {

    private static final Wrapper WRAPPER = Wrapper.getInstance();
    private static final String SERVICE_NAME_FORMAT = "%s-%d";

    private ServiceId getCurrentService() {
        ServiceInfoSnapshot currentServiceInfoSnapshot = WRAPPER.getCurrentServiceInfoSnapshot();
        return currentServiceInfoSnapshot.getServiceId();
    }

    private HostAndPort getCurrentServiceAddress() {
        ServiceInfoSnapshot currentServiceInfoSnapshot = WRAPPER.getCurrentServiceInfoSnapshot();
        return currentServiceInfoSnapshot.getAddress();
    }

    public int getCurrentTaskId() {
        ServiceId currentService = getCurrentService();
        return currentService.getTaskServiceId();
    }

    public String getCurrentTaskName() {
        ServiceId currentService = getCurrentService();
        return currentService.getTaskName();
    }

    public String getFullCurrentServiceName() {
        int currentTaskId = getCurrentTaskId();
        String currentTaskName = getCurrentTaskName();

        return String.format(SERVICE_NAME_FORMAT, currentTaskName, currentTaskId);
    }

    public InetSocketAddress buildSocketAddress() {
        HostAndPort address = getCurrentServiceAddress();
        return new InetSocketAddress(address.getHost(), address.getPort());
    }

    public int getCurrentSnapshotPort() {
        HostAndPort address = getCurrentServiceAddress();
        return address.getPort();
    }

    public String getCurrentSnapshotHost() {
        HostAndPort address = getCurrentServiceAddress();
        return address.getHost();
    }
}
