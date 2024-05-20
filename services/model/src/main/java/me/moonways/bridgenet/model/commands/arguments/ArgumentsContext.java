package me.moonways.bridgenet.model.commands.arguments;

import java.util.Optional;

public interface ArgumentsContext {

    int count();

    boolean has(int index);

    void setName(int index, String name);

    Optional<ArgumentAdapter> get(String name);

    Optional<String> getString(String name);
}
