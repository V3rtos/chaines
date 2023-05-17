package me.moonways.bridgenet.api.connection.server;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.net.InetSocketAddress;

public final class AddressServerMap {

    private static final String SERVER_NOT_CONTAINS_ERR_MSG = "Server Port is not contains in ServerChannelMap (%d)";
    private static final String SERVER_ALREADY_CONTAINS_ERR_MSG = "Server Port is already contains in ServerChannelMap (%d)";

    private final TIntObjectMap<Server> serverByAddressPortMap =
            TCollections.synchronizedMap(new TIntObjectHashMap<>());

    private void validateServer(Server server) {
        if (server == null) {
            throw new NullPointerException("server");
        }
    }

    private void validateServerNotContains(InetSocketAddress socketAddress) {
        int serverPort = socketAddress.getPort();
        if (!serverByAddressPortMap.containsKey(serverPort)) {
            throw new NullPointerException(String.format(SERVER_NOT_CONTAINS_ERR_MSG, serverPort));
        }
    }

    private void validateServerAlreadyContains(InetSocketAddress socketAddress) {
        int serverPort = socketAddress.getPort();
        if (serverByAddressPortMap.containsKey(serverPort)) {
            throw new NullPointerException(String.format(SERVER_ALREADY_CONTAINS_ERR_MSG, serverPort));
        }
    }

    public void registerServerAddressPort(@NotNull Server server) {
        validateServer(server);

        InetSocketAddress inetSocketAddress = server.getBridgenetChannel().getInetSocketAddress();
        validateServerAlreadyContains(inetSocketAddress);

        serverByAddressPortMap.put(inetSocketAddress.getPort(), server);
    }

    public void unregisterServerAddressPort(@NotNull Server server) {
        validateServer(server);

        InetSocketAddress inetSocketAddress = server.getBridgenetChannel().getInetSocketAddress();
        validateServerNotContains(inetSocketAddress);

        serverByAddressPortMap.remove(inetSocketAddress.getPort());
    }

    @Nullable
    public Server getServerByAddressPort(int serverPort) {
        return serverByAddressPortMap.get(serverPort);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <S extends Server> S getUncheckedServer(int serverPort) {
        return (S) serverByAddressPortMap.get(serverPort);
    }
}
