package me.moonways.bridgenet.model.service.gui.item.entries;

/**
 * Интерфейс ItemsEntryable<T extends ItemsEntry> представляет объект,
 * который хранит экземпляр объекта, реализующего интерфейс ItemsEntry.
 * Он используется для управления объектами, используемыми
 * в парсинге JSON-объектов из конфигурационных файлов в папке /etc/minecraft_data/.
 *
 * @param <T> Тип объекта, реализующего интерфейс ItemsEntry.
 */
public interface ItemsEntryable<T extends ItemsEntry> {

    /**
     * @return - Наименование объекта.
     */
    String namespace();

    /**
     * @return - Экземпляр объекта, реализующего интерфейс ItemsEntry.
     */
    T entry();

    /**
     * Установить экземпляр объекта, реализующего интерфейс ItemsEntry.
     *
     * @param entry - Экземпляр объекта, реализующего интерфейс ItemsEntry.
     */
    void set(T entry);
}
