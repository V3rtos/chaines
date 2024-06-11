package me.moonways.bridgenet.bootstrap.system;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.OverridenProperty;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggersManager {

    public static final LoggersManager INSTANCE = new LoggersManager();

    public void run() {
        toggleDebugMode();
    }

    /**
     * Переключить DEBUG-режим в зависимости от
     * значения секции в общей properties-конфигурации
     * проекта: "debug.mode"
     */
    private void toggleDebugMode() {
        boolean isDebugModeEnabled = OverridenProperty.DEBUG_MODE.get();
        log.info("debug-mode status: {}", (isDebugModeEnabled ? "ENABLED" : "DISABLED"));

        if (isDebugModeEnabled) {
            Configurator.setRootLevel(Level.DEBUG);
        } else {
            Configurator.setRootLevel(Level.INFO);
        }
    }
}
