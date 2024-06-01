package me.moonways.bridgenet.client.api.cloudnet;

import java.net.InetSocketAddress;

/**
 * Интерфейс CloudNetWrapper предоставляет методы для получения информации о
 * текущей задаче и сервисе в облачной сетевой среде.
 */
public interface CloudNetWrapper {

    /**
     * @return ID текущей задачи в виде целого числа.
     */
    int getCurrentTaskId();

    /**
     * @return имя текущей задачи в виде строки.
     */
    String getCurrentTaskName();

    /**
     * Возвращает полное имя текущего сервиса, которое может включать
     * дополнительные детали об экземпляре сервиса.
     */
    String getCurrentFullName();

    /**
     * @return сокетный адрес текущего сервиса в виде объекта {@link InetSocketAddress}.
     */
    InetSocketAddress getCurrentAddress();

    /**
     * @return порт текущего snapshot в виде целого числа.
     */
    int getCurrentSnapshotPort();

    /**
     * @return хост текущего snapshot в виде строки.
     */
    String getCurrentSnapshotHost();
}
