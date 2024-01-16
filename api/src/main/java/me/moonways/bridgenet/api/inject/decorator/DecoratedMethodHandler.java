package me.moonways.bridgenet.api.inject.decorator;

/**
 * Обработчик декорированных методов.
 */
public interface DecoratedMethodHandler {

    /**
     * Обработать метод, помеченный декораторами.
     * @param invocation - процесс вызова декоратора.
     */
    Object handleProxyInvocation(DecoratorInvocation invocation);
}
