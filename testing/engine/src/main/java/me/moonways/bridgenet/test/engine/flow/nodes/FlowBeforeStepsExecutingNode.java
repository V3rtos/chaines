package me.moonways.bridgenet.test.engine.flow.nodes;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.test.engine.DisplayTestItem;
import me.moonways.bridgenet.test.engine.component.module.Module;
import me.moonways.bridgenet.test.engine.component.step.Step;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
public class FlowBeforeStepsExecutingNode implements TestFlowNode {

    private final AtomicInteger dependenciesCounter = new AtomicInteger();

    @Override
    public void execute(TestFlowContext context) {
        Optional<List<Step>> beforeStepsListOptional = context.getInstance(TestFlowContext.BEFORE_STEPS);
        beforeStepsListOptional.ifPresent(preparedStepsList ->
                executeAll(context, preparedStepsList));
    }

    /**
     * Получает зависимости бинов из списка подготовленных шагов.
     *
     * @param preparedStepsList список подготовленных шагов.
     * @return список классов зависимостей бинов.
     */
    private List<Class<?>> getBeanDependencies(List<Step> preparedStepsList) {
        return preparedStepsList.stream()
                .flatMap(step -> step.config().getBeansDependencies().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Получает зависимости модулей из списка подготовленных шагов.
     *
     * @param preparedStepsList список подготовленных шагов.
     * @return список классов зависимостей модулей.
     */
    private List<Class<? extends Module>> getModuleDependencies(List<Step> preparedStepsList) {
        return preparedStepsList.stream()
                .flatMap(step -> step.config().getModulesDependencies().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private void executeAll(TestFlowContext context, List<Step> preparedStepsList) {
        List<Class<? extends Module>> moduleDependencies = getModuleDependencies(preparedStepsList);
        applyingModules(context, moduleDependencies);

        List<Class<?>> dependencies = new ArrayList<>();

        dependencies.addAll(moduleDependencies);
        dependencies.addAll(getBeanDependencies(preparedStepsList));

        subscribe(context, preparedStepsList, dependencies);
    }

    private void subscribe(TestFlowContext context, List<Step> preparedStepsList, List<Class<?>> dependencies) {
        Optional<BeansService> beansServiceOptional = context.getInstance(TestFlowContext.BEANS);
        BeansService beansService = beansServiceOptional.get();

        for (Class<?> dependency : dependencies) {
            beansService.subscribeOn(dependency, (bean) -> {

                if (dependenciesCounter.incrementAndGet() == dependencies.size()) {
                    doExecute(context, preparedStepsList);
                }
            });
        }
    }

    private void doExecute(TestFlowContext context, List<Step> preparedStepsList) {
        BeansService beansService = context.getInstance(TestFlowContext.BEANS).get();
        log.debug("Executing §2{} §rbefore-steps...", preparedStepsList.size());

        preparedStepsList.forEach(step -> {

            DisplayTestItem displayTestItem = DisplayTestItem.builder()
                    .testClass(context.getTestClass())
                    .notifier(context.getRunNotifier())
                    .displayName(String.format("[:before-step] %s", step.getClass().getSimpleName()))
                    .build();

            log.debug("Executing before-step §2{}§r...", step.getClass().getSimpleName());
            beansService.inject(step);

            try {
                displayTestItem.fireStarted();
                step.execute(context);
                displayTestItem.fireFinished();
            } catch (Throwable throwable) {
                displayTestItem.fireFinishedFailure();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyingModules(TestFlowContext context, List<Class<? extends Module>> moduleDependencies) {
        Optional<List<Module>> loadedModulesListOptional = context.getInstance(TestFlowContext.LOADED_MODULES);
        List<Module> modules = loadedModulesListOptional.orElse(new ArrayList<>());

        context.getFlowNode(FlowModulesApplyingNode.class)
                .ifPresent(flowModulesApplyingNode -> {

                    flowModulesApplyingNode.processModulesAnnotation(context,
                            moduleDependencies.stream()
                                    .filter(moduleClass -> modules.stream().noneMatch(module -> module.getClass().equals(moduleClass)))
                                    .toArray(Class[]::new));
                });
    }
}
