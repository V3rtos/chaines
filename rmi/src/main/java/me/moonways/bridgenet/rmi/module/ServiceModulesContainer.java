package me.moonways.bridgenet.rmi.module;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rmi.service.ServiceInfo;

import java.util.Collection;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class ServiceModulesContainer {

    private final TIntObjectMap<RemoteModule<?>> injectedModules = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    @ToString.Include
    private final ServiceInfo serviceInfo;

    public void injectModule(ModuleFactory moduleFactory) {
        int namespaceId = moduleFactory.getId().getNamespaceId();

        RemoteModule<?> module = moduleFactory.create(serviceInfo);

        injectedModules.put(namespaceId, module);
    }

    public <M extends RemoteModule<?>> M getModuleInstance(int namespaceId) {
        RemoteModule<?> module = injectedModules.get(namespaceId);

        //noinspection unchecked
        return (M) module;
    }

    public <M extends RemoteModule<?>> M getModuleInstance(Class<M> cls) {
        Collection<RemoteModule<?>> modulesCollection = injectedModules.valueCollection();

        for (RemoteModule<?> moduleInstance : modulesCollection) {
            if (moduleInstance.getClass().equals(cls)) {

                //noinspection unchecked
                return (M) moduleInstance;
            }
        }

        return null;
    }
}
