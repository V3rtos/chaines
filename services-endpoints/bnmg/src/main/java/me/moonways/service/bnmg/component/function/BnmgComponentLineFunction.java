package me.moonways.service.bnmg.component.function;

import me.moonways.service.bnmg.component.BnmgComponentLine;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BnmgComponentLineFunction<T> {

    @NotNull
    T callFunction(@NotNull BnmgComponentLine line, @NotNull String signature);
}
