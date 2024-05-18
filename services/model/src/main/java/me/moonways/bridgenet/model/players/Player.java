package me.moonways.bridgenet.model.players;

import me.moonways.bridgenet.model.language.Message;
import me.moonways.bridgenet.model.players.service.PlayerConnection;
import me.moonways.bridgenet.model.players.service.statistic.ActivityStatistics;
import me.moonways.bridgenet.model.util.TitleFade;
import me.moonways.bridgenet.model.util.audience.event.AudienceSendEvent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;

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
     *
     * @return - CompletableFuture, представляющий асинхронный результат
     * операции отправки сообщения на панель действий.
     */
    CompletableFuture<AudienceSendEvent> sendActionbar(Component message) throws RemoteException;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     *
     * @return - CompletableFuture, представляющий асинхронный результат
     * операции отправки сообщения на панель действий.
     */
    CompletableFuture<AudienceSendEvent> sendActionbar(Message message) throws RemoteException;;

    /**
     * Отправляет сообщение на панель действий игрока с указанным компонентом сообщения.
     *
     * @param message - Компонент сообщения, который будет отправлен на панель действий.
     *
     * @return - CompletableFuture, представляющий асинхронный результат
     * операции отправки сообщения на панель действий.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    CompletableFuture<AudienceSendEvent> sendActionbar(String message) throws RemoteException;;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title - Текст заголовка.
     * @param subtitle - Текст подзаголовка.
     * @param fade - Эффекты затухания заголовка.
     */
    CompletableFuture<AudienceSendEvent> sendTitle(Component title, Component subtitle, TitleFade fade) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным
     * текстом и подзаголовком.
     *
     * @param title - Текст заголовка.
     * @param subtitle - Текст подзаголовка.
     */
    CompletableFuture<AudienceSendEvent> sendTitle(Component title, Component subtitle) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом.
     * @param title - Текст заголовка.
     */
    CompletableFuture<AudienceSendEvent> sendTitle(Component title) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title - Текст заголовка.
     * @param subtitle - Текст подзаголовка.
     * @param fade - Эффекты затухания заголовка.
     */
    CompletableFuture<AudienceSendEvent> sendTitle(Message title, Message subtitle, TitleFade fade) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным
     * текстом и подзаголовком.
     *
     * @param title - Текст заголовка.
     * @param subtitle - Текст подзаголовка.
     */
    CompletableFuture<AudienceSendEvent> sendTitle(Message title, Message subtitle) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом.
     * @param title - Текст заголовка.
     */
    CompletableFuture<AudienceSendEvent> sendTitle(Message title) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом,
     * подзаголовком и эффектами затухания.
     *
     * @param title - Текст заголовка.
     * @param subtitle - Текст подзаголовка.
     * @param fade - Эффекты затухания заголовка.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    CompletableFuture<AudienceSendEvent> sendTitle(String title, String subtitle, TitleFade fade) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным
     * текстом и подзаголовком.
     *
     * @param title - Текст заголовка.
     * @param subtitle - Текст подзаголовка.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    CompletableFuture<AudienceSendEvent> sendTitle(String title, String subtitle) throws RemoteException;

    /**
     * Отправить заголовок на экран игрока с указанным текстом.
     * @param title - Текст заголовка.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    CompletableFuture<AudienceSendEvent> sendTitle(String title) throws RemoteException;
}
