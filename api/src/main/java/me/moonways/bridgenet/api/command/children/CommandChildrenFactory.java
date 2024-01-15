package me.moonways.bridgenet.api.command.children;

import me.moonways.bridgenet.api.command.children.definition.MentorChild;
import me.moonways.bridgenet.api.command.children.definition.PredicateChild;
import me.moonways.bridgenet.api.command.children.definition.ProducerChild;

import java.lang.reflect.Method;

class CommandChildrenFactory {

    public CommandChild createMentor(Object sourceObject, Method method) {
        return new MentorChild(sourceObject, method);
    }

    public CommandChild createProducer(Object sourceObject, Method method, String name, String permission, String usage, String description) {
        return new ProducerChild(sourceObject, method, name, permission, usage, description);
    }

    public CommandChild createPredicate(Object sourceObject, Method method) {
        return new PredicateChild(sourceObject, method);
    }
}
