package me.moonways.api.inject;

import me.moonways.bridgenet.api.inject.DependencyInjection;

public class DecoratorsTestStarter {

    public static void main(String[] args) {
        DependencyInjection dependencyInjection = new DependencyInjection();
        dependencyInjection.findComponentsIntoBasePackage();

        DecoratedObjectStorage instance = (DecoratedObjectStorage) dependencyInjection.getContainer().findInstance(
                DecoratedObjectStorage.class);

        TestDecoratedObject object = instance.getTestProxiedObject();

        object.testVoidLateExecution();
        System.out.println(object.testStringLateExecution());
        object.testKeepTime();
        object.testAsync();
        System.out.println((object.testSingleton() == object.testSingleton()) + " (" + object.testSingleton() + ")");
        object.testRequiredNotNull();
        object.testRequiredNotNullWithCustomMessage();
    }
}
