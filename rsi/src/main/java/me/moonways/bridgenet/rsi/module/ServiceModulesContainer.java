package me.moonways.bridgenet.rsi.module;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rsi.service.ServiceInfo;

import java.util.Collection;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class ServiceModulesContainer {

    private final TIntObjectMap<Module<?>> injectedModules = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    @ToString.Include
    private final ServiceInfo serviceInfo;

    public void injectModule(ModuleFactory moduleFactory) {
        int namespaceId = moduleFactory.getId().getNamespaceId();

        Module<?> module = moduleFactory.create(serviceInfo);

        injectedModules.put(namespaceId, module);
    }

    public <M extends Module<?>> M getModuleInstance(int namespaceId) {
        Module<?> module = injectedModules.get(namespaceId);

        //noinspection unchecked
        return (M) module;
    }

    public <M extends Module<?>> M getModuleInstance(Class<M> cls) {
        Collection<Module<?>> modulesCollection = injectedModules.valueCollection();

        for (Module<?> moduleInstance : modulesCollection) {
            if (moduleInstance.getClass().equals(cls)) {

                //noinspection unchecked
                return (M) moduleInstance;
            }
        }

        return null;
    }
}
