package me.moonways.bridgenet.model.service.players;

import me.moonways.bridgenet.model.service.players.component.PlayerLeveling;
import me.moonways.bridgenet.model.service.players.component.PlayerStore;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Интерфейс PlayersServiceModel расширяет интерфейс RemoteService и
 * предоставляет методы для работы с игроками в сетевой игре в Minecraft.
 * Этот интерфейс включает методы для получения объекта PlayerLeveling,
 * объекта PlayerStore, общего количества игроков онлайн и списка онлайн игроков.
 */
public interface PlayersServiceModel extends RemoteService {

    /**
     * Получить объект PlayerLeveling для работы с уровнем игроков.
     */
    PlayerLeveling leveling() throws RemoteException;

    /**
     * Получить объект PlayerStore для работы с хранилищем игроков.
     */
    PlayerStore store() throws RemoteException;

    /**
     * Получить общее количество игроков онлайн.
     */
    int getTotalOnline() throws RemoteException;

    /**
     * Получить список онлайн игроков.
     */
    List<Player> getOnlinePlayers() throws RemoteException;
}
