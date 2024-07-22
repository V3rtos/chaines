package me.moonways.bridgenet.rest4j;

/**
 * Интерфейс Api предоставляет константы и методы для формирования URL-адресов различных сервисов API.
 * <p>
 * Этот интерфейс содержит константы для версий API и путей к различным сервисам, таким как друзья, игры, группы и другие.
 * Методы этого интерфейса позволяют легко строить полные пути к ресурсам API.
 * </p>
 *
 * <p>
 * Примеры использования:
 * </p>
 * <pre>{@code
 * // Получение пути к сервису друзей
 * String friendsUrl = Api.friendsPath("list");
 * // Результат: "/v1/friends/list"
 *
 * // Получение пути к сервису игр с дополнительными параметрами
 * String gamesUrl = Api.gamesPath("info", "123");
 * }</pre>
 */
public interface Api {

    /**
     * Версия API.
     */
    String VERSION_1 = "/v1";

    /**
     * Путь к сервису друзей.
     */
    String FRIENDS_SERVICE = "/friends";

    /**
     * Путь к сервису игр.
     */
    String GAMES_SERVICE = "/games";

    /**
     * Путь к сервису групп.
     */
    String PARTIES_SERVICE = "/parties";

    /**
     * Путь к сервису гильдий.
     */
    String GUILDS_SERVICE = "/guilds";

    /**
     * Путь к языковому сервису.
     */
    String LANGUAGE_SERVICE = "/language";

    /**
     * Путь к сервису прав доступа.
     */
    String PERMISSIONS_SERVICE = "/permissions";

    /**
     * Путь к сервису жалоб.
     */
    String REPORTS_SERVICE = "/reports";

    /**
     * Путь к сервису игроков.
     */
    String PLAYERS_SERVICE = "/players";

    /**
     * Путь к серверному сервису.
     */
    String SERVERS_SERVICE = "/servers";

    /**
     * Формирует полный путь к любому сервису API.
     *
     * @param service имя сервиса
     * @param paths   дополнительные пути
     * @return полный путь к ресурсу API
     */
    static String anyServicePath(String service, String... paths) {
        return String.format("%s%s/%s", VERSION_1, service, String.join("/", paths));
    }

    /**
     * Формирует полный путь к сервису друзей.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса друзей
     */
    static String friendsPath(String... paths) {
        return anyServicePath(FRIENDS_SERVICE, paths);
    }

    /**
     * Формирует полный путь к сервису игр.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса игр
     */
    static String gamesPath(String... paths) {
        return anyServicePath(GAMES_SERVICE, paths);
    }

    /**
     * Формирует полный путь к сервису групп.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса групп
     */
    static String partiesPath(String... paths) {
        return anyServicePath(PARTIES_SERVICE, paths);
    }

    /**
     * Формирует полный путь к сервису гильдий.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса гильдий
     */
    static String guildsPath(String... paths) {
        return anyServicePath(GUILDS_SERVICE, paths);
    }

    /**
     * Формирует полный путь к языковому сервису.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу языкового сервиса
     */
    static String languagePath(String... paths) {
        return anyServicePath(LANGUAGE_SERVICE, paths);
    }

    /**
     * Формирует полный путь к сервису прав доступа.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса прав доступа
     */
    static String permissionsPath(String... paths) {
        return anyServicePath(PERMISSIONS_SERVICE, paths);
    }

    /**
     * Формирует полный путь к сервису жалоб.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса отчетов
     */
    static String reportsPath(String... paths) {
        return anyServicePath(REPORTS_SERVICE, paths);
    }

    /**
     * Формирует полный путь к сервису игроков.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу сервиса игроков
     */
    static String playersPath(String... paths) {
        return anyServicePath(PLAYERS_SERVICE, paths);
    }

    /**
     * Формирует полный путь к серверному сервису.
     *
     * @param paths дополнительные пути
     * @return полный путь к ресурсу серверного сервиса
     */
    static String serversPath(String... paths) {
        return anyServicePath(SERVERS_SERVICE, paths);
    }
}
