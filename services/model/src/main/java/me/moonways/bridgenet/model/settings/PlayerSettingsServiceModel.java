package me.moonways.bridgenet.model.settings;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

public interface PlayerSettingsServiceModel extends RemoteService {

    /**
     * Получить список идентификаторов всех возможных
     * пользовательских настроек
     */
    Collection<SettingID<?>> getTotalSettings() throws RemoteException;

    /**
     * Получить параметры пользовательской настройки по
     * уникальному идентификатору пользователя.
     *
     * @param playerId - уникальный идентификатор пользователя.
     * @param id - идентификатор пользовательской настройки.
     */
    <T> Setting<T> getSetting(UUID playerId, SettingID<T> id) throws RemoteException;

    /**
     * Получить параметры пользовательской настройки по
     * имени пользователя.
     *
     * @param playerName - имя пользователя.
     * @param id - идентификатор пользовательской настройки.
     */
    <T> Setting<T> getSetting(String playerName, SettingID<T> id) throws RemoteException;
}
