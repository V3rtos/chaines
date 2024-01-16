package me.moonways.bridgenet.api.inject.scanner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.scanner.controller.ScannerController;
import me.moonways.bridgenet.api.util.jaxb.XmlJaxbParser;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.InjectionErrorMessages;
import me.moonways.bridgenet.api.inject.factory.ObjectFactory;
import me.moonways.bridgenet.api.inject.config.XMLObjectFactoryDescriptor;
import me.moonways.bridgenet.api.inject.config.XMLContainersDescriptor;
import me.moonways.bridgenet.api.inject.config.XMLScannerDescriptor;

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
    private DependencyInjection injector;

    void initMaps() {
        XmlJaxbParser parser = new XmlJaxbParser();
        XMLContainersDescriptor xmlContainers = parser.parseCopiedResource(getClass().getClassLoader(), "injectconfig.xml", XMLContainersDescriptor.class);

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

    private void storeScanners(XMLContainersDescriptor xmlContainers) {
        List<XMLScannerDescriptor> scannersList = xmlContainers.getScannersList();

        for (XMLScannerDescriptor xmlScanController : scannersList) {

            String annotationClassName = xmlScanController.getAnnotationClass();
            String targetClassName = xmlScanController.getTargetClass();

            try {
                Class<?> annotationClass = Class.forName(annotationClassName);
                Class<?> scannerClass = Class.forName(targetClassName);

                Class<? extends ScannerController> subclass = scannerClass.asSubclass(ScannerController.class);

                ScannerController scannerController = subclass.getConstructor().newInstance();

                injector.injectFields(scannerController);
                scannerControllerMap.put(annotationClass, scannerController);
            }
            catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                   NoSuchMethodException | ClassNotFoundException exception) {
                log.error(InjectionErrorMessages.CANNOT_CREATE_OBJECT_INSTANCE, targetClassName, exception.toString());
            }
        }
    }

    private void storeFactories(XMLContainersDescriptor xmlContainers) {
        List<XMLObjectFactoryDescriptor> factoriesList = xmlContainers.getFactoriesList();

        for (XMLObjectFactoryDescriptor xmlObjectFactory : factoriesList) {

            String annotationClassName = xmlObjectFactory.getAnnotationClass();
            String targetClassName = xmlObjectFactory.getTargetClass();

            try {
                Class<?> annotationClass = Class.forName(annotationClassName);
                Class<?> scannerClass = Class.forName(targetClassName);

                Class<? extends ObjectFactory> subclass = scannerClass.asSubclass(ObjectFactory.class);

                ObjectFactory objectFactory = subclass.getConstructor().newInstance();

                injector.injectFields(objectFactory);
                objectFactoryMap.put(annotationClass, objectFactory);
            }
            catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                   NoSuchMethodException | ClassNotFoundException exception) {

                log.error(InjectionErrorMessages.CANNOT_CREATE_OBJECT_INSTANCE, targetClassName, exception.toString());
            }
        }
    }
}
