package me.moonways.bridgenet.model.service.players;

import me.moonways.bridgenet.model.audience.ComponentHolders;
import me.moonways.bridgenet.model.event.AudienceSendEvent;
import me.moonways.bridgenet.model.service.language.Message;
import me.moonways.bridgenet.model.service.players.component.PlayerConnection;
import me.moonways.bridgenet.model.service.players.component.statistic.ActivityStatistics;
import me.moonways.bridgenet.model.util.Title;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface Player extends OfflinePlayer, Remote {

    /**
     * Получить соединение игрока с серверами
     * единой платформы Bridgenet.
     */
    PlayerConnection getConnection() throws RemoteException;

    /**
     * Получить статистику активности игрока.
     */
    ActivityStatistics getStatistics() throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     * @return - Optional, представляющий результат успешности проведения процесса
     * операции отправки сообщения на панель действий.
     */
    Optional<AudienceSendEvent> sendActionbar(Component message) throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     * @return - Optional, представляющий результат успешности проведения процесса
     * операции отправки сообщения на панель действий.
     */
    Optional<AudienceSendEvent> sendActionbar(Component message, ComponentHolders holders) throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     * @return - Optional, представляющий результат успешности проведения процесса
     * операции отправки сообщения на панель действий.
     */
    Optional<AudienceSendEvent> sendActionbar(Message message) throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     * @return - Optional, представляющий результат успешности проведения процесса
     * операции отправки сообщения на панель действий.
     */
    Optional<AudienceSendEvent> sendActionbar(Message message, ComponentHolders holders) throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     * @return - Optional, представляющий результат успешности проведения процесса
     * операции отправки сообщения на панель действий.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    Optional<AudienceSendEvent> sendActionbar(String message) throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     * @return - Optional, представляющий результат успешности проведения процесса
     * операции отправки сообщения на панель действий.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    Optional<AudienceSendEvent> sendActionbar(String message, ComponentHolders holders) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title - Содержание заголовка.
     */
    Optional<AudienceSendEvent> sendComponentTitle(Title<Component> title) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title   - Содержание заголовка.
     * @param holders - переменные, меняющие текстовый образ сообщения.
     */
    Optional<AudienceSendEvent> sendComponentTitle(Title<Component> title, ComponentHolders holders) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title - Содержание заголовка.
     */
    Optional<AudienceSendEvent> sendTranslatedTitle(Title<Message> title) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title   - Содержание заголовка.
     * @param holders - переменные, меняющие текстовый образ сообщения.
     */
    Optional<AudienceSendEvent> sendTranslatedTitle(Title<Message> title, ComponentHolders holders) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title - Содержание заголовка.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    Optional<AudienceSendEvent> sendTitle(Title<String> title) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным
     * текстом и подзаголовком.
     *
     * @param title   - Содержание заголовка.
     * @param holders - переменные, меняющие текстовый образ сообщения.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    Optional<AudienceSendEvent> sendTitle(Title<String> title, ComponentHolders holders) throws RemoteException;
}
