package me.moonways.bridgenet;

import lombok.Getter;
import me.moonways.bridgenet.api.BridgenetControl;
import me.moonways.bridgenet.api.TestCommand;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import net.conveno.jdbc.ConvenoRouter;

public class BridgenetBootstrap {

    @Inject
    @Getter
    private BridgenetControl bridgenetControl;

    @Inject
    private BridgenetConsole bridgenetConsole;

    private void applyDependencyInjection() {
        DependencyInjection dependencyInjection = new DependencyInjection();

        // local system services.
        dependencyInjection.addDepend(dependencyInjection);
        dependencyInjection.addDepend(new BridgenetConsole(this));

        // dependencies services.
        dependencyInjection.addDepend(ConvenoRouter.create());

        // inject
        dependencyInjection.scanDependencies("me.moonways");
        dependencyInjection.injectDependencies(this);
    }

    public void start() {
        applyDependencyInjection();
        registerInternalCommands();

        bridgenetConsole.start();
    }

    private void registerInternalCommands() {
        bridgenetControl.registerCommand(TestCommand.class);
    }

    public static void main(String[] args) {
        BridgenetBootstrap bootstrap = new BridgenetBootstrap();
        bootstrap.start();
    }
}
