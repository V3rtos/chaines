package me.moonways.rsap.api.transport;

public interface TransportRef {

    default RemoteServiceCall newCall() {
        return new RemoteServiceCall();
    }

    void call(RemoteServiceCall call);

    void done(RemoteServiceCall call);
}
