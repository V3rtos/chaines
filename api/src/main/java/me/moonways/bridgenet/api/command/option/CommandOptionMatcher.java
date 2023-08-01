package me.moonways.bridgenet.api.command.option;

import me.moonways.bridgenet.api.command.CommandSession;
import org.jetbrains.annotations.NotNull;

public interface CommandOptionMatcher {

    boolean matches(@NotNull CommandSession session);

    void apply(@NotNull CommandSession session);
}
