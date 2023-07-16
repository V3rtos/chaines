package me.moonways.bridgenet.rsi.dynamic;

import java.io.Serializable;
import java.rmi.RemoteException;

public class DynamicServiceImpl implements DynamicService, Serializable {

    private static final long serialVersionUID = 8962973504321300596L;

    @Override
    public IDynamicEmulator getEmulator() throws RemoteException {
        return new DynamicEmulator();
    }
}
