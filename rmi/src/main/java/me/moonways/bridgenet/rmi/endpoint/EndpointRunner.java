package me.moonways.bridgenet.rmi.endpoint;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.rmi.module.ServiceModulesContainer;
import me.moonways.bridgenet.rmi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rmi.service.RemoteService;
import me.moonways.bridgenet.rmi.service.RemoteServicesManagement;
import me.moonways.bridgenet.rmi.service.ServiceInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Log4j2
public class EndpointRunner {

    private static final String URL_SPEC_FORMAT = "jar:file:%s!/";

    private final Map<Endpoint, Class<?>> servicesImplementsMap = Collections.synchronizedMap(new HashMap<>());

    @Inject
    private RemoteServicesManagement remoteServicesManagement;

    public void start(Endpoint endpoint) {
        String name = endpoint.getServiceInfo().getName();

        if (!validate(endpoint)) {
            log.warn("§6Endpoint '{}' integration aborted: §eFailed validation", name);
            return;
        }

        bind(endpoint);
    }

    @SneakyThrows
    private boolean validate(Endpoint endpoint) {
        final String name = endpoint.getServiceInfo().getName();
        final EndpointConfig config = endpoint.getConfig();
        Path applicationJarPath = endpoint.getPath().resolve(config.getApplicationJarName());

        if (!Files.exists(applicationJarPath) || Files.readAllBytes(applicationJarPath).length == 0) {
            log.error("§4Application runner file '{}' for '{}' endpoint is not found", applicationJarPath, name);
            return false;
        }

        Class<? extends RemoteService> interfaceClass = endpoint.getServiceInfo().getModelClass();
        Class<?> serviceImplementClass = findServiceImplementClass(name, applicationJarPath, interfaceClass);

        if (serviceImplementClass == null) {
            log.error("§4Founded endpoint '{}' implementation is not implement from '{}'", name, interfaceClass.getName());
            return false;
        }

        servicesImplementsMap.put(endpoint, serviceImplementClass);
        return true;
    }

    @SuppressWarnings("resource")
    private Class<?> findServiceImplementClass(String endpointName, Path applicationJarPath, Class<? extends RemoteService> interfaceClass) {
        File file = applicationJarPath.toFile();

        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> enumeration = jarFile.entries();

            URL[] urls = {new URL(String.format(URL_SPEC_FORMAT, file.getAbsolutePath()))};
            URLClassLoader classLoader = URLClassLoader.newInstance(urls, RemoteService.class.getClassLoader());

            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();

                if (entry.isDirectory() || !entry.getName().endsWith(".class") || !entry.getName().contains("moonways") || entry.getName().contains("api"))
                    continue;

                String className = entry.getName()
                        .substring(0, entry.getName().length() - ".class".length())
                        .replace("/", ".");

                Class<?> serviceImplementClass = classLoader.loadClass(className);

                if (interfaceClass.isAssignableFrom(serviceImplementClass)) {
                    return serviceImplementClass;
                }
            }
        } catch (IOException | ClassNotFoundException exception) {
            log.error("§4Cannot be find endpoint '{}' implement class: §c{}", endpointName, exception.toString());
        }

        return null;
    }

    private void bind(Endpoint endpoint) {
        ServiceInfo serviceInfo = endpoint.getServiceInfo();

        Class<?> serviceImplementClass = servicesImplementsMap.remove(endpoint);
        ServiceModulesContainer serviceModulesContainer = remoteServicesManagement.getModulesContainerMap().get(serviceInfo);

        AccessRemoteModule accessModule = serviceModulesContainer.getModuleInstance(AccessRemoteModule.class);

        if (accessModule == null) {
            log.error("§4Cannot bind endpoint '{}': AccessModule is not injected", serviceInfo.getName());
            return;
        }

        accessModule.setEndpointClass(serviceImplementClass);
        accessModule.exportStub(serviceInfo);
    }
}
