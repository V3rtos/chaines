package me.moonways.bridgenet.rsi.endpoint;

import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;

import java.util.Collections;
import java.util.List;

public class EndpointController {

    private final EndpointRunner runner = new EndpointRunner();
    private final EndpointLoader loader = new EndpointLoader();

    @Inject
    private DependencyInjection dependencyInjection;

    private List<Endpoint> endpointsList;

    public void injectInternalComponents() {
        dependencyInjection.injectFields(runner);
        dependencyInjection.injectFields(loader);
    }

    public void findEndpoints() {
        endpointsList = loader.lookupStoredEndpoints();
    }

    public void bindEndpoints() {
        for (Endpoint endpoint : endpointsList) {
            runner.start(endpoint);
        }
    }

    public List<Endpoint> getEndpoints() {
        return Collections.unmodifiableList(endpointsList);
    }
}
