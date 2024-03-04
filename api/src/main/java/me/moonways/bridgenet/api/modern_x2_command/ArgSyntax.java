package me.moonways.bridgenet.api.modern_x2_command;

import java.lang.annotation.*;

@Repeatable(ArgSyntaxes.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ArgSyntax {

    int position();

    Pattern pattern() default @Pattern;
}
