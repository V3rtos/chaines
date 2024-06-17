package me.moonways.bridgenet.test.engine.component.module;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class ModuleAdapter implements Module {

    protected static final String PACKAGE_PREFIX = "me.moonways.bridgenet";
    private static final String PACKAGE_DELIMITER = ".";

    protected static String packageName(String... names) {
        return PACKAGE_PREFIX.concat(PACKAGE_DELIMITER) + String.join(PACKAGE_DELIMITER, names);
    }

    protected static String absolutePackageName(String... names) {
        return String.join(PACKAGE_DELIMITER, names);
    }

    protected static String fromClassPackage(Class<?> cls) {
        return cls.getPackage().getName();
    }

    private final ModuleConfig config;

    @Override
    public void install(TestFlowContext context) {
        // override me.
    }

    @Override
    public final ModuleConfig config() {
        return config;
    }
}
