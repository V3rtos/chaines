package me.moonways.bridgenet.jdbc.core.compose;

public interface Tabled<I extends TemplatedQuery> {

    I container(String table);
}
