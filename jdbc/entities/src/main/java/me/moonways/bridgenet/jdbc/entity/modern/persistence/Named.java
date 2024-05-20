package me.moonways.bridgenet.jdbc.entity.modern.persistence;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Named {

    String value();
}
