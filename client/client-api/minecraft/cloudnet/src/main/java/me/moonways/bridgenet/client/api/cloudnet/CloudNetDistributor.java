package me.moonways.bridgenet.client.api.cloudnet;

import de.dytanic.cloudnet.wrapper.Wrapper;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;

import java.util.Optional;
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
        return createRealWrapper()
                .map(realCloudNetWrapper -> (CloudNetWrapper) realCloudNetWrapper)
                .orElseGet(FakeCloudNetWrapper::new);
    }

    private Optional<RealCloudNetWrapper> createRealWrapper() {
        try {
            Class.forName("de.dytanic.cloudnet.wrapper.Wrapper");
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }

        Wrapper realWrapper = Wrapper.getInstance();
        if (realWrapper == null) {
            return Optional.empty();
        }

        return Optional.of(new RealCloudNetWrapper(realWrapper));
    }
}
