package me.moonways.bridgenet.model.service.players;

import me.moonways.bridgenet.model.service.language.Language;
import me.moonways.bridgenet.model.service.permissions.group.PermissionGroup;
import me.moonways.bridgenet.model.service.permissions.permission.Permission;
import me.moonways.bridgenet.model.audience.EntityAudience;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;

/**
 * Представляет собой удаленный интерфейс д
 * ля управления офлайн игроком в сетевой игре.
 * Этот интерфейс определяет методы для получения идентификатора
 * и имени офлайн игрока, его соединения с сервером,
 * списка разрешений, группы разрешений, уровня игрока и опыта.
 */
public interface OfflinePlayer extends Remote, EntityAudience {

    /**
     * Получить идентификатор офлайн игрока.
     */
    UUID getId() throws RemoteException;

    /**
     * Получить имя офлайн игрока.
     */
    String getName() throws RemoteException;

    /**
     * Получить список разрешений офлайн игрока.
     */
    Set<Permission> getPermissions() throws RemoteException;

    /**
     * Получить группу разрешений офлайн игрока.
     */
    PermissionGroup getGroup() throws RemoteException;

    /**
     * Получить выбранный пользователем тип мирового языка.
     */
    Language getLanguage() throws RemoteException;

    /**
     * Получить уровень офлайн игрока.
     */
    int getLevel() throws RemoteException;

    /**
     * Получить общий опыт офлайн игрока.
     */
    int getTotalExperience() throws RemoteException;

    /**
     * Получить текущий опыт офлайн игрока.
     */
    int getExperience() throws RemoteException;

    /**
     * Получить количество опыта, необходимое для достижения 
     * следующего уровня офлайн игрока.
     */
    int getExperienceToNextLevel() throws RemoteException;

    /**
     * Проверить сущность игрока на то, актуален
     * ли для него сейчас статус "в сети".
     */
    boolean isOnline() throws RemoteException;
}
