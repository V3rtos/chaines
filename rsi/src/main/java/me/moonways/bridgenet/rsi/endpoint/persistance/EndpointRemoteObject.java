package me.moonways.bridgenet.rsi.endpoint.persistance;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class EndpointRemoteObject extends UnicastRemoteObject {

    private static final long serialVersionUID = -1564328046265570096L;
    private final EndpointRemoteContext endpointContext = new EndpointRemoteContext();

    @Inject
    private BeansService beansService;

    public EndpointRemoteObject() throws RemoteException {
        super();
    }

    @PostConstruct
    private void internal_postConstruct() {
        beansService.inject(endpointContext);

        construct(endpointContext);
        injectInternal();
    }

    protected void construct(EndpointRemoteContext context) {
        // override me.
    }

    protected final EndpointRemoteContext getEndpointContext() {
        return endpointContext;
    }

    /**
     * Заполняем инжекцией переменные, которые
     * проинициализированы и находятся в нашем
     * объекте эндпоинта.
     */
    private void injectInternal() {
        for (Field declaredField : getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(this);

                if (value != null) {
                    Class<?> type = value.getClass();
                    if (type.getPackage().getName().startsWith("me.moonways")) {
                        if (type.isAssignableFrom(Autobind.class)) {
                            beansService.bind(value);
                        } else {
                            beansService.fakeBind(value);
                        }
                    }
                }
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
