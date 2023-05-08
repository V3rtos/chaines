package me.moonways.bridgenet.scheduler;

public class ScheduleException extends RuntimeException {

    private static String formatMessage(String message, Object[] parameters) {
        String formattedMessage = String.format(message, parameters);

        for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
            String placeholder = String.format("{%d}", paramIndex);
            formattedMessage = formattedMessage.replace(placeholder, parameters[paramIndex].toString());
        }

        return formattedMessage;
    }

    public ScheduleException(String message, Object... parameters) {
        super(formatMessage(message, parameters));
    }

    public ScheduleException(Throwable cause, String message, Object... parameters) {
        super(formatMessage(message, parameters), cause);
    }
}
