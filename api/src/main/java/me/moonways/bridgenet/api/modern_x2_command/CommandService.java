package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandExecuteEvent;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandNotFoundEvent;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class CommandService {

    @Inject
    private EventService eventService;

    private CommandSearchStrategy searchStrategy;
    private AICommandValidateManagement validateManagement;

    public synchronized void handle(EntityCommandSender entity, String label) {
        Optional<Command> command = searchStrategy.search(label);

        if (command.isPresent()) {
            handleOk(entity, label, command.get());
        } else {
            handleFail(entity);
        }
    }

    public void handleAsync(EntityCommandSender entity, String label) {

    }

    private synchronized void handleOk(EntityCommandSender entity, String label, Command command) {
        ExecutionContext executionContext = ExecutionContext.create(entity, CommandLabelContext.create(label), command.getInfo());

        if (!validateEvent(executionContext)) return;

        AICommandValidateResult primaryResult = validateManagement.validate(executionContext, command);
        if (primaryResult.isOk()) {
            execute(executionContext, command);
        }
    }

    private synchronized void handleFail(EntityCommandSender entity) {
        CommandNotFoundEvent commandNotFoundEvent = eventService
                .fireEvent(new CommandNotFoundEvent())
                .getFollower()
                .getCompleted();

        if (commandNotFoundEvent.getMessage() != null) {
            entity.sendMessage(commandNotFoundEvent.getMessage());
        }
    }

    private synchronized boolean validateEvent(ExecutionContext executionContext) {
        CommandExecuteEvent executeEvent = eventService
                .fireEvent(new CommandExecuteEvent(executionContext))
                .getFollower()
                .getCompleted();

        return !executeEvent.isCancelled();
    }

    private synchronized void execute(ExecutionContext executionContext, Command command) {
        Object parent = command.getParent();
        Method method = command.getHandle();

        try {
            method.setAccessible(true);
            method.invoke(parent, executionContext);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
