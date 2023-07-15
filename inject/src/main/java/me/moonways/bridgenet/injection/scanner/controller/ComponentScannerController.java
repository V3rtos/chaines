package me.moonways.bridgenet.injection.scanner.controller;

import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.DependencyContainer;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.factory.ObjectFactory;
import me.moonways.bridgenet.injection.scanner.ScannerFilter;
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

public class ComponentScannerController implements ScannerController {

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
    public Set<Class<?>> findAllComponents(ScannerFilter filter) {
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
    public void whenFound(DependencyInjection dependencyInjection, Class<?> resource) {
        DependencyContainer container = dependencyInjection.getContainer();

        if (resource == DependencyInjection.class) {

            dependencyInjection.bind(this);
            container.addComponentWithAnnotation(resource, Component.class);
            return;
        }

        if (container.isComponentFound(resource))
            return;

        ObjectFactory objectFactory = dependencyInjection.getScanner().getObjectFactory(Component.class);

        Object object = objectFactory.create(resource);

        dependencyInjection.bind(resource, object);
        container.addComponentWithAnnotation(resource, Component.class);
    }
}
