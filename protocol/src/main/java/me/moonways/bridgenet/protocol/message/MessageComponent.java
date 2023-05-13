package me.moonways.bridgenet.protocol.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageComponent {

    ProtocolDirection direction();

    MessageState state() default MessageState.EMPTY;
}
