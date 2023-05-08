package me.moonways.bridgenet;

import lombok.Getter;
import me.moonways.bridgenet.command.CommandRegistrationService;

public class BridgenetBootstrap {

    private final BridgenetConsole console = new BridgenetConsole(this);

    @Getter
    private final CommandRegistrationService commandRegistrationService = new CommandRegistrationService();


    public void start() {
        commandRegistrationService.register(TestCommand.class);

        console.start();
    }

    public static void main(String[] args) {
        BridgenetBootstrap bootstrap = new BridgenetBootstrap();
        bootstrap.start();
    }
}
