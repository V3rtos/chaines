package me.moonways.bridgenet.test.engine.flow.nodes;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.bean.service.BeansAnnotationsAwaitService;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.def.DefaultTypeAnnotationProcessor;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import me.moonways.bridgenet.test.engine.component.module.Module;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;
import me.moonways.bridgenet.test.engine.component.module.impl.AllModules;
import me.moonways.bridgenet.test.engine.persistance.TestModules;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
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
        processModulesAnnotation(context, annotation.value());
    }

    public void processModulesAnnotation(TestFlowContext context, Class<? extends Module>[] typesArray) {
        BeansService beansService = context.getInstance(TestFlowContext.BEANS).get();
        List<Module> instancesList = toInstancesList(new ArrayList<>(), typesArray);

        log.debug("Analyzing §2{} §rimplemented modules...", instancesList.size());
        List<Bean> beans = createBeansList(beansService, instancesList);

        log.debug("Binding §6{} §rbeans from Test-Engine modules: §7[{}]", beans.size(),
                beans.stream()
                        .map(bean -> bean.getType().getRoot().getSimpleName())
                        .collect(Collectors.joining(", ")));

        beans.forEach(bean -> processBeanBinding(beansService, bean));

        log.debug("Installing §2{} §rimplemented modules...", instancesList.size());

        context.setInstance(TestFlowContext.LOADED_MODULES, instancesList);
        instancesList.forEach(module -> {

            beansService.bind(module);
            module.install(context);
        });
    }

    private List<Bean> createBeansList(BeansService beansService, List<Module> instancesList) {
        List<Bean> beans;
        if (instancesList.stream().noneMatch(module -> module.getClass().equals(AllModules.class))) {

            List<String> packageNames = new ArrayList<>();
            packageNames.add(TESTS_PACKAGE_NAME);

            for (Module module : instancesList) {
                log.debug("Analyzing module §6{}§r...", module.getClass().getSimpleName());

                ModuleConfig config = module.config();
                packageNames.addAll(config.getPackagesToScanning());
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
                beansService.processTypeAnnotationProcessor(new DefaultTypeAnnotationProcessor(annotationType));
            }
        }
    }

    private List<Module> toInstancesList(List<Module> previousInstancesList, Class<? extends Module>[] typesArray) {
        List<Module> engineModuleList = new ArrayList<>();

        for (Class<? extends Module> engineModuleClass : typesArray) {
            Module module = BeanFactoryProviders.DEFAULT.getImpl()
                    .get()
                    .create(engineModuleClass);

            previousInstancesList.add(module);

            ModuleConfig config = module.config();
            if (config.getDependencies() != null && !config.getDependencies().isEmpty()) {

                //noinspection unchecked
                List<Module> instancesList = toInstancesList(previousInstancesList, config.getDependencies()
                        .stream()
                        .filter(dependencyClass -> previousInstancesList.stream().noneMatch(previous -> previous.getClass().equals(dependencyClass)))
                        .toArray(Class[]::new));

                engineModuleList.addAll(instancesList);
            }

            engineModuleList.add(module);
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
