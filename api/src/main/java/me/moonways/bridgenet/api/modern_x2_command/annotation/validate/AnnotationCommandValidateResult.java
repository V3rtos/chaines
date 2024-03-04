package me.moonways.bridgenet.api.modern_x2_command.annotation.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.EntityCommandSender;

@Getter
@RequiredArgsConstructor
public class AnnotationCommandValidateResult {

    private final Type type;
    private final String message;

    public boolean isOk() {
        return type.equals(Type.OK);
    }

    public static AnnotationCommandValidateResult ok() {
        return new AnnotationCommandValidateResult(Type.OK, null);
    }

    public static AnnotationCommandValidateResult fail(EntityCommandSender sender, String message) {
        sender.sendMessage(message);

        return new AnnotationCommandValidateResult(Type.FAIL, message);
    }

    public static AnnotationCommandValidateResult fail() {
        return new AnnotationCommandValidateResult(Type.FAIL, null);
    }

    public enum Type {

        OK,
        FAIL
    }
}
