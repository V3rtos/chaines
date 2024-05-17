package me.moonways.bridgenet.model.gui.click;

/**
 * Интерфейс ItemClickListener представляет собой слушатель событий кликов по элементам GUI в Minecraft.
 * Этот интерфейс определяет метод consume(), который вызывается при возникновении события клика.
 */
public interface ItemClickListener {

    /**
     * Обработать событие клика по элементу GUI.
     * @param event Событие клика, которое произошло.
     */
    void consume(ItemClickEvent event);
}
