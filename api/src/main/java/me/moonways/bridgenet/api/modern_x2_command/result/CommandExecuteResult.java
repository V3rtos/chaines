package me.moonways.bridgenet.api.modern_x2_command.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public final class CommandExecuteResult {

    private final Type type;
    private final String value;

    public boolean isOk() {
        return type.equals(Type.OK);
    }

    public boolean isFail() {
        return type.equals(Type.FAIL);
    }

    public void test() {

    }

    public boolean isEmpty() {
        return type.equals(Type.EMPTY);
    }

    public static CommandExecuteResult ok(EntityCommandSender sender, Consumer<EntityCommandSender> senderConsumer) {
        senderConsumer.accept(sender);

        return new CommandExecuteResult(Type.OK, null);
    }

    public static CommandExecuteResult ok(EntityCommandSender sender, String reason) {
        sender.sendMessage(reason);

        return new CommandExecuteResult(Type.OK, reason);
    }

    public static CommandExecuteResult ok(EntityCommandSender sender, CommandExecuteResultMsg msg) {
        sender.sendMessage(msg.getValue());

        return new CommandExecuteResult(Type.OK, msg.getValue());
    }

    public static CommandExecuteResult ok() {
        return new CommandExecuteResult(Type.OK, null);
    }

    public static CommandExecuteResult fail(EntityCommandSender sender, Consumer<EntityCommandSender> senderConsumer, String reason) {
        senderConsumer.accept(sender);

        return new CommandExecuteResult(Type.FAIL, reason);
    }

    public static CommandExecuteResult fail(EntityCommandSender sender, String reason) {
        sender.sendMessage(reason);

        return new CommandExecuteResult(Type.FAIL, reason);
    }

    public static CommandExecuteResult fail(EntityCommandSender sender, CommandExecuteResultMsg reason) {
        sender.sendMessage(reason.getValue());

        return new CommandExecuteResult(Type.FAIL, reason.getValue());
    }

    public static CommandExecuteResult empty() {
        return new CommandExecuteResult(Type.EMPTY, "void method");
    }

    public enum Type {
        OK,
        FAIL,
        EMPTY
    }
}
