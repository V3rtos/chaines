package me.moonways.bridgenet.injection.scanner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.injection.InjectionErrorMessages;
import me.moonways.bridgenet.injection.factory.ObjectFactory;
import me.moonways.bridgenet.injection.scanner.controller.ScannerController;
import me.moonways.bridgenet.injection.xml.XMLConfiguration;
import me.moonways.bridgenet.injection.xml.XMLConfigurationParser;
import me.moonways.bridgenet.injection.xml.element.XMLObjectFactory;
import me.moonways.bridgenet.injection.xml.element.XMLRootElement;
import me.moonways.bridgenet.injection.xml.element.XMLScanController;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class DependencyScannerContainer {

    private final Map<Class<?>, ObjectFactory> objectFactoryMap = new HashMap<>();
    private final Map<Class<?>, ScannerController> scannerControllerMap = new HashMap<>();

    @Inject
    private DependencyInjection dependencyInjection;

    void initMaps() {
        XMLConfigurationParser parser = new XMLConfigurationParser();
        XMLConfiguration xmlConfiguration = parser.parseNewInstance();

        storeScanners(xmlConfiguration);
        storeFactories(xmlConfiguration);
    }

    public ObjectFactory getObjectFactory(Class<? extends Annotation> cls) {
        return objectFactoryMap.get(cls);
    }

    public ScannerController getScannerController(Class<? extends Annotation> cls) {
        return scannerControllerMap.get(cls);
    }

    private void storeScanners(XMLConfiguration xmlConfiguration) {
        XMLRootElement rootElement = xmlConfiguration.getRootElement();
        List<XMLScanController> scannersList = rootElement.getScannersList();

        for (XMLScanController xmlScanController : scannersList) {

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

    private void storeFactories(XMLConfiguration xmlConfiguration) {
        XMLRootElement rootElement = xmlConfiguration.getRootElement();
        List<XMLObjectFactory> factoriesList = rootElement.getFactoriesList();

        for (XMLObjectFactory xmlObjectFactory : factoriesList) {

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
