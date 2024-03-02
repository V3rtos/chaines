package me.moonways.bridgenet.model.auth;

import org.jetbrains.annotations.Nullable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Account extends Remote {

    /**
     * Получить идентификатор зарегистрированного пользователя.
     */
    UUID getId() throws RemoteException;

    /**
     * Получить имя зарегистрированного пользователя.
     */
    String getName() throws RemoteException;

    /**
     * Получить информацию о дополнительной защите зарегистрированного пользователя.
     * Если ни одна дополнительная защита аккаунта не подключена, то данная функция вернет null.
     */
    @Nullable
    Account2FA getAdditionalSecurity() throws RemoteException;

    /**
     * Получить информацию о сессии зарегистрированного пользователя.
     * Если сессия по каким-то причинам не найдена, то данная функция вернет null.
     */
    @Nullable
    AuthenticationSession getSession() throws RemoteException;

    /**
     * Проверить на наличие хоть одной подключенной дополнительной
     * защиты зарегистрированного пользователя. Данная функция вернет TRUE
     * в случае, если нашлась хоть одна система защиты.
     */
    boolean hasAdditionalSecurity() throws RemoteException;

    /**
     * Проверить на наличие АКТИВНОЙ сессии пользователя.
     * Если сессии у пользователя не существует вообще, либо она есть, но
     * она не является активной, то в обоих случаях данная функция вернет FALSE.
     */
    boolean hasActiveSession() throws RemoteException;
}
