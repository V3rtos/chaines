package me.moonways.bridgenet.mtp.message.persistence;

import me.moonways.bridgenet.api.inject.processor.persistence.UseTypeAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Данная аннотация помечает определенное сообщение,
 * как объект, который в последствии должен будет передаваться
 * от имени СЕРВЕРА к КЛИЕНТУ, то есть основой для его регистрации
 * будет являться именно СЕРВЕРЫЙ канал.
 */
@UseTypeAnnotationProcessor
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerMessage {
}
