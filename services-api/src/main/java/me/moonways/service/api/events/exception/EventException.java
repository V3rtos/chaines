package me.moonways.service.api.events.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventException extends RuntimeException {

    private static final long serialVersionUID = -244215571470405751L;

    private static String formatMessage(String message, Object[] parameters) {
        String formattedMessage = String.format(message, parameters);

        for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
            String placeholder = String.format("{%d}", paramIndex);
            formattedMessage = formattedMessage.replace(placeholder, parameters[paramIndex].toString());
        }

        return formattedMessage;
    }

    public EventException(@NotNull String message, Object... parameters) {
        this(null, message, parameters);
    }

    public EventException(@Nullable Throwable cause, @NotNull String message, Object... parameters) {
        super(formatMessage(message, parameters), cause);
    }
}
