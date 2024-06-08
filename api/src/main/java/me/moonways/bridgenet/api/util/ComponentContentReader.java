package me.moonways.bridgenet.api.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;


@UtilityClass
public class ComponentContentReader {
    
    private static final String STRING_CLOSE_SIGN = "\"";
    private static final GsonComponentSerializer SERIALIZER = GsonComponentSerializer.gson();;

    public String read(Component component) {
        String string = SERIALIZER.serialize(component);

        if (string.startsWith(STRING_CLOSE_SIGN) && string.endsWith(STRING_CLOSE_SIGN)) {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }
}
