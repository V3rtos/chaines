package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.modern_command.StandardCommandInfo;

import java.lang.annotation.Annotation;

public interface RegistrationCommandAnnotation<A extends Annotation, C extends StandardCommandInfo> {

    /**
     * Регистрация аннотации.
     *
     * @param annotation - аннотация.
     * @param commandInfo - тип команды, для которой регистрируем аннотацию.
     */
    void register(A annotation, C commandInfo);
}
