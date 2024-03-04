package me.moonways.bridgenet.api.modern_x2_command.obj.pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PatternFormat {

    LATIN_ONLY("[a-zA-Z]+"), // Строка содержит только латинские символы
    UPPER_CASE("[A-Z]+"),     // Строка содержит только заглавные буквы
    LOWER_CASE("[a-z]+"),     // Строка содержит только строчные буквы
    NUMERIC("[0-9]+"),        // Строка содержит только цифры
    ALPHANUMERIC("[a-zA-Z0-9]+"), // Строка содержит только буквы и цифры
    EMAIL("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b"), // Строка представляет email адрес
    PHONE_NUMBER("\\+?[0-9\\-]+"), // Строка представляет номер телефона
    URL("(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"), // Строка представляет URL
    DATE("\\d{4}-\\d{2}-\\d{2}"), // Строка представляет дату в формате YYYY-MM-DD
    TIME("\\d{2}:\\d{2}:\\d{2}"), // Строка представляет время в формате HH:MM:SS
    HEX_COLOR("#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})"), // Строка представляет цвет в формате HEX
    UUID("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"), // Строка представляет UUID
    IP_ADDRESS("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b"), // Строка представляет IP-адрес
    HTML_TAG("<(\"[^\"]*\"|'[^']*'|[^'\">])*>"), // Строка представляет HTML-тег
    MAC_ADDRESS("([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})"), // Строка представляет MAC-адрес
    FILE_PATH("^(?:[a-zA-Z]\\:|\\\\)\\\\([^\\\\]+\\\\)*[^\\/:*?\"<>|]+\\.\\w+$"), // Строка представляет путь к файлу
    HASHTAG("(?:\\s|^)#[\\p{L}0-9_]*\\b"), // Строка содержит хэштег
    USERNAME("[a-zA-Z0-9_]{3,16}"),
    EMPTY(""); // Строка представляет имя пользователя

    private final String value;

    public static String getPattern(PatternFormat pattern) {
        return pattern.getValue();
    }
}
