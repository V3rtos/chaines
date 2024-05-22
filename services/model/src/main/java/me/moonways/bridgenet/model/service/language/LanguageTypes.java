package me.moonways.bridgenet.model.service.language;

import java.util.Locale;

/**
 * Данный интерфейс реализует статические
 * константы основных языковых типов, которые
 * могут быть использованы внутри системы
 * или другими сервисами.
 */
public interface LanguageTypes {

    /**
     * Messages File: /etc/lang/de.ini
     */
    Language GERMANY = Language.fromLocale(Locale.GERMANY);

    /**
     * Messages File: /etc/lang/en.ini
     */
    Language ENGLISH = Language.fromLocale(Locale.ENGLISH);

    /**
     * Messages File: /etc/lang/fr.ini
     */
    Language FRENCH = Language.fromLocale(Locale.FRENCH);

    /**
     * Messages File: /etc/lang/it.ini
     */
    Language ITALY = Language.fromLocale(Locale.ITALY);

    /**
     * Messages File: /etc/lang/ja.ini
     */
    Language JAPAN = Language.fromLocale(Locale.JAPAN);

    /**
     * Messages File:: /etc/lang/ru.ini
     */
    Language RUSSIAN = Language.fromName("ru");

    /**
     * Messages File: /etc/lang/zh.ini
     */
    Language CHINA = Language.fromLocale(Locale.SIMPLIFIED_CHINESE);

    /**
     * Ordered list of all groups
     */
    Language[] TYPES =
            {
                    GERMANY,
                    ENGLISH,
                    FRENCH,
                    ITALY,
                    JAPAN,
                    RUSSIAN,
                    CHINA,
            };
}
