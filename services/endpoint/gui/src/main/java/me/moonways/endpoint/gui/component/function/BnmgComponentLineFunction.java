package me.moonways.endpoint.gui.component.function;

import me.moonways.endpoint.gui.component.BnmgComponentLine;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BnmgComponentLineFunction<T> {

    @NotNull
    T callFunction(@NotNull BnmgComponentLine line, @NotNull String signature);
}
