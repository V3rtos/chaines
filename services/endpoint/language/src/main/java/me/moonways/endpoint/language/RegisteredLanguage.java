package me.moonways.endpoint.language;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.assembly.ini.IniConfig;
import me.moonways.bridgenet.model.language.Language;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class RegisteredLanguage {

    private final Language language;
    private final IniConfig messagesConfig;
}
