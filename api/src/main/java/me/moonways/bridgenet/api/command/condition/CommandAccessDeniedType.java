package me.moonways.bridgenet.api.command.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommandAccessDeniedType {

    DO_NOT_HAVE_PERMISSION("У вас недостаточно прав!");

    private final String errorMessage;
}
