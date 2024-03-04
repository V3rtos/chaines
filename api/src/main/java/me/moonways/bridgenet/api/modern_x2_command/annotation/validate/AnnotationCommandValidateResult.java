package me.moonways.bridgenet.api.modern_x2_command.annotation.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;

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
        Type fail = Type.FAIL;
        fail.handle(sender, message);

        return new AnnotationCommandValidateResult(fail, message);
    }

    public static AnnotationCommandValidateResult fail() {
        return new AnnotationCommandValidateResult(Type.FAIL, null);
    }

    public enum Type {

        OK {
            @Override
            public void handle(EntityCommandSender sender, String message) {
                if (!message.isEmpty())
                    sender.sendMessage(message);
            }
        },
        FAIL {
            @Override
            public void handle(EntityCommandSender sender, String message) {
                if (!message.isEmpty())
                    sender.sendMessage(message);
            }
        };

        public abstract void handle(EntityCommandSender sender, String message);
    }
}
