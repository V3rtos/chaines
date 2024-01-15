package me.moonways.bridgenet.api.inject.decorator;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.xml.XmlDecoratorHandler;
import me.moonways.bridgenet.api.inject.decorator.xml.XmlDecoratedInterceptor;
import me.moonways.bridgenet.api.inject.decorator.xml.XmlDecoratorInput;
import me.moonways.bridgenet.api.util.jaxb.XmlJaxbParser;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public final class DecoratedMethodScanner {

    private final Map<Class<?>, DecoratedMethodHandler> handlersByAnnotationsMap = new HashMap<>();
    private final Map<Class<?>, String> namesByAnnotationsMap = new HashMap<>();

    private final Map<String, Set<String>>
        conflictsMap = new HashMap<>(),
        inheritsMap = new HashMap<>();

    private XmlDecoratedInterceptor parseXmlInterceptor() {
        final XmlJaxbParser parser = new XmlJaxbParser();
        final ClassLoader classLoader = getClass().getClassLoader();

        return parser.parseResource(classLoader, "decorators.xml",
                XmlDecoratedInterceptor.class);
    }

    public void bindHandlers() {
        XmlDecoratedInterceptor rootXML = parseXmlInterceptor();

        for (XmlDecoratorHandler decoratorXML : rootXML.getMethodHandlers()) {
            String name = decoratorXML.getName();

            try {
                Class<?> annotationClass = Class.forName(decoratorXML.getAnnotationClassname());
                Class<?> handlerClass = Class.forName(decoratorXML.getHandlerClassname());

                DecoratedMethodHandler instance = handlerClass.asSubclass(DecoratedMethodHandler.class).newInstance();

                handlersByAnnotationsMap.put(annotationClass, instance);
                namesByAnnotationsMap.put(annotationClass, name);

                List<XmlDecoratorInput> conflicts = decoratorXML.getConflicts();
                List<XmlDecoratorInput> inherits = decoratorXML.getInherits();

                if (conflicts != null) {
                    conflictsMap.put(name,
                        conflicts.stream().map(XmlDecoratorInput::getValue).collect(Collectors.toSet()));
                }
                if (inherits != null) {
                    inheritsMap.put(name,
                        inherits.stream().map(XmlDecoratorInput::getValue).collect(Collectors.toSet()));
                }
            }
            catch (ClassNotFoundException | InstantiationException
                   | IllegalAccessException exception) {

                log.error("§4Cannot be inject MethodHandler for {} annotation: §c{}", decoratorXML.getAnnotationClassname(), exception.toString());
            }
        }
    }

    public DecoratedMethodHandler findMethodHandler(Class<? extends Annotation> annotation) {
        return handlersByAnnotationsMap.get(annotation);
    }

    public Set<Class<?>> getAnnotationsTypes() {
        return handlersByAnnotationsMap.keySet();
    }

    public Set<Class<?>> findConflictedAnnotations(Class<? extends Annotation> cls) {
        String name = namesByAnnotationsMap.get(cls);
        Set<String> conflictedNames = conflictsMap.get(name);

        if (conflictedNames == null) {
            return Collections.emptySet();
        }

        return conflictedNames.stream()
            .map(this::findAnnotationByName)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    public Set<Class<?>> findInheritsAnnotations(Class<? extends Annotation> cls) {
        String name = namesByAnnotationsMap.get(cls);
        Set<String> parentsNames = inheritsMap.get(name);

        if (parentsNames == null) {
            return Collections.emptySet();
        }

        return parentsNames.stream()
            .map(this::findAnnotationByName)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    private Class<?> findAnnotationByName(String name) {
        for (Entry<Class<?>, String> entry : namesByAnnotationsMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
