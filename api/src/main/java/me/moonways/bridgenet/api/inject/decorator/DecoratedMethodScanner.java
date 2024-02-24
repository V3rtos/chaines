package me.moonways.bridgenet.api.inject.decorator;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.decorator.config.XMLMethodHandlerDescriptor;
import me.moonways.bridgenet.api.inject.decorator.config.XMLInterceptorDescriptor;
import me.moonways.bridgenet.api.inject.decorator.config.XMLInputDescriptor;
import me.moonways.bridgenet.api.util.jaxb.XmlJaxbParser;
import me.moonways.bridgenet.assembly.ResourcesTypes;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public final class DecoratedMethodScanner {

    private final Map<Class<?>, DecoratedMethodHandler> handlersByAnnotationsMap = new HashMap<>();
    private final Map<Class<?>, String> namesByAnnotationsMap = new HashMap<>();
    private final Map<String, Set<String>> conflictsMap = new HashMap<>(), inheritsMap = new HashMap<>();

    @Inject
    private XmlJaxbParser xmlJaxbParser;

    private XMLInterceptorDescriptor parseXmlInterceptor() {
        return xmlJaxbParser.parseToDescriptorByType(ResourcesTypes.DECORATORS_XML, XMLInterceptorDescriptor.class);
    }

    public void bindHandlers() {
        XMLInterceptorDescriptor rootXML = parseXmlInterceptor();

        for (XMLMethodHandlerDescriptor decoratorXML : rootXML.getMethodHandlers()) {
            String name = decoratorXML.getName();

            try {
                Class<?> annotationClass = Class.forName(decoratorXML.getAnnotationClassname());
                Class<?> handlerClass = Class.forName(decoratorXML.getHandlerClassname());

                DecoratedMethodHandler instance = handlerClass.asSubclass(DecoratedMethodHandler.class).newInstance();

                handlersByAnnotationsMap.put(annotationClass, instance);
                namesByAnnotationsMap.put(annotationClass, name);

                List<XMLInputDescriptor> conflicts = decoratorXML.getConflicts();
                List<XMLInputDescriptor> inherits = decoratorXML.getInherits();

                if (conflicts != null) {
                    conflictsMap.put(name,
                        conflicts.stream().map(XMLInputDescriptor::getValue).collect(Collectors.toSet()));
                }
                if (inherits != null) {
                    inheritsMap.put(name,
                        inherits.stream().map(XMLInputDescriptor::getValue).collect(Collectors.toSet()));
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
