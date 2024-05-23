package me.moonways.bridgenet.api.inject.bean.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanComponent;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.processor.AnnotationProcessorConfig;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationContext;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationResult;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.assembly.OverridenProperty;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
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

    @Getter
    private final BeansStore store;
    @Getter
    private final BeansScanningService scanner;
    private final BeansInjectionService injector;
    @Getter
    private final BeansAnnotationsAwaitService annotationsAwaits;

    private final AnnotationInterceptor interceptor = new AnnotationInterceptor();

    private final Set<Class<?>> initializedAnnotationsSet = Collections.synchronizedSet(new HashSet<>());
    private final Map<Class<?>, Consumer<Object>> onBindingsMap = Collections.synchronizedMap(new HashMap<>());

    public BeansService() {
        this.scanner = new BeansScanningService();
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

        // other internal Bridgenet module dependencies.
        onBinding(ResourcesAssembly.class, ResourcesAssembly::overrideSystemProperties);

        bind(new ResourcesAssembly());
        bind(new BridgenetDataLogger());
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
     * Инициализация проекта и системы Dependency Injection.
     */
    public void fakeStart() {
        log.info("BeansService.fakeStart -> begin;");

        bindThis();
        initBeanFactories();

        log.info("BeansService.fakeStart -> end;");
    }

    /**
     * Инициализация проекта и системы Dependency Injection.
     */
    public void start() {
        log.info("BeansService.start -> begin;");

        bindThis();
        initBeanFactories();

        scanAllAnnotationProcessors();

        log.info("BeansService.start -> end;");
    }

    /**
     * Инициализировать фабрики и провайдеры фабрик
     * бинов в системе с автоматической их инжекцией.
     */
    private void initBeanFactories() {
        log.info("Initialize beans factories & providers...");

        BeanFactoryProviders.DEFAULT = Optional.ofNullable(OverridenProperty.BEANS_FACTORY_DEFAULT.get())
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
    public void scanAnnotationProcessors(List<TypeAnnotationProcessor<?>> inboundProcessors) {
        log.info("Processing inbound list of TypeAnnotationProcessor`s...");
        log.info("Founded §3{} §rannotation-processors", inboundProcessors.size());

        for (TypeAnnotationProcessor<?> processor : inboundProcessors) {
            processTypeAnnotationProcessor(processor);
        }
    }

    /**
     * Обрабатываем типовые процессоры аннотаций:
     * Сканируем проект, ищем все доступные процессоры, затем конфигурируем
     * и сканируем по полученной конфигурации проект еще раз на поиск бинов,
     * подходящих под параметры процессора. Следующим шагом проводим дополнительную
     * верификацию обнаруженного бина и передаем его в обработку процессору аннотации.
     */
    private void scanAllAnnotationProcessors() {
        scanAnnotationProcessors(scanner.scanAnnotationProcessors());
    }

    /**
     * Вызывать обработку типового процессора аннотаций.
     *
     * @param processor - процессор аннотаций.
     */
    public void processTypeAnnotationProcessor(TypeAnnotationProcessor<?> processor) {
        inject(processor);

        @AllArgsConstructor
        class AnnotationProcessorHandler<V extends Annotation> {
            private TypeAnnotationProcessor<V> processor;

            public void handle() {
                AnnotationProcessorConfig<V> config = processor.configure();
                Class<V> annotationType = config.getAnnotationType();

                if (annotationType == null) {
                    return;
                }

                log.info("Processing TypeAnnotationProcessor implement - §2@{}", annotationType.getName());

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
     * @param type   - класс бина.
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
     *
     * @param bean - бин.
     */
    public void fakeBind(Bean bean) {
        inject(bean.getRoot());

        // call @PostConstruct functions.
        List<BeanMethod> postConstructFunctions = bean.getType().getPostConstructFunctions();
        postConstructFunctions.forEach(BeanMethod::invoke);
    }

    /**
     * Сохранить экземпляр объекта как бин.
     *
     * @param type   - класс бина.
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
     *
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
        List<BeanMethod> postConstructFunctions = bean.getType().getPostConstructFunctions();
        postConstructFunctions.forEach(BeanMethod::invoke);

        // call post-binding consumers.
        Bean finalBean = bean;
        Optional.ofNullable(onBindingsMap.remove(bean.getType().getRoot()))
                .ifPresent(consumer -> consumer.accept(finalBean.getRoot()));
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
     *
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

    /**
     * Получить проинициализированный бин по одному
     * из его основных классов.
     *
     * @param cls - регистрационный класс бина.
     */
    public Optional<Bean> get(Class<?> cls) {
        return store.find(cls);
    }

    /**
     * Получить проинициализированный бин по одному
     * из его основных классов.
     *
     * @param cls - регистрационный класс бина.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getInstance(Class<T> cls) {
        return get(cls).map(bean -> (T) bean.getRoot());
    }

    /**
     * Потребляет указанный процесс, при определении забиндиного типа
     * бина.
     *
     * @param type - тип бина, который ожидается для этого процесса.
     */
    public <T> void onBinding(Class<T> type, Consumer<T> consumer) {
        onBindingsMap.put(type, (Consumer<Object>) consumer);
    }
}
