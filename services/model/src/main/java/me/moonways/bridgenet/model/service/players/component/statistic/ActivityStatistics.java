package me.moonways.bridgenet.model.service.players.component.statistic;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ActivityStatistics extends Remote {

    /**
     * Сбросить все статистики активности игрока.
     */
    void reset() throws RemoteException;

    /**
     * Установить значение целочисленной статистики.
     *
     * @param statistic - Статистика, для которой нужно установить значение.
     * @param value     - Значение статистики.
     */
    void setInt(Statistic statistic, int value) throws RemoteException;

    /**
     * Установить значение длинной статистики.
     *
     * @param statistic - Статистика, для которой нужно установить значение.
     * @param value     - Значение статистики.
     */
    void setLong(Statistic statistic, long value) throws RemoteException;

    /**
     * Получить значение целочисленной статистики.
     *
     * @param statistic - Статистика, для которой нужно получить значение.
     * @return - Значение целочисленной статистики.
     */
    int getInt(Statistic statistic) throws RemoteException;

    /**
     * Получить значение длинной статистики.
     *
     * @param statistic - Статистика, для которой нужно получить значение.
     * @return - Значение длинной статистики.
     */
    long getLong(Statistic statistic) throws RemoteException;
}
