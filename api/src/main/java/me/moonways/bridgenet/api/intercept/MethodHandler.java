package me.moonways.bridgenet.api.intercept;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodHandler {

    Class<? extends Annotation>[] target() default Annotation.class;
}
