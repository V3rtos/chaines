package me.moonways.bridgenet.client.api.cloudnet;

import de.dytanic.cloudnet.wrapper.Wrapper;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;

import java.util.concurrent.atomic.AtomicReference;

@Autobind
public class CloudNetDistributor {
    private final AtomicReference<CloudNetWrapper> instance = new AtomicReference<>();

    @Inject
    private BeansService beansService;

    public CloudNetWrapper getInstance() {
        return instance.get();
    }

    @PostConstruct
    private void initWrapperInstance() {
        CloudNetWrapper instanceImpl = createCloudnetWrapper();

        beansService.bind(CloudNetWrapper.class, instanceImpl);
        instance.set(instanceImpl);
    }

    private CloudNetWrapper createCloudnetWrapper() {
        Wrapper realWrapper = Wrapper.getInstance();
        CloudNetWrapper instanceImpl;

        if (realWrapper != null) {
            instanceImpl = new RealCloudNetWrapper(realWrapper);
        } else {
            instanceImpl = new FakeCloudNetWrapper();
        }

        return instanceImpl;
    }
}
