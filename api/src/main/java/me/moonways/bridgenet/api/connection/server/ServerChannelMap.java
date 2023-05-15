package me.moonways.bridgenet.api.connection.server;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

public final class ServerChannelMap {

    private static final String SERVER_NOT_CONTAINS_ERR_MSG = "Server Port is not contains in ServerChannelMap (%d)";
    private static final String SERVER_ALREADY_CONTAINS_ERR_MSG = "Server Port is already contains in ServerChannelMap (%d)";

    private final TIntObjectMap<Server> serverByChannelPortMap =
            TCollections.synchronizedMap(new TIntObjectHashMap<>());

    private void validateServer(Server server) {
        if (server == null) {
            throw new NullPointerException("server");
        }
    }

    private void validateServerNotContains(InetSocketAddress socketAddress) {
        int serverPort = socketAddress.getPort();
        if (!serverByChannelPortMap.containsKey(serverPort)) {
            throw new NullPointerException(String.format(SERVER_NOT_CONTAINS_ERR_MSG, serverPort));
        }
    }

    private void validateServerAlreadyContains(InetSocketAddress socketAddress) {
        int serverPort = socketAddress.getPort();
        if (serverByChannelPortMap.containsKey(serverPort)) {
            throw new NullPointerException(String.format(SERVER_ALREADY_CONTAINS_ERR_MSG, serverPort));
        }
    }

    public void registerServerChannelPort(@NotNull Server server) {
        validateServer(server);

        InetSocketAddress inetSocketAddress = server.getBridgenetChannel().getInetSocketAddress();
        validateServerAlreadyContains(inetSocketAddress);

        serverByChannelPortMap.put(inetSocketAddress.getPort(), server);
    }

    public void unregisterServerChannelPort(@NotNull Server server) {
        validateServer(server);

        InetSocketAddress inetSocketAddress = server.getBridgenetChannel().getInetSocketAddress();
        validateServerNotContains(inetSocketAddress);

        serverByChannelPortMap.remove(inetSocketAddress.getPort());
    }

    @Nullable
    public Server getServerByChannelPort(int serverPort) {
        return serverByChannelPortMap.get(serverPort);
    }
}
