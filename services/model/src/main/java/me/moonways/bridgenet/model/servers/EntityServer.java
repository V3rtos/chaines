package me.moonways.bridgenet.model.servers;

import me.moonways.bridgenet.model.players.connection.ConnectedEntityPlayer;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EntityServer extends Remote {
    String CHANNEL_PROPERTY = "entity.server.instance";

    /**
     * Получение уникального идентификатора сервера,
     * под которым он зарегистрирован в системе.
     */
    UUID getUniqueId() throws RemoteException;

    /**
     * Получить название сервера, под которым
     * он сам себя идентифицирует при рукопожатии.
     */
    String getName() throws RemoteException;

    /**
     * Получить канал сервера по протоколу MTP
     * для отправки и обработки сообщений.
     */
    MTPMessageSender getChannel() throws RemoteException;

    /**
     * Получение адреса сервера, который он сам для
     * себя идентифицировал при рукопожатии.
     */
    InetSocketAddress getInetAddress() throws RemoteException;

    /**
     * Подключить какого-то игрока к текущему серверу.
     * @param player - игрок, которого подключаем.
     */
    CompletableFuture<Boolean> connectThat(@NotNull ConnectedEntityPlayer player) throws RemoteException;

    /**
     * Получить список подключенных игроков к серверу.
     */
    Collection<ConnectedEntityPlayer> getConnectedPlayers() throws RemoteException;

    /**
     * Получить общее число онлайна на сервере.
     */
    int getTotalOnline() throws RemoteException;
}
