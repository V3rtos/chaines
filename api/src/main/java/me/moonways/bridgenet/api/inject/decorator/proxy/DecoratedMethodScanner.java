package me.moonways.bridgenet.api.inject.decorator.proxy;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.xml.XmlDecoratorHandler;
import me.moonways.bridgenet.api.inject.decorator.xml.XmlDecoratedInterceptor;
import me.moonways.bridgenet.api.jaxb.XmlJaxbParser;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public final class DecoratedMethodScanner {

    private final Map<Class<?>, DecoratedMethodHandler> handlersByAnnotationsMap = new HashMap<>();

    private XmlDecoratedInterceptor parseXmlInterceptor() {
        final XmlJaxbParser parser = new XmlJaxbParser();
        final ClassLoader classLoader = getClass().getClassLoader();

        return parser.parseResource(classLoader, "decorators.xml",
                XmlDecoratedInterceptor.class);
    }

    public void injectProxiedHandlers() {
        XmlDecoratedInterceptor xmlProxyInterceptor = parseXmlInterceptor();
        for (XmlDecoratorHandler xmlMethodHandler : xmlProxyInterceptor.getMethodHandlers()) {
            try {
                Class<?> annotationClass = Class.forName(xmlMethodHandler.getAnnotationClassname());
                Class<?> handlerClass = Class.forName(xmlMethodHandler.getHandlerClassname());

                DecoratedMethodHandler instance = handlerClass.asSubclass(DecoratedMethodHandler.class).newInstance();

                handlersByAnnotationsMap.put(annotationClass, instance);
            }
            catch (ClassNotFoundException | InstantiationException
                   | IllegalAccessException exception) {

                log.error("§4Cannot be inject MethodHandler for {} annotation: §c{}", xmlMethodHandler.getAnnotationClassname(), exception.toString());
            }
        }
    }

    public DecoratedMethodHandler findMethodHandler(Class<? extends Annotation> annotation) {
        return handlersByAnnotationsMap.get(annotation);
    }

    public Set<Class<?>> getAnnotationsTypes() {
        return handlersByAnnotationsMap.keySet();
    }
}
