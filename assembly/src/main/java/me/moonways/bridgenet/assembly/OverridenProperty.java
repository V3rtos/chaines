package me.moonways.bridgenet.assembly;

import java.util.function.Supplier;

public interface OverridenProperty {

    Supplier<String> SYSTEM_NAME            = () -> System.getProperty("system.name");
    Supplier<String> SYSTEM_VERSION         = () -> System.getProperty("system.version");

    Supplier<String> BEANS_PACKAGE          = () -> System.getProperty("beans.package");
    Supplier<String> BEANS_FACTORY_DEFAULT  = () -> System.getProperty("beans.factory.default");
}
