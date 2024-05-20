package me.moonways.bridgenet.jdbc.entity.modern.persistence.multi;

import me.moonways.bridgenet.jdbc.entity.modern.persistence.Signature;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SignatureArguments {

    Signature[] value();
}
