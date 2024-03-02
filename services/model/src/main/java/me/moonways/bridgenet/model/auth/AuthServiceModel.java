package me.moonways.bridgenet.model.auth;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

public interface AuthServiceModel extends RemoteService {

    /**
     * Найти зарегистрированный аккаунт по идентификатору пользователя.
     * Если пользователя нет в базе данных зарегистрированных, то
     * функция вернет пустой java.util.Optional
     *
     * @param playerId - идентификатор пользователя, по которому ищем аккаунт.
     */
    Optional<Account> findAccountById(UUID playerId) throws RemoteException;

    /**
     * Данная функция воспроизводит попытку авторизации
     * уже существующего пользователя, и в случае если
     * во время этого процесса была обнаружена какая-то
     * ошибка входных данных, мы узнаем о ней конкретнее
     * из описания me.moonways.bridgenet.model.auth.AuthorizationResult
     *
     * @param playerId - идентификатор пользователя.
     * @param inputPassword - пароль от аккаунта, который ввел пользователь в систему.
     */
    AuthorizationResult tryLogin(UUID playerId, String inputPassword) throws RemoteException;

    /**
     * Данная функция воспроизводит попытку регистрации
     * нового пользователя в системе, и в случае если
     * во время этого процесса была обнаружена какая-то
     * ошибка входных данных, мы узнаем о ней конкретнее
     * из описания me.moonways.bridgenet.model.auth.AuthorizationResult
     *
     * @param playerId - идентификатор пользователя.
     * @param inputPassword - новый пароль аккаунта, который ввел пользователь в систему.
     */
    AuthorizationResult tryRegistration(UUID playerId, String inputPassword) throws RemoteException;

    /**
     * Данная функция воспроизводит попытку выхода из аккаунта
     * пользователя в системе, и в случае если
     * во время этого процесса была обнаружена какая-то
     * ошибка входных данных, мы узнаем о ней конкретнее
     * из описания me.moonways.bridgenet.model.auth.AuthorizationResult
     *
     * @param playerId - идентификатор пользователя.
     */
    AuthorizationResult tryLogOut(UUID playerId) throws RemoteException;

    /**
     * Данная функция воспроизводит попытку удаления аккаунта
     * пользователя в системе, и в случае если
     * во время этого процесса была обнаружена какая-то
     * ошибка входных данных, мы узнаем о ней конкретнее
     * из описания me.moonways.bridgenet.model.auth.AuthorizationResult
     *
     * @param playerId - идентификатор пользователя.
     */
    AuthorizationResult tryAccountDelete(UUID playerId) throws RemoteException;
}
