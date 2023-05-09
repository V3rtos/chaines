package me.moonways.bridgenet;

import lombok.Getter;
import me.moonways.bridgenet.dependencyinjection.DependencyInjection;
import me.moonways.bridgenet.dependencyinjection.Inject;
import me.moonways.bridgenet.dependencyinjection.scanner.ResourceClasspathScanner;
import net.conveno.jdbc.ConvenoRouter;

public class BridgenetBootstrap {

    @Inject
    @Getter
    private BridgenetControl bridgenetControl;

    private void applyDependencyInjection() {
        DependencyInjection dependencyInjection = new DependencyInjection();
        dependencyInjection.scanDependencies("me.moonways", new ResourceClasspathScanner());

        // local system services.
        dependencyInjection.addDepend(new BridgenetConsole(this));
        dependencyInjection.addDepend(new BridgenetControl());

        // dependencies services.
        dependencyInjection.addDepend(ConvenoRouter.create());

        // inject
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
