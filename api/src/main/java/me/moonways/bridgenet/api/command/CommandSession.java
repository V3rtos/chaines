package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

@ToString
@RequiredArgsConstructor
public final class CommandSession {

    private final HelpMessageView helpMessageView;

    @Getter
    private final CommandDescriptor descriptor;
    private final CommandArguments arguments;

    @Getter
    private final EntityCommandSender sender;

    public <T extends EntityCommandSender> T getSender(Class<T> objectCast) {
        return objectCast.cast(sender);
    }

    public void printDefaultMessage(@NotNull String messageFormat) {
        helpMessageView.print(sender, messageFormat);
    }

    public CommandArguments arguments() {
        return arguments;
    }

    public static class HelpMessageView {

        private static final String NAME_FORMAT = "{0}";
        private static final String DESCRIPTION_FORMAT = "{1}";

        private final Map<String, String> producersDescriptionMap = new WeakHashMap<>();

        public void addDescription(@NotNull String name, @Nullable String usage, @Nullable String description) {
            if (description != null) {
                producersDescriptionMap.put(usage != null ? usage : name.toLowerCase(), description);
            }
        }

        public void print(@NotNull EntityCommandSender sender, @NotNull String format) {
            producersDescriptionMap.forEach((name, desc) -> {

                String formattedMessage = format.replace(NAME_FORMAT, name).replace(DESCRIPTION_FORMAT, desc);
                sender.sendMessage(formattedMessage);
            });
        }
    }
}
