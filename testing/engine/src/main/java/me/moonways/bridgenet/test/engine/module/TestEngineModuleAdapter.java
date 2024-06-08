package me.moonways.bridgenet.test.engine.module;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class TestEngineModuleAdapter implements TestEngineModule {

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

    private final TestModuleBeans beans;

    @Override
    public void onInstall(TestFlowContext context) {
        // override me.
    }
}
