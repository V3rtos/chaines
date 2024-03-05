package me.moonways.bridgenet.api.modern_x2_command.objects.regex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommandRegex {

    private final String value;
    private final String exceptionMsg;
}
