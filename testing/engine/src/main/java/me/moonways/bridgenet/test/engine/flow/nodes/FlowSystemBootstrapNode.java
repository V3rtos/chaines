package me.moonways.bridgenet.test.engine.flow.nodes;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookContainer;
import me.moonways.bridgenet.test.engine.TestingObject;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;

@Log4j2
public class FlowSystemBootstrapNode implements TestFlowNode {

    private static final AppBootstrap bootstrap = new AppBootstrap();
    // todo - мерджить предыдущий тестовый контекст

    @Override
    public void execute(TestFlowContext context) {
        if (bootstrap.isRunning()) {
            System.out.println("##################### [Test-Engine] System emulation is already started, skipping. [Test-Engine] #####################");
            return;
        }

        bootstrap.setRunning(true);
        bootstrap.runSystems();

        fakeStart(context);
        bootstrap.startBeansActivity(false);

        context.setInstance(TestFlowContext.BOOTSTRAP, bootstrap);
        context.setInstance(TestFlowContext.BEANS, bootstrap.getBeansService());
    }

    private void fakeStart(TestFlowContext context) {
        TestingObject testingObject = context.getTestingObject();

        BeansService beansService = bootstrap.getBeansService();
        BootstrapHookContainer hooksContainer = bootstrap.getHooksContainer();

        log.debug("Running fake starting process: {}", context);
        beansService.fakeStart();

        // Create a testing-object instance after fake beans starting.
        Object instance = testingObject.getInstance();

        log.debug("Binding bootstrap hooks from {}", hooksContainer);
        beansService.inject(hooksContainer);
        hooksContainer.bindHooks();

        log.debug("Binding TestEngine processor-context instances with DI");
        beansService.bind(context.getRunner());
        beansService.bind(context.getProcessor());

        log.debug("Injecting testing object (self) with DI: {}", testingObject);
        beansService.fakeBind(testingObject);
        beansService.fakeBind(instance);
    }

}
