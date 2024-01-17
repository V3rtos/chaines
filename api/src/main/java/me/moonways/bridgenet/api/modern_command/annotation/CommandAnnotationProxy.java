package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

import java.lang.reflect.Method;

@MethodInterceptor
public class CommandAnnotationProxy {

    @Inject
    private CommandAnnotationService service;

    @MethodHandler
    public void handle(ProxiedMethod method, Object[] args) {
        Method declare = method.getDeclare();

        if (!service.isSubcommand(declare)) {
            method.call(args);
            return;
        }

        CommandSession commandSession = ((CommandSession) args[0]);
        service.processCommandAnnotations(commandSession, declare);

        method.call(args);
    }
}
