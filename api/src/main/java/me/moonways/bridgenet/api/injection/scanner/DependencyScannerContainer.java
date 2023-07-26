package me.moonways.bridgenet.api.injection.scanner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.injection.scanner.controller.ScannerController;
import me.moonways.bridgenet.api.xml.XmlJaxbParser;
import me.moonways.bridgenet.api.injection.DependencyInjection;
import me.moonways.bridgenet.api.injection.Inject;
import me.moonways.bridgenet.api.injection.InjectionErrorMessages;
import me.moonways.bridgenet.api.injection.factory.ObjectFactory;
import me.moonways.bridgenet.api.injection.xml.XmlObjectFactory;
import me.moonways.bridgenet.api.injection.xml.XmlContainers;
import me.moonways.bridgenet.api.injection.xml.XmlScanController;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class DependencyScannerContainer {

    @Getter
    private String generalPackage;

    private final Map<Class<?>, ObjectFactory> objectFactoryMap = new HashMap<>();
    private final Map<Class<?>, ScannerController> scannerControllerMap = new HashMap<>();

    @Inject
    private DependencyInjection dependencyInjection;

    void initMaps() {
        XmlJaxbParser parser = new XmlJaxbParser();
        XmlContainers xmlContainers = parser.parseCopiedResource(getClass().getClassLoader(), "injectconfig.xml", XmlContainers.class);

        storeScanners(xmlContainers);
        storeFactories(xmlContainers);

        generalPackage = xmlContainers.getSearchPackage();
    }

    public ObjectFactory getObjectFactory(Class<? extends Annotation> cls) {
        return objectFactoryMap.get(cls);
    }

    public ScannerController getScannerController(Class<? extends Annotation> cls) {
        return scannerControllerMap.get(cls);
    }

    private void storeScanners(XmlContainers xmlContainers) {
        List<XmlScanController> scannersList = xmlContainers.getScannersList();

        for (XmlScanController xmlScanController : scannersList) {

            String annotationClassName = xmlScanController.getAnnotationClass();
            String targetClassName = xmlScanController.getTargetClass();

            try {
                Class<?> annotationClass = Class.forName(annotationClassName);
                Class<?> scannerClass = Class.forName(targetClassName);

                Class<? extends ScannerController> subclass = scannerClass.asSubclass(ScannerController.class);

                ScannerController scannerController = subclass.getConstructor().newInstance();

                dependencyInjection.injectFields(scannerController);
                scannerControllerMap.put(annotationClass, scannerController);
            }
            catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                   NoSuchMethodException | ClassNotFoundException exception) {

                MessageFactory messageFactory = log.getMessageFactory();
                Message message = messageFactory.newMessage(InjectionErrorMessages.CANNOT_CREATE_OBJECT_INSTANCE,
                        targetClassName);

                log.error(message, exception);
            }
        }
    }

    private void storeFactories(XmlContainers xmlContainers) {
        List<XmlObjectFactory> factoriesList = xmlContainers.getFactoriesList();

        for (XmlObjectFactory xmlObjectFactory : factoriesList) {

            String annotationClassName = xmlObjectFactory.getAnnotationClass();
            String targetClassName = xmlObjectFactory.getTargetClass();

            try {
                Class<?> annotationClass = Class.forName(annotationClassName);
                Class<?> scannerClass = Class.forName(targetClassName);

                Class<? extends ObjectFactory> subclass = scannerClass.asSubclass(ObjectFactory.class);

                ObjectFactory objectFactory = subclass.getConstructor().newInstance();

                dependencyInjection.injectFields(objectFactory);
                objectFactoryMap.put(annotationClass, objectFactory);
            }
            catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                   NoSuchMethodException | ClassNotFoundException exception) {

                MessageFactory messageFactory = log.getMessageFactory();
                Message message = messageFactory.newMessage(InjectionErrorMessages.CANNOT_CREATE_OBJECT_INSTANCE,
                        targetClassName);

                log.error(message, exception);
            }
        }
    }
}
