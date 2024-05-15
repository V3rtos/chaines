package me.moonways.bridgenet.test.engine.flow.nodes;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.bean.service.BeansAnnotationsAwaitService;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.def.JustBindTypeAnnotationProcessor;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import me.moonways.bridgenet.test.engine.module.TestEngineModule;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;
import me.moonways.bridgenet.test.engine.module.impl.AllModules;
import me.moonways.bridgenet.test.engine.persistance.TestModules;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class FlowModulesApplyingNode implements TestFlowNode {

    private static final String TESTS_PACKAGE_NAME = "me.moonways.bridgenet.test";

    @Override
    public void execute(TestFlowContext context) {
        TestModules annotation = context.getTestClass().getAnnotation(TestModules.class);

        if (annotation == null) {
            log.debug("§4Annotation §e@TestModules §4is not founded, skipping step.");
            return;
        }

        log.debug("Annotation §e@TestModules §ris founded, processing...");
        processModulesAnnotation(context, annotation);
    }

    private void processModulesAnnotation(TestFlowContext context, TestModules annotation) {
        BeansService beansService = context.getInstance(TestFlowContext.BEANS).get();
        List<TestEngineModule> instancesList = toInstancesList(new ArrayList<>(), annotation.value());

        log.debug("Analyzing §2{} §rimplemented modules...", instancesList.size());
        List<Bean> beans = createBeansList(beansService, instancesList);

        log.debug("Binding §6{} §rbeans from Test-Engine modules: §7[{}]", beans.size(),
                beans.stream()
                        .map(bean -> bean.getType().getRoot().getSimpleName())
                        .collect(Collectors.joining(", ")));

        beans.forEach(bean -> processBeanBinding(beansService, bean));
    }

    private List<Bean> createBeansList(BeansService beansService, List<TestEngineModule> instancesList) {
        List<Bean> beans;
        if (instancesList.stream().noneMatch(testEngineModule -> testEngineModule.getClass().equals(AllModules.class))) {

            List<String> packageNames = new ArrayList<>();
            packageNames.add(TESTS_PACKAGE_NAME);

            for (TestEngineModule instance : instancesList) {
                log.debug("Analyzing module §6{}§r...", instance.getClass());

                TestModuleBeans instanceBeans = instance.getBeans();
                packageNames.addAll(instanceBeans.getPackagesToScanning());
            }

            beans = beansService.getScanner()
                    .scanByPackage(packageNames.toArray(new String[0]));

            sortBeansByPackagesList(beans, packageNames);

        } else {
            log.debug("Loading all system modules...");

            List<TypeAnnotationProcessor<?>> typeAnnotationProcessors = beansService.getScanner()
                    .scanAnnotationProcessors();

            beansService.scanAnnotationProcessors(typeAnnotationProcessors);
            beans = new ArrayList<>(beansService.getStore().getTotalBeans());
        }
        return beans;
    }

    private void processBeanBinding(BeansService beansService, Bean bean) {
        beansService.bind(bean);

        BeansAnnotationsAwaitService annotationsAwaits = beansService.getAnnotationsAwaits();

        if (annotationsAwaits.needsAwaits(bean)) {
            Class<? extends Annotation>[] awaitsAnnotationsTypes = annotationsAwaits.getAwaitsAnnotationsTypes(bean);

            for (Class<? extends Annotation> annotationType : awaitsAnnotationsTypes) {
                beansService.processTypeAnnotationProcessor(new JustBindTypeAnnotationProcessor(annotationType));
            }
        }
    }

    private List<TestEngineModule> toInstancesList(List<TestEngineModule> previousInstancesList, Class<? extends TestEngineModule>[] typesArray) {
        List<TestEngineModule> engineModuleList = new ArrayList<>();

        for (Class<? extends TestEngineModule> engineModuleClass : typesArray) {
            TestEngineModule testEngineModule = BeanFactoryProviders.DEFAULT.getImpl()
                    .get()
                    .create(engineModuleClass);

            previousInstancesList.add(testEngineModule);

            TestModuleBeans beans = testEngineModule.getBeans();
            if (beans.getDependencies() != null && !beans.getDependencies().isEmpty()) {

                //noinspection unchecked
                List<TestEngineModule> instancesList = toInstancesList(previousInstancesList, beans.getDependencies()
                        .stream()
                        .filter(dependencyClass -> previousInstancesList.stream().noneMatch(module -> module.getClass().equals(dependencyClass)))
                        .toArray(Class[]::new));

                engineModuleList.addAll(instancesList);
            }

            engineModuleList.add(testEngineModule);
        }

        return engineModuleList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void sortBeansByPackagesList(List<Bean> beans, List<String> packages) {
        List<Bean> sortedList = new ArrayList<>();

        for (String packageName : packages) {
            for (Bean bean : beans) {
                if (bean.getType().getRoot().getPackage().getName().startsWith(packageName)) {
                    sortedList.add(bean);
                }
            }
        }

        beans.clear();
        beans.addAll(sortedList);
    }
}
