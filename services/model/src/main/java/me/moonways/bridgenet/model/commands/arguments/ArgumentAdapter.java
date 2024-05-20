package me.moonways.bridgenet.model.commands.arguments;

public interface ArgumentAdapter {

    int index();

    String name();


    String asString();

    Integer asInt();

    Long asLong();

    Double asDouble();

    Boolean asBoolean();
}
