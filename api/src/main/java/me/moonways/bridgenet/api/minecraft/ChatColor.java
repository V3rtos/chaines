package me.moonways.bridgenet.api.minecraft;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public enum ChatColor {

    BLACK('0', 0),
    DARK_BLUE('1', 1),
    DARK_GREEN('2', 2),
    DARK_AQUA('3', 3),
    DARK_RED('4', 4),
    DARK_PURPLE('5', 5),
    GOLD('6', 6),
    GRAY('7', 7),
    DARK_GRAY('8', 8),
    BLUE('9', 9),
    GREEN('a', 10),
    AQUA('b', 11),
    RED('c', 12),
    LIGHT_PURPLE('d', 13),
    YELLOW('e', 14),
    WHITE('f', 15),
    MAGIC('k', 16, true),
    BOLD('l', 17, true),
    STRIKETHROUGH('m', 18, true),
    UNDERLINE('n', 19, true),
    ITALIC('o', 20, true),
    RESET('r', 21);

    public static final char COLOR_CHAR = '§';
    private static final String COLOR_TAGS_FILTER = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("§x(?>§[0-9a-f]){6}", 2);
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-ORX]");

    private static final Map<Integer, ChatColor> BY_ID = new HashMap<>();
    private static final Map<Character, ChatColor> BY_CHAR = new HashMap<>();

    static {
        ChatColor[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            ChatColor color = var0[var2];
            BY_ID.put(color.intCode, color);
            BY_CHAR.put(color.code, color);
        }
    }

    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;

    ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    ChatColor(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{COLOR_CHAR, code});
    }

    public boolean isColor() {
        return !this.isFormat && this != RESET;
    }

    public static @Nullable ChatColor getByChar(char code) {
        return BY_CHAR.get(code);
    }

    public static @Nullable ChatColor getByChar(@NotNull String code) {
        return BY_CHAR.get(code.charAt(0));
    }

    @Nullable
    @Contract("!null -> !null; null -> null")
    public static String stripColor(@Nullable String input) {
        return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    @NotNull
    public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && COLOR_TAGS_FILTER.indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    @NotNull
    public static String getLastColors(@NotNull String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int index = length - 1; index > -1; --index) {
            char section = input.charAt(index);
            if (section == 167 && index < length - 1) {
                if (index > 11 && input.charAt(index - 12) == 167 && (input.charAt(index - 11) == 'x' || input.charAt(index - 11) == 'X')) {
                    String color = input.substring(index - 12, index + 2);
                    if (HEX_COLOR_PATTERN.matcher(color).matches()) {
                        result.insert(0, color);
                        break;
                    }
                }

                char c = input.charAt(index + 1);
                ChatColor color = getByChar(c);
                if (color != null) {
                    result.insert(0, color);
                    if (color.isColor() || color.equals(RESET)) {
                        break;
                    }
                }
            }
        }

        return result.toString();
    }

    @NotNull
    @Override
    public String toString() {
        return this.toString;
    }
}