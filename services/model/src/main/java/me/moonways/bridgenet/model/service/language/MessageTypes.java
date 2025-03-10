package me.moonways.bridgenet.model.service.language;

/**
 * Данный интерфейс реализует статические
 * константы основных языковых сообщений, которые
 * могут быть использованы внутри системы
 * или другими сервисами.
 */
public interface MessageTypes {

    /**
     * Сообщения, выделенные под группу `General`
     */
    Message UNKNOWN_MESSAGE_TYPE = Message.keyed("general.unknownMessageType");

    /**
     * Сообщения, выделенные под группу `Players`
     */
    Message GREETING_ON_JOIN = Message.keyed("players.greetingOnJoin");

    /**
     * Сообщения, выделенные под группу `Gui`
     */
    Message GUI_SESSION_EXPIRED = Message.keyed("gui.openedSessionExpired");
}
