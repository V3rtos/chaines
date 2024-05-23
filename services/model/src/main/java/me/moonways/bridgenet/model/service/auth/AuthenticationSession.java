package me.moonways.bridgenet.model.service.auth;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

/**
 * Данный интерфейс описывает поведение
 * сохраненной сессии аккаунта после попыток
 * регистрации или входа в аккаунт.
 */
public interface AuthenticationSession extends Remote {

    /**
     * Получить дату последней успешной авторизации аккаунта.
     */
    Timestamp getLastAuthenticationDate() throws RemoteException;

    /**
     * Получить последний IP-адрес, под которым был
     * воспроизведен вход в аккаунт или регистрация.
     */
    String getLastAuthenticationIp() throws RemoteException;

    /**
     * Проверить, истекла ли сессия на данный момент.
     * Если сессия истекла, то это не мешает ей быть активной,
     * так как во время пребывания пользователя
     * на том или ином сервере никто не будет просить
     * пользователя перезайти в аккаунт
     * в рандомный для него момент.
     */
    boolean isExpired() throws RemoteException;

    /**
     * Проверить, активная ли сессия на актуальный момент.
     * Это означает что если пользователь на данный момент в сети,
     * то не играет роли истекла ли сессия на данный момент, здесь
     * важен только факт его активности.
     */
    boolean isActive() throws RemoteException;

    /**
     * Проверить, является ли сессия неактивной на данный момент.
     * Это означает что если пользователь на данный момент не в сети,
     * то не играет роли истекла ли сессия на данный момент, здесь
     * важен только факт его неактивности.
     */
    boolean isInactive() throws RemoteException;
}
