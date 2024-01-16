package me.moonways.bridgenet.api.modern_command.annotation;

import me.moonways.bridgenet.api.modern_command.StandardCommandInfo;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;

import java.lang.annotation.Annotation;

public interface VerificationCommandAnnotation<A extends Annotation, C extends StandardCommandInfo> {

    /**
     * Верификация аннотации перед использованием команды.
     *
     * @param annotation - аннотация.
     * @param commandInfo - информация о команде.
     * @param entity - сущность.
     * @return - true/false
     */
    boolean verify(A annotation, C commandInfo, CommandEntity entity);
}
