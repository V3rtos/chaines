package me.moonways.bridgenet.api.proxy;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodHandler {

    Class<? extends Annotation>[] target() default Annotation.class;
}
