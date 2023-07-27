package me.moonways.bridgenet.mtp;

import lombok.Getter;
import lombok.experimental.Delegate;
import me.moonways.bridgenet.api.intercept.AnnotationInterceptor;
import me.moonways.bridgenet.api.injection.DependencyInjection;
import me.moonways.bridgenet.api.injection.PostFactoryMethod;
import me.moonways.bridgenet.api.injection.proxy.intercept.ProxiedObjectProxy;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.MessageHandlerList;
import me.moonways.bridgenet.api.injection.Component;
import me.moonways.bridgenet.api.injection.Inject;

@Getter
@Component
public class MTPDriver {

    @Delegate
    private MessageRegistry messages = new MessageRegistry();

    @Delegate
    private MessageHandlerList handlerList = new MessageHandlerList();

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private AnnotationInterceptor interceptor;

    @PostFactoryMethod
    void init() {
        dependencyInjection.injectFields(messages);
        dependencyInjection.injectFields(handlerList);

        //messages = interceptor.createProxyChecked(messages, new ProxiedObjectProxy());
        //handlerList = interceptor.createProxyChecked(handlerList, new ProxiedObjectProxy());

        handlerList.detectHandlers();
    }
}
