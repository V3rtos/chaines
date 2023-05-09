package me.moonways.bnmg.component.function;

import me.moonways.bnmg.component.BnmgComponentLine;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BnmgComponentLineFunction<T> {

    @NotNull
    T callFunction(@NotNull BnmgComponentLine line, @NotNull String signature);
}
