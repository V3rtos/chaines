package me.moonways.bridgenet.rsi.service;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rsi.module.*;
import me.moonways.bridgenet.rsi.xml.XMLConfiguration;
import me.moonways.bridgenet.rsi.xml.XMLConfigurationParser;
import me.moonways.bridgenet.rsi.xml.element.XMLModule;
import me.moonways.bridgenet.rsi.xml.element.XMLRootElement;
import me.moonways.bridgenet.rsi.xml.element.XMLService;
import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.Inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@Log4j2
@Component
public final class RemoteServiceRegistry {

    private XMLConfiguration configurationInstance;

    private final Map<ServiceInfo, RemoteService> services = Collections.synchronizedMap(new HashMap<>());
    private final Map<ModuleID, ModuleFactory> modulesFactories = Collections.synchronizedMap(new HashMap<>());

    @Inject
    private DependencyInjection dependencyInjection;

    @PostFactoryMethod
    void init() {
        XMLConfigurationParser parser = new XMLConfigurationParser();
        configurationInstance = parser.parseNewInstance();

        XMLRootElement rootElement = configurationInstance.getRootElement();
        log.info("Parsed RMI XML-Configuration content: {}", rootElement);

        List<XMLModule> xmlModulesList = rootElement.getModulesList();
        List<XMLService> xmlServicesList = rootElement.getServicesList();

        if (xmlModulesList != null) {
            initXmlModules(xmlModulesList);
        }

        if (xmlServicesList != null) {
            initXmlServices(xmlServicesList);
        }
    }

    private void initXmlModules(List<XMLModule> xmlModulesList) {
        log.info("§e{} §rmodules found", xmlModulesList.size());
        log.info("Running modules registration process...");

        for (XMLModule xmlService : xmlModulesList) {

            String name = xmlService.getName().toUpperCase();
            String configClass = xmlService.getConfigClass();
            String targetClass = xmlService.getTargetClass();

            log.info("Registering module: §f{} §r(targetClass={}, configClass={})", name, targetClass, configClass);
            registerModule(xmlService);
        }
    }

    private void initXmlServices(List<XMLService> xmlServicesList) {
        log.info("§e{} §rremote services found", xmlServicesList.size());
        log.info("Running remote services registration process...");

        for (XMLService xmlService : xmlServicesList) {

            String name = xmlService.getName().toUpperCase();
            String bindPort = xmlService.getBindPort();
            String targetType = xmlService.getTargetType();

            log.info("Registering remote service: §f{} §r(port={}, class={})", name, bindPort, targetType);
            registerService(xmlService);
        }
    }

    private ServiceInfo createServiceInfo(XMLService wrapper) {
        String name = wrapper.getName();
        Class<?> serviceTargetClass;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            serviceTargetClass = classLoader.loadClass(wrapper.getTargetType());
        }
        catch (ClassNotFoundException exception) {
            throw new ServiceException(exception);
        }

        if (!RemoteService.class.isAssignableFrom(serviceTargetClass) || !serviceTargetClass.isInterface()) {
            throw new ServiceException("service " + name + " is not valid");
        }

        int port = Integer.parseInt(wrapper.getBindPort());

        @SuppressWarnings("unchecked") ServiceInfo serviceInfo = new ServiceInfo(
                name, port, (Class<? extends RemoteService>) serviceTargetClass
        );

        return serviceInfo;
    }

    private RemoteService findServiceInstance(ServiceInfo serviceInfo) {
        return new RemoteService() {
        }; // todo
    }

    private void registerService(XMLService wrapper) {
        ServiceInfo serviceInfo = createServiceInfo(wrapper);
        RemoteService service = findServiceInstance(serviceInfo);

        if (service == null) {
            throw new ServiceException("Service " + serviceInfo + " is null");
        }

        dependencyInjection.injectFields(service);

        log.info("Service {} was registered", serviceInfo.getName());
        services.put(serviceInfo, service);
    }

    private ModuleID getModuleID(Class<? extends Module> cls) {
        ModuleID moduleID;
        try {
            Module<?> module = cls.newInstance();
            moduleID = module.getId();
        }
        catch (InstantiationException | IllegalAccessException exception) {
            throw new ServiceException(exception);
        };

        return moduleID;
    }

    private ModuleFactory createModuleFactory(XMLModule wrapper) {
        Class<?> targetClass;
        Class<?> configClass;

        try {
            ClassLoader classLoader = getClass().getClassLoader();

            targetClass = classLoader.loadClass(wrapper.getTargetClass());
            configClass = classLoader.loadClass(wrapper.getConfigClass());
        }
        catch (ClassNotFoundException exception) {
            throw new ServiceException(exception);
        }

        final Class<? extends Module> checkedModuleClass = targetClass.asSubclass(Module.class);
        final ModuleID moduleID = getModuleID(checkedModuleClass);

        Function<ServiceInfo, Module<?>> factoryFunc = (serviceInfo) -> {
            try {
                Module<?> module = checkedModuleClass.newInstance();

                dependencyInjection.injectFields(module);

                Method bindMethod = checkedModuleClass.getMethod("bind", XMLConfiguration.class, ServiceInfo.class, Class.class);
                bindMethod.invoke(module, configurationInstance, serviceInfo, configClass);

                return (Module<?>) module;
            }
            catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
                throw new ServiceException(exception);
            }
        };

        return new ModuleFactory(moduleID, factoryFunc);
    }

    private void registerModule(XMLModule wrapper) {
        ModuleFactory moduleFactory = createModuleFactory(wrapper);
        ModuleID id = moduleFactory.getId();

        log.info("{} was registered", id);

        modulesFactories.put(id, moduleFactory);
    }
}
