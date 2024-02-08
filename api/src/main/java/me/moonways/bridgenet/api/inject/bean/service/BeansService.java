package me.moonways.bridgenet.api.inject.bean.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanComponent;
import me.moonways.bridgenet.api.inject.bean.BeanConstructFunction;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.processor.AnnotationProcessorConfig;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationContext;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationResult;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

/**
 * Основная точка управления системой бинов,
 * которые относятся к реализации
 * технологии Dependency Injection. Здесь
 * реализовано все - сканирование и настройка проекта
 * по пакейджам, чтение конфигурации, бинд, удаление
 * и инжекция бинов.
 */
@Log4j2
public final class BeansService {

    public static final String PROPERTY_PACKAGE_NAME = "inject.project.package.name";
    public static final String PROPERTY_DEFAULT_BEAN_FACTORY = "inject.bean.factory.default";

    /**
     * Сгенерировать стандартные проперти-конфигурации,
     * хранящие стандартные ключи и значения для
     * корректной работы Dependency Injection.
     */
    public static Properties generateDefaultProperties() {
        Properties properties = new Properties();

        properties.setProperty(PROPERTY_PACKAGE_NAME, "me.moonways");
        properties.setProperty(PROPERTY_DEFAULT_BEAN_FACTORY, "CONSTRUCTOR");

        return properties;
    }

    private final BeansStore store;
    private final BeansScanningService scanner;
    private final BeansInjectionService injector;
    private final BeansAnnotationsAwaitService annotationsAwaits;

    private final AnnotationInterceptor interceptor = new AnnotationInterceptor();

    private final Set<Class<?>> initializedAnnotationsSet = Collections.synchronizedSet(new HashSet<>());

    private Properties properties;

    public BeansService() {
        this.scanner = new BeansScanningService(interceptor);
        this.store = new BeansStore(scanner);
        this.injector = new BeansInjectionService(store, scanner);
        this.annotationsAwaits = new BeansAnnotationsAwaitService(this, store);
    }

    /**
     * Сохранение экземляров внутренних сервисов
     * реализации Dependency Injection в саму себя же
     * для более гибкого их применения в рамках всего проекта.
     */
    private void bindThis() {
        bind(this);
        bind(store);
        bind(scanner);
        bind(injector);
        bind(annotationsAwaits);
        bind(interceptor);
    }

    /**
     * Проверить маркировки на указанной аннотации как
     * уже проинициализированной при помощи процессора
     * аннотаций.
     */
    public boolean isAnnotationsInitialized(Class<? extends Annotation>... annotationTypes) {
        return Stream.of(annotationTypes).allMatch(initializedAnnotationsSet::contains);
    }

    /**
     * Инициализация проекта и системы Dependency Injection
     * по указанной проперти-конфигурации.
     *
     * @param properties - проперти-конфигурация Dependency Injection.
     */
    public void start(Properties properties) {
        this.properties = properties;

        log.info("BeansService.start -> begin;");

        bindThis();

        initBeanFactories();
        processTypeAnnotationProcessors();

        log.info("BeansService.start -> end;");
    }

    /**
     * Инициализировать фабрики и провайдеры фабрик
     * бинов в системе с автоматической их инжекцией.
     */
    private void initBeanFactories() {
        log.info("Initialize beans factories & providers...");

        String defaultBeanFactory = properties.getProperty(PROPERTY_DEFAULT_BEAN_FACTORY);

        BeanFactoryProviders.DEFAULT = Optional.ofNullable(defaultBeanFactory)
                .map(BeanFactoryProviders::valueOf)
                        .orElse(BeanFactoryProviders.CONSTRUCTOR);

        for (BeanFactoryProviders provider : BeanFactoryProviders.values()) {
            inject(provider.getImpl().get());
        }
    }

    /**
     * Обрабатываем типовые процессоры аннотаций:
     * Сканируем проект, ищем все доступные процессоры, затем конфигурируем
     * и сканируем по полученной конфигурации проект еще раз на поиск бинов,
     * подходящих под параметры процессора. Следующим шагом проводим дополнительную
     * верификацию обнаруженного бина и передаем его в обработку процессору аннотации.
     */
    private void processTypeAnnotationProcessors() {
        log.info("Processing all type annotations processors...");
        List<TypeAnnotationProcessor<?>> typeAnnotationProcessors = scanner.scanTypeAnnotationProcessors();

        log.info("Founded §3{} §rtype annotations processors", typeAnnotationProcessors.size());

        for (TypeAnnotationProcessor<?> processor : typeAnnotationProcessors) {
            processTypeAnnotationProcessor(processor);
        }
    }

    /**
     * Вызывать обработку типового процессора аннотаций.
     * @param processor - процессор аннотаций.
     */
    public void processTypeAnnotationProcessor(TypeAnnotationProcessor<?> processor) {
        inject(processor);

        @AllArgsConstructor
        class AnnotationProcessorHandler<V extends Annotation> {
            private TypeAnnotationProcessor<V> processor;

            public void handle() {
                AnnotationProcessorConfig<V> config = processor.configure(properties);
                Class<V> annotationType = config.getAnnotationType();

                if (annotationType == null) {
                    return;
                }

                log.info("Processing type annotation processor of §2@{}", annotationType.getName());

                initializedAnnotationsSet.add(annotationType);
                scanner.scanBeans(config).forEach(bean -> processBean(config, bean));

                annotationsAwaits.flushQueue(annotationType);
            }

            public void processBean(AnnotationProcessorConfig<V> config, Bean bean) {
                AnnotationVerificationContext<V> verification = new AnnotationVerificationContext<>(
                        config.getAnnotationType(), bean, config);

                AnnotationVerificationResult result = processor.verify(verification);

                if (result.isSuccess()) {
                    bean.getProperties().setProperty(TypeAnnotationProcessor.BEAN_ANNOTATION_TYPE_PROPERTY, config.getAnnotationType().getName());
                    processor.processBean(BeansService.this, bean);
                }
            }
        }

        AnnotationProcessorHandler<?> processorHandler = new AnnotationProcessorHandler<>(processor);
        processorHandler.handle();
    }

    /**
     * Воспроизвести имитацию сохранения экземпляра
     * объекта как бина.
     *
     * @param type - класс бина.
     * @param object - инстанс бина.
     */
    public void fakeBind(Class<?> type, Object object) {
        fakeBind(scanner.createBean(type, object));
    }

    /**
     * Воспроизвести имитацию сохранения экземпляра
     * объекта как бина.
     * <p>
     * Класс бина в данном случае будет браться
     * напрямую из объекта, который мы указываем
     * в сигнатуру.
     *
     * @param object - инстанс бина.
     */
    public void fakeBind(Object object) {
        fakeBind(object.getClass(), object);
    }

    /**
     * Воспроизвести имитацию сохранения бина.
     * @param bean - бин.
     */
    public void fakeBind(Bean bean) {
        inject(bean.getRoot());

        // call @PostConstruct functions.
        List<BeanConstructFunction> postConstructFunctions = bean.getType().getPostConstructFunctions();
        postConstructFunctions.forEach(BeanConstructFunction::invoke);
    }

    /**
     * Сохранить экземпляр объекта как бин.
     *
     * @param type - класс бина.
     * @param object - инстанс бина.
     */
    public void bind(Class<?> type, Object object) {
        bind(scanner.createBean(type, object));
    }

    /**
     * Сохранить экземпляр объекта как бин.
     * <p>
     * Класс бина в данном случае будет браться
     * напрямую из объекта, который мы указываем
     * в сигнатуру.
     *
     * @param object - инстанс бина.
     */
    public void bind(Object object) {
        bind(object.getClass(), object);
    }

    /**
     * Сохранить бин.
     * @param bean - бин.
     */
    public void bind(Bean bean) {
        if (store.isStored(bean)) {
            return;
        }

        if (annotationsAwaits.needsAwaits(bean)) {
            annotationsAwaits.offer(bean);
            return;
        }

        inject(bean.getRoot());

        if (bean.getType().isAnnotated(EnableDecorators.class)) {
            bean = reconstructWithDecorators(bean);
        }

        // self-injection process
        for (BeanComponent component : bean.getType().getInjectSelfComponents()) {
            injector.tryInjectSelf(component);
        }

        store.store(bean);
        injector.flushInjectionQueue();

        log.info("Binding a bean of §6{}", bean.getType().getRoot().getName());

        // call @PostConstruct functions.
        List<BeanConstructFunction> postConstructFunctions = bean.getType().getPostConstructFunctions();
        postConstructFunctions.forEach(BeanConstructFunction::invoke);
    }

    /**
     * Пересобрать инстанс бина с проксированием
     * корневого экземпляра.
     *
     * @param bean - бин.
     */
    private Bean reconstructWithDecorators(Bean bean) {
        Object proxied = interceptor.createProxy(bean.getRoot(), new DecoratedObjectProxy());
        return new Bean(bean.getProperties(), bean.getId(), bean.getType(), proxied);
    }

    /**
     * Удалить сохраненный ранее бин из кеша
     * по его классу.
     *
     * @param beanType - класс бина.
     */
    public void unbind(Class<?> beanType) {
        unbind(store.find(beanType).orElse(null));
    }

    /**
     * Удалить сохраненный ранее бин из кеша
     * по его инстансу.
     * <p>
     * Класс бина в данном случае будет браться
     * напрямую из объекта, который мы указываем
     * в сигнатуру.
     *
     * @param object - инстанс бина.
     */
    public void unbind(Object object) {
        unbind(object.getClass());
    }

    /**
     * Удалить бин из кеша.
     * @param bean - бин.
     */
    public void unbind(Bean bean) {
        store.delete(bean);
        log.info("Unbinding a bean of §4{}", bean.getType().getRoot().getName());
    }

    /**
     * Проинициализировать компоненты объекта, которые
     * требуют инжекции бинов и других сущностей
     * реализованной технологии Dependency Injection.
     *
     * @param object - объект, который нуждается в инициализации.
     */
    public void inject(Object object) {
        injector.injectComponents(object);
    }
}
