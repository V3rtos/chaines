package me.moonways.bridgenet.assembly;

import java.util.function.Supplier;

/**
 * Здесь хранятся переопределенные системные properties,
 * хранящиеся в конфигурации `config.properties` модуля `assembly`.
 * Хранение значений необходимо прописывать через Supplier для
 * обеспечения корректности получения значения. Все таки здесь у нас
 * лежат константы, которые JVM подгрузит при первой возможности, а
 * сами properties подгружаются во время инициализации BeansService.
 */
public interface OverridenProperty {

    Supplier<Boolean> DEBUG_MODE            = () -> Boolean.parseBoolean(System.getProperty("debug.mode"));
    Supplier<String> SYSTEM_NAME            = () -> System.getProperty("system.name");
    Supplier<String> SYSTEM_VERSION         = () -> System.getProperty("system.version");

    Supplier<String> BEANS_PACKAGE          = () -> System.getProperty("beans.package");
    Supplier<String> BEANS_FACTORY_DEFAULT  = () -> System.getProperty("beans.factory.default");
}
