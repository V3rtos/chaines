package me.moonways.bridgenet;

import lombok.Getter;
import me.moonways.bridgenet.command.sender.ConsoleSender;
import me.moonways.bridgenet.dependencyinjection.DependencyInjection;
import me.moonways.bridgenet.dependencyinjection.Inject;
import me.moonways.bridgenet.dependencyinjection.scanner.ResourceClasspathScanner;

public class BridgenetBootstrap {

    @Inject
    @Getter
    private BridgenetControl bridgenetControl;

    private void applyDependencyInjection() {
        DependencyInjection dependencyInjection = new DependencyInjection();
        dependencyInjection.scanDependencies("me.moonways", new ResourceClasspathScanner());

        dependencyInjection.addDepend(new BridgenetConsole(this));
        dependencyInjection.addDepend(new BridgenetControl());

        dependencyInjection.injectDependencies(this);
    }

    public void start() {
        applyDependencyInjection();

        bridgenetControl.init();
        registerInternalCommands();

        bridgenetControl.getConsole().start();
    }

    private void registerInternalCommands() {
        bridgenetControl.registerCommand(TestCommand.class);
    }

    public static void main(String[] args) {
        BridgenetBootstrap bootstrap = new BridgenetBootstrap();
        bootstrap.start();
    }
}
