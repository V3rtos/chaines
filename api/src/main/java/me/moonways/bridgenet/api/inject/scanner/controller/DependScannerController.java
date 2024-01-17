package me.moonways.bridgenet.api.inject.scanner.controller;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyContainer;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.factory.ObjectFactory;
import me.moonways.bridgenet.api.inject.scanner.DependencyScanner;
import me.moonways.bridgenet.api.inject.scanner.ScannerFilter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

@Log4j2
public class DependScannerController implements ScannerController {

    private final TypeAnnotationsScanner typeAnnotationsScanner = new TypeAnnotationsScanner();
    private final SubTypesScanner subTypesScanner = new SubTypesScanner(false);
    private final ResourcesScanner resourcesScanner = new ResourcesScanner();

    private Set<Class<?>> mergeSets(Set<Set<Class<?>>> setOfSets) {
        Set<Class<?>> resultSet = new HashSet<>();

        for (Set<Class<?>> internal : setOfSets) {
            resultSet.addAll(internal);
        }

        return resultSet;
    }

    private Configuration createConfiguration(ScannerFilter scannerFilter) {
        log.info("Create scanner configuration by {}", scannerFilter);

        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        FilterBuilder inputsFilter = new FilterBuilder()
                .includePackage(scannerFilter.getPackageNames().toArray(new String[0]));

        Collection<URL> urls = ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]));

        return new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(typeAnnotationsScanner, subTypesScanner, resourcesScanner)
                .filterInputsBy(inputsFilter);
    }

    @Override
    public Set<Class<?>> requestResources(@NotNull ScannerFilter filter) {
        Configuration configuration = createConfiguration(filter);

        Reflections reflections = new Reflections(configuration);

        Set<Class<? extends Annotation>> annotations = filter.getAnnotations();
        Set<Set<Class<?>>> setOfSets = new HashSet<>();

        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
            setOfSets.add(typesAnnotatedWith);
        }

        return mergeSets(setOfSets);
    }

    @Override
    public void handleResource(@NotNull DependencyInjection injector,
                               @NotNull Class<?> resource,
                               @NotNull Class<? extends Annotation> annotation) {

        DependencyContainer container = injector.getContainer();

        if (resource == DependencyInjection.class) {

            injector.bind(this);
            container.subscribeDependByAnnotation(resource, annotation);
            return;
        }

        if (container.isStored(resource))
            return;

        DependencyScanner scanner = injector.getScanner();

        ObjectFactory objectFactory = scanner.getObjectFactory(annotation);
        Object object = objectFactory.create(resource);

        scanner.processPreConstructs(resource);

        bind(injector, resource, object, annotation);
    }

    public final void bind(@NotNull DependencyInjection injector,
                           @NotNull Class<?> resource,
                           @NotNull Object object,
                           @NotNull Class<? extends Annotation> annotation) {

        DependencyContainer container = injector.getContainer();

        injector.bind(resource, object);
        container.subscribeDependByAnnotation(resource, annotation);
    }
}
