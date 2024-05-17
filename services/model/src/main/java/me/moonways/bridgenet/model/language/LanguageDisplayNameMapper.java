package me.moonways.bridgenet.model.language;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.PostConstruct;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Autobind
public final class LanguageDisplayNameMapper {

    private final Map<Language, String> displayNamesMap = new HashMap<>();

    @PostConstruct
    private void init() throws IllegalAccessException {
        Field[] fields = LanguageTypes.class.getDeclaredFields();

        for (Field languageField : fields) {
            if (languageField.getType().equals(Language.class)) {
                displayNamesMap.put((Language) languageField.get(null), normalizeName(languageField.getName()));
            }
        }
    }

    private String normalizeName(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public String getDisplayName(Language language) {
        return displayNamesMap.get(language);
    }
}
