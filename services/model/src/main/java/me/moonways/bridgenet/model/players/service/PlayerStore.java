package me.moonways.bridgenet.model.players.service;

import me.moonways.bridgenet.model.players.OfflinePlayer;
import me.moonways.bridgenet.model.players.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс PlayerStore представляет собой удаленный интерфейс для
 * работы с хранилищем игроков в сетевой игре в Minecraft.
 * Этот интерфейс включает методы для получения офлайн игрока
 * по идентификатору или имени, получения игрока по идентификатору или имени,
 * а также для получения имени игрока по его идентификатору
 * и идентификатора игрока по его имени.
 */
public interface PlayerStore extends Remote {

    /**
     * Получить офлайн игрока по его идентификатору.
     *
     * @param id Идентификатор офлайн игрока.
     * @return - Объект OfflinePlayer, представляющий офлайн игрока.
     */
    OfflinePlayer getOffline(UUID id) throws RemoteException;

    /**
     * Получить офлайн игрока по его имени.
     *
     * @param name Имя офлайн игрока.
     * @return - Объект OfflinePlayer, представляющий офлайн игрока.
     */
    OfflinePlayer getOffline(String name) throws RemoteException;

    /**
     * Получить онлайн игрока по его идентификатору.
     *
     * @param id Идентификатор онлайн игрока.
     * @return - Опциональный объект Player, представляющий онлайн игрока, если такой игрок онлайн, в противном случае пустое значение.
     */
    Optional<Player> get(UUID id) throws RemoteException;

    /**
     * Получить онлайн игрока по его имени.
     *
     * @param name Имя онлайн игрока.
     * @return - Опциональный объект Player, представляющий онлайн игрока, если такой игрок онлайн, в противном случае пустое значение.
     */
    Optional<Player> get(String name) throws RemoteException;

    /**
     * Получить имя игрока по его идентификатору.
     *
     * @param id Идентификатор игрока.
     * @return - Имя игрока.
     */
    String nameById(UUID id) throws RemoteException;

    /**
     * Получить идентификатор игрока по его имени.
     *
     * @param name - Имя игрока.
     * @return - Идентификатор игрока.
     */
    UUID idByName(String name) throws RemoteException;
}
