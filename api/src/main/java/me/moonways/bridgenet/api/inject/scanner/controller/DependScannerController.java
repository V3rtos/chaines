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
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

@Log4j2
public class DependScannerController implements ScannerController {

    private Configuration createConfiguration(ScannerFilter scannerFilter) {
        log.info("Create scanner configuration by {}", scannerFilter);

        List<ClassLoader> classLoadersList = new ArrayList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        FilterBuilder filterBuilder = new FilterBuilder();
        scannerFilter.getPackageNames().forEach(filterBuilder::includePackage);

        Collection<URL> urls = ClasspathHelper.forClassLoader(
                classLoadersList.toArray(new ClassLoader[0]));

        return new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes, Scanners.Resources)
                .filterInputsBy(filterBuilder);
    }

    @Override
    public Set<Class<?>> requestResources(@NotNull ScannerFilter filter) {
        Configuration configuration = createConfiguration(filter);

        Reflections reflections = new Reflections(configuration);

        Set<Class<? extends Annotation>> annotations = filter.getAnnotations();

        Set<Class<?>> result = new HashSet<>();

        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
            result.addAll(typesAnnotatedWith);
        }

        return result;
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
