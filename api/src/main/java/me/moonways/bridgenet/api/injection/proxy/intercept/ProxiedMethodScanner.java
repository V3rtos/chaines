package me.moonways.bridgenet.api.injection.proxy.intercept;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.injection.proxy.xml.XmlProxiedMethodHandler;
import me.moonways.bridgenet.api.injection.proxy.xml.XmlProxyInterceptor;
import me.moonways.bridgenet.api.xml.XmlJaxbParser;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public final class ProxiedMethodScanner {

    private final Map<Class<?>, ProxiedMethodHandler> handlersByAnnotationsMap = new HashMap<>();

    private XmlProxyInterceptor parseXmlInterceptor() {
        final XmlJaxbParser parser = new XmlJaxbParser();
        final ClassLoader classLoader = getClass().getClassLoader();

        return parser.parseResource(classLoader, "proxymethodconfig.xml",
                XmlProxyInterceptor.class);
    }

    public void injectProxiedHandlers() {
        XmlProxyInterceptor xmlProxyInterceptor = parseXmlInterceptor();
        for (XmlProxiedMethodHandler xmlMethodHandler : xmlProxyInterceptor.getMethodHandlers()) {
            try {
                Class<?> annotationClass = Class.forName(xmlMethodHandler.getAnnotationClassname());
                Class<?> handlerClass = Class.forName(xmlMethodHandler.getHandlerClassname());

                ProxiedMethodHandler instance = handlerClass.asSubclass(ProxiedMethodHandler.class).newInstance();

                handlersByAnnotationsMap.put(annotationClass, instance);
            }
            catch (ClassNotFoundException | InstantiationException
                   | IllegalAccessException exception) {

                log.error("§4Cannot be inject MethodHandler for {} annotation: §c{}", xmlMethodHandler.getAnnotationClassname(), exception.toString());
            }
        }
    }

    public ProxiedMethodHandler findMethodHandler(Class<? extends Annotation> annotation) {
        return handlersByAnnotationsMap.get(annotation);
    }

    public Set<Class<?>> getAnnotationsTypes() {
        return handlersByAnnotationsMap.keySet();
    }
}
