package me.moonways.bridgenet.model.service.language;

import me.moonways.bridgenet.model.event.PlayerLanguageUpdateEvent;
import me.moonways.bridgenet.rmi.service.RemoteService;
import net.kyori.adventure.text.Component;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public interface LanguageServiceModel extends RemoteService {

    /**
     * Получить уже ранее зарегистрированный тип мирового языка.
     *
     * @param id - идентификатор языка.
     */
    Optional<Language> getLang(UUID id) throws RemoteException;

    /**
     * Получить уже ранее зарегистрированный тип мирового языка.
     *
     * @param name - код языка.
     */
    Optional<Language> getLang(String name) throws RemoteException;

    /**
     * Получить уже ранее зарегистрированный тип мирового языка.
     *
     * @param locale - тип языка через константу Locale.
     */
    Optional<Language> getLang(Locale locale) throws RemoteException;

    /**
     * Получить сообщение, написанное на указанном
     * типе мирового языка.
     *
     * @param language - тип мирового языка.
     * @param key      - ключ к сообщению в базе языка.
     */
    Component message(Language language, String key) throws RemoteException;

    /**
     * Получить сообщение, написанное на указанном
     * типе мирового языка.
     *
     * @param language - тип мирового языка.
     * @param message  - ключ к сообщению в базе языка.
     */
    Component message(Language language, Message message) throws RemoteException;

    /**
     * Получить сообщение в виде текса, написанное
     * на указанном типе мирового языка.
     *
     * @param language - тип мирового языка.
     * @param key      - ключ к сообщению в базе языка.
     */
    String messageText(Language language, String key) throws RemoteException;

    /**
     * Получить сообщение в виде текса, написанное
     * на указанном типе мирового языка.
     *
     * @param language - тип мирового языка.
     * @param message  - ключ к сообщению в базе языка.
     */
    String messageText(Language language, Message message) throws RemoteException;

    /**
     * @return - Стандартный тип мирового языка, возвращаемый в случае, если язык не был найден.
     */
    Language getDefault() throws RemoteException;

    /**
     * Получить уже ранее зарегистрированный тип мирового языка,
     * который использует определенный пользователь.
     *
     * @param playerId - идентификатор игрока.
     */
    Language getPlayerLang(UUID playerId) throws RemoteException;

    /**
     * Получить уже ранее зарегистрированный тип мирового языка,
     * который использует определенный пользователь.
     *
     * @param playerName - имя игрока.
     */
    Language getPlayerLang(String playerName) throws RemoteException;

    /**
     * Установить индивидуальный тип мирового языка
     * определенному пользователю.
     *
     * @param playerId - идентификатор пользователя.
     * @param language - устанавливаемый тип мирового языка.
     */
    Optional<PlayerLanguageUpdateEvent> setPlayerLang(UUID playerId, Language language) throws RemoteException;

    /**
     * Установить индивидуальный тип мирового языка
     * определенному пользователю.
     *
     * @param playerName - имя пользователя.
     * @param language   - устанавливаемый тип мирового языка.
     */
    Optional<PlayerLanguageUpdateEvent> setPlayerLang(String playerName, Language language) throws RemoteException;
}
