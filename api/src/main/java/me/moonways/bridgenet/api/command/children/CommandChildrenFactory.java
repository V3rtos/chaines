package me.moonways.bridgenet.api.command.children;

import me.moonways.bridgenet.api.command.children.definition.CommandMatchingChild;
import me.moonways.bridgenet.api.command.children.definition.CommandMentorChild;
import me.moonways.bridgenet.api.command.children.definition.CommandProducerChild;

import java.lang.reflect.Method;
import java.util.List;

class CommandChildrenFactory {

    public CommandChild createMentor(Object sourceObject, Method method) {
        return new CommandMentorChild(sourceObject, method);
    }

    public CommandChild createProducer(Object sourceObject, Method method, String name, List<String> aliases, String permission, String usage, String description) {
        return new CommandProducerChild(sourceObject, method, aliases, name, permission, usage, description);
    }

    public CommandChild createPredicate(Object sourceObject, Method method) {
        return new CommandMatchingChild(sourceObject, method);
    }
}
