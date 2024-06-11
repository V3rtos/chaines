package me.moonways.bridgenet.test.engine.flow.nodes;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.test.engine.component.step.Step;
import me.moonways.bridgenet.test.engine.component.step.StepConfig;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import me.moonways.bridgenet.test.engine.persistance.BeforeSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для применения шагов, которые должны быть выполнены перед тестом.
 */
@Log4j2
public class FlowBeforeStepsApplyingNode implements TestFlowNode {

    /**
     * Выполняет узел тестового потока.
     *
     * @param context контекст тестового потока.
     */
    @Override
    public void execute(TestFlowContext context) {
        BeforeSteps annotation = context.getTestClass().getAnnotation(BeforeSteps.class);

        if (annotation == null) {
            log.debug("§4Annotation §e@BeforeSteps §4is not founded, skipping step.");
            return;
        }

        log.debug("Annotation §e@BeforeSteps §ris founded, processing...");
        processBeforeStepsAnnotation(context, annotation);
    }

    /**
     * Обрабатывает аннотацию @BeforeSteps.
     *
     * @param context    контекст тестового потока.
     * @param annotation аннотация @BeforeSteps.
     */
    private void processBeforeStepsAnnotation(TestFlowContext context, BeforeSteps annotation) {
        List<Step> preparedStepsList = toInstancesList(new ArrayList<>(), annotation.value());
        log.debug("Analyzing §2{} §rbefore-steps...", preparedStepsList.size());
        context.setInstance(TestFlowContext.BEFORE_STEPS, preparedStepsList);
    }

    /**
     * Преобразует массив классов шагов в список экземпляров шагов.
     *
     * @param previousInstancesList список предыдущих экземпляров шагов.
     * @param typesArray            массив классов шагов.
     * @return список экземпляров шагов.
     */
    private List<Step> toInstancesList(List<Step> previousInstancesList, Class<? extends Step>[] typesArray) {
        List<Step> resultList = new ArrayList<>();

        for (Class<? extends Step> stepClass : typesArray) {
            Step step = BeanFactoryProviders.DEFAULT.getImpl().get().create(stepClass);
            previousInstancesList.add(step);

            StepConfig config = step.config();

            addStepsFromConfig(previousInstancesList, resultList, config.getBeforeSteps());
            resultList.add(step);
            addStepsFromConfig(previousInstancesList, resultList, config.getAfterSteps());
        }

        return resultList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Добавляет шаги из конфигурации в результирующий список.
     *
     * @param previousInstancesList список предыдущих экземпляров шагов.
     * @param resultList            результирующий список шагов.
     * @param stepsConfig           конфигурация шагов.
     */
    private void addStepsFromConfig(List<Step> previousInstancesList, List<Step> resultList, List<Class<? extends Step>> stepsConfig) {
        if (stepsConfig != null && !stepsConfig.isEmpty()) {
            List<Step> instancesList = toInstancesList(previousInstancesList, stepsConfig
                    .stream()
                    .filter(dependencyClass -> previousInstancesList.stream().noneMatch(module -> module.getClass().equals(dependencyClass)))
                    .toArray(Class[]::new));

            resultList.addAll(instancesList);
        }
    }
}
