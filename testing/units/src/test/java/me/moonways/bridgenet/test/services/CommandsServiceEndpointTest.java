package me.moonways.bridgenet.test.services;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.commands.*;
import me.moonways.bridgenet.model.commands.arguments.ArgumentAdapter;
import me.moonways.bridgenet.model.commands.arguments.ArgumentsContext;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Arrays;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class CommandsServiceEndpointTest {

    @Inject
    private CommandsServiceModel serviceModel;

    @Test
    public void test_registerCommand() throws RemoteException {
        CommandDescription commandDescription =
                CommandDescription.builder()
                        .name("print")
                        .usage("/print <value-to-print>")
                        .aliases(Arrays.asList("println", "sout", "log", "debug"))
                        .description("Print a value from [1] argument")
                        .build();

        serviceModel.register(
                new BridgenetCommand(commandDescription) {

                    @Override
                    public void prepare(ArgumentsContext argumentsContext) {
                        argumentsContext.setName(0, "value");
                    }

                    @Override
                    public CommandResult run(CommandSessionContext context) {
                        ArgumentsContext arguments = context.getArguments();
                        String value = arguments.get("value").map(ArgumentAdapter::asString).orElse(null);

                        log.debug(value);

                        return (value != null ? CONFIRMED : FAILED);
                    }
                }
        );

        serviceModel.setCommandExecutor((command, reader) -> {

            String label = reader.label();
            ArgumentsContext argumentsContext = reader.readArgumentsToContext();

            return command.run(new CommandSessionContext() {
                @Override
                public ArgumentsContext getArguments() {
                    return argumentsContext;
                }

                @Override
                public String getLabel() {
                    return label;
                }
            });
        });
    }
}
