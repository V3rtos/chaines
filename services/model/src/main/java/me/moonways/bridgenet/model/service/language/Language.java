package me.moonways.bridgenet.model.service.language;

import lombok.*;

import java.util.Locale;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Language {

    public static Language fromName(String name) {
        return new Language(UUID.nameUUIDFromBytes(name.getBytes()), name);
    }

    public static Language fromLocale(Locale locale) {
        return fromName(locale.getLanguage());
    }

    @EqualsAndHashCode.Include
    private final UUID id;
    private final String name;

    public Locale toLocale() {
        return Locale.forLanguageTag(name);
    }
}
