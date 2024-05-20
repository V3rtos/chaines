package me.moonways.bridgenet.jdbc.entity.modern.persistence;

import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;
import me.moonways.bridgenet.jdbc.entity.modern.persistence.multi.SignatureArguments;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(SignatureArguments.class)
public @interface Signature {

    ParameterSignature value();
}
