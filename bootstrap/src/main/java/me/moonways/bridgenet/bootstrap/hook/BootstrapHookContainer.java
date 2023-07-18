package me.moonways.bridgenet.bootstrap.hook;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.xml.XmlJaxbParser;
import me.moonways.bridgenet.bootstrap.xml.XmlBootstrap;
import me.moonways.bridgenet.bootstrap.xml.XmlHook;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public final class BootstrapHookContainer {

    private static final String BOOTSTRAP_XML_CONFIGURATION_NAME = "bootstrap.xml";

    private final Map<Class<? extends BootstrapHook>, BootstrapHook> instancesByHooksTypesMap = new HashMap<>();
    private final Map<Class<?>, XmlHook> xmlByHooksTypesMap = new HashMap<>();
    private final Map<Class<?>, BootstrapHookPriority> prioririesByHooksTypesMap = new HashMap<>();

    @Inject
    private DependencyInjection dependencyInjection;

    private XmlBootstrap parseConfiguration() {
        final XmlJaxbParser parser = new XmlJaxbParser();

        ClassLoader classLoader = getClass().getClassLoader();
        return parser.parseCopiedResource(classLoader, BOOTSTRAP_XML_CONFIGURATION_NAME,
                XmlBootstrap.class);
    }

    public void injectHooks() {
        log.info("BootstrapHookContainer.injectHooks() => begin;");

        XmlBootstrap xmlBootstrap = parseConfiguration();
        List<XmlHook> hooks = xmlBootstrap.getHooks();

        for (XmlHook xmlHook : hooks) {

            BootstrapHook bootstrapHook = parseHook(xmlHook);
            if (bootstrapHook == null)
                continue;

            log.info("Hook '{}' was success parsed", xmlHook.getDisplayName());

            Class<? extends BootstrapHook> cls = bootstrapHook.getClass();

            xmlByHooksTypesMap.put(cls, xmlHook);
            instancesByHooksTypesMap.put(cls, bootstrapHook);
        }

        log.info("BootstrapHookContainer.injectHooks() => end;");
    }

    private BootstrapHook parseHook(XmlHook xmlHook) {
        final String displayName = xmlHook.getDisplayName();
        final String priorityName = xmlHook.getPriority();
        final String executorPath = xmlHook.getExecutorPath();

        try {
            final Class<?> cls = Class.forName(executorPath);
            Class<? extends BootstrapHook> hookCls = cls.asSubclass(BootstrapHook.class);

            BootstrapHookPriority priority = Enum.valueOf(BootstrapHookPriority.class, priorityName.toUpperCase());
            prioririesByHooksTypesMap.put(hookCls, priority);

            return hookCls.newInstance();
        }
        catch (Exception exception) {
            log.error("§4Cannot be parse hook '{}' from xml", displayName, exception);
        }

        return null;
    }

    public String findHookName(Class<? extends BootstrapHook> cls) {
        return xmlByHooksTypesMap.get(cls).getDisplayName();
    }

    public int findHookPriorityID(Class<? extends BootstrapHook> cls) {
        final XmlHook xmlHook = xmlByHooksTypesMap.get(cls);
        final int defaultPriorityID = -1;

        if (xmlHook == null) {
            return defaultPriorityID;
        }

        final String priorityIDString = xmlHook.getPriorityID();
        if (priorityIDString == null) {
            return defaultPriorityID;
        }

        return Integer.parseInt(priorityIDString);
    }

    public BootstrapHook findHookInstance(Class<? extends BootstrapHook> cls) {
        return instancesByHooksTypesMap.get(cls);
    }

    public BootstrapHookPriority findHookPriority(Class<? extends BootstrapHook> cls) {
        return prioririesByHooksTypesMap.get(cls);
    }

    public Collection<BootstrapHook> getRegisteredHooks(@NotNull BootstrapHookPriority scope) {
        return instancesByHooksTypesMap.keySet()
                .stream()
                .filter(cls -> Objects.equals(findHookPriority(cls), scope))
                .map(this::findHookInstance)
                .collect(Collectors.toSet());
    }
}
