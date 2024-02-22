package me.moonways.bridgenet.api.modern_x2_command.ai.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;

@Getter
@RequiredArgsConstructor
public class AICommandValidateResult {

    private final Type type;
    private final String message;

    public boolean isOk() {
        return type.equals(Type.OK);
    }

    public static AICommandValidateResult ok() {
        return new AICommandValidateResult(Type.OK, null);
    }

    public static AICommandValidateResult fail(EntityCommandSender sender, String message) {
        Type fail = Type.FAIL;
        fail.handle(sender, message);

        return new AICommandValidateResult(fail, message);
    }

    public static AICommandValidateResult fail() {
        return new AICommandValidateResult(Type.FAIL, null);
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
