package me.moonways.bridgenet.api.inject.bean.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Lazy;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanComponent;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;
import me.moonways.bridgenet.api.inject.bean.factory.FactoryType;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.inject.processor.AnnotationProcessorConfig;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationContext;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationResult;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.assembly.OverridenProperty;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
    private static final ExecutorService executorService
            = Threads.newSingleThreadExecutor();

    @Getter
    private final BeansStore store;
    @Getter
    private final BeansScanningService scanner;
    private final BeansInjectionService injector;
    @Getter
    private final BeansAnnotationsAwaitService annotationsAwaits;

    private final AnnotationInterceptor interceptor = new AnnotationInterceptor();

    private final Set<Class<?>> initializedAnnotationsSet = Collections.synchronizedSet(new HashSet<>());
    private final Map<Class<?>, Consumer<Object>> beansOnBindingsMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<Class<?>, Runnable> processorsOnBindingsMap = Collections.synchronizedMap(new HashMap<>());

    private final Lazy<DecoratedObjectProxy> decoratorsProxyLazy =
            Lazy.ofFactory(DecoratedObjectProxy.class);

    public BeansService() {
        this.scanner = new BeansScanningService();
        this.store = new BeansStore(scanner);
        this.injector = new BeansInjectionService(store, scanner);
        this.annotationsAwaits = new BeansAnnotationsAwaitService(this, store);
    }

    /**
     * Сохранение экземпляров внутренних сервисов
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

        bind(new ResourcesAssembly());
        bind(new BridgenetDataLogger());
    }

    /**
     * Инициализация проекта и системы Dependency Injection.
     */
    public void fakeStart() {
        log.debug("BeansService.fakeStart -> begin;");
        CompletableFuture.runAsync(() -> {

            bindThis();
            initBeanFactories();

            log.debug("BeansService.fakeStart -> end;");

        }, executorService).join();
    }

    /**
     * Инициализация проекта и системы Dependency Injection.
     */
    public void start() {
        log.debug("BeansService.start -> begin;");
        CompletableFuture.runAsync(() -> {

            bindThis();
            initBeanFactories();

            scanAllAnnotationProcessors();

            log.debug("BeansService.start -> end;");

        }, executorService).join();
    }

    /**
     * Инициализировать фабрики и провайдеры фабрик
     * бинов в системе с автоматической их инжекцией.
     */
    private void initBeanFactories() {
        log.debug("Initialize beans factories & providers...");

        FactoryType.DEFAULT = Optional.ofNullable(OverridenProperty.BEANS_FACTORY_DEFAULT.get())
                .map(FactoryType::valueOf)
                .orElse(FactoryType.CONSTRUCTOR);

        for (FactoryType provider : FactoryType.values()) {
            inject(provider.get());
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
        log.debug("Processing inbound list of TypeAnnotationProcessor`s...");
        log.debug("Founded §3{} §rannotation-processors", inboundProcessors.size());

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

        AnnotationProcessorHandler<?> processorHandler = new AnnotationProcessorHandler<>(processor);
        processorHandler.handle();
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
     * Воспроизвести имитацию сохранения экземпляра
     * объекта как бина.
     *
     * @param type   - класс бина.
     * @param object - инстанс бина.
     */
    public synchronized void fakeBind(Class<?> type, Object object) {
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
     * @param object - instance бина.
     */
    public synchronized void fakeBind(Object object) {
        fakeBind(object.getClass(), object);
    }

    /**
     * Воспроизвести имитацию сохранения бина.
     *
     * @param bean - бин.
     */
    public synchronized void fakeBind(Bean bean) {
        inject(bean.getRoot());

        // call @PostConstruct functions.
        List<BeanMethod> postConstructFunctions = bean.getType().getPostConstructFunctions();
        postConstructFunctions.forEach(BeanMethod::invoke);
    }

    /**
     * Сохранить экземпляр объекта как бин.
     *
     * @param type   - класс бина.
     * @param object - instance бина.
     */
    public synchronized void bind(Class<?> type, Object object) {
        bind(scanner.createBean(type, object));
    }

    /**
     * Сохранить экземпляр объекта как бин.
     * <p>
     * Класс бина в данном случае будет браться
     * напрямую из объекта, который мы указываем
     * в сигнатуру.
     *
     * @param object - instance бина.
     */
    public synchronized void bind(Object object) {
        bind(object.getClass(), object);
    }

    /**
     * Сохранить бин.
     *
     * @param bean - бин.
     */
    public synchronized void bind(Bean bean) {
        if (annotationsAwaits.needsAwaits(bean)) {
            annotationsAwaits.offer(bean);
            return;
        }
        if (store.isStored(bean)) {
            return;
        }

        doBind(bean);
    }

    /**
     * Воспроизвести полноценный процесс бинда бина.
     *
     * @param bean - бин.
     */
    private void doBind(Bean bean) {
        inject(bean.getRoot());

        Class<?> rootClass = bean.getRoot().getClass();

        if (injector.isQueued(rootClass)) {
            injector.subscribeLeaveAtQueue(rootClass, this::doBind);
            return;
        }

        callPostConstructs(bean);
        tryOverrideDecorators(bean);

        // self-injection process
        for (BeanComponent component : bean.getType().getInjectSelfComponents()) {
            injector.injectSelf(component);
        }

        log.debug("Binding a bean of §6{}", bean.getType().getRoot().getName());

        justStore(bean);
        callPostBindingConsumers(bean);
    }

    /**
     * Преобразовать экземпляр в только что созданный бин.
     *
     * @param object - экземпляр, который преобразуем в бин.
     */
    public Bean createBean(Object object) {
        return scanner.createBean(object.getClass(), object);
    }

    /**
     * Преобразовать экземпляр в только что созданный бин.
     *
     * @param cls - класс, который берем в основу типизации бина.
     * @param object - экземпляр, который преобразуем в бин.
     */
    public Bean createBean(Class<?> cls, Object object) {
        return scanner.createBean(cls, object);
    }

    /**
     * Наложить на бин проксирование в случае, если
     * для него были включены декораторы аннотацией @EnableDecorators.
     *
     * @param bean - бин, который необходимо проверить.
     */
    public synchronized void tryOverrideDecorators(Bean bean) {
        if (bean.getType().isDecoratorsEnabled()) {
            enableDecorators(bean);
        }
    }

    /**
     * Пересобрать instance бина с проксированием
     * корневого экземпляра.
     *
     * @param bean - бин.
     */
    private void enableDecorators(Bean bean) {
        Object root = bean.getRoot();
        Object proxy = interceptor.createProxy(root, decoratorsProxyLazy.get());
        //inject(proxy);
        bean.setRoot(proxy);
    }

    /**
     * Вызвать сохраненные консумеры, ожидающие
     * бинда указанного бина.
     *
     * @param bean - бин, которого могут ожидать консумеры.
     */
    private void callPostBindingConsumers(Bean bean) {
        Object root = bean.getRoot();
        Optional.ofNullable(beansOnBindingsMap.remove(bean.getType().getRoot()))
                .ifPresent(consumer -> consumer.accept(root));

        for (Class<?> beanInterface : bean.getType().getInterfaces()) {
            Optional.ofNullable(beansOnBindingsMap.remove(beanInterface))
                    .ifPresent(consumer -> consumer.accept(root));
        }
    }

    /**
     * Удалить сохраненный ранее бин из кеша
     * по его классу.
     *
     * @param beanType - класс бина.
     */
    public synchronized void unbind(Class<?> beanType) {
        unbind(store.find(beanType).orElse(null));
    }

    /**
     * Удалить сохраненный ранее бин из кеша
     * по его instance.
     * <p>
     * Класс бина в данном случае будет браться
     * напрямую из объекта, который мы указываем
     * в сигнатуру.
     *
     * @param object - instance бина.
     */
    public synchronized void unbind(Object object) {
        unbind(object.getClass());
    }

    /**
     * Удалить бин из кеша.
     *
     * @param bean - бин.
     */
    public synchronized void unbind(Bean bean) {
        store.delete(bean);
        log.debug("Unbinding a bean of §4{}", bean.getType().getRoot().getName());
    }

    /**
     * Проинициализировать компоненты объекта, которые
     * требуют инжекции бинов и других сущностей
     * реализованной технологии Dependency Injection.
     *
     * @param object - объект, который нуждается в инициализации.
     */
    public synchronized void inject(Object object) {
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
     * Вызов функций, ожидающих выполнения сразу после
     * конструирования и инжекции бина.
     *
     * @param bean - бин
     */
    private void callPostConstructs(Bean bean) {
        if (!injector.isQueued(bean.getRoot().getClass())) {
            bean.getType()
                    .getPostConstructFunctions()
                    .forEach(BeanMethod::invoke);
        }
    }

    /**
     * Потребляет указанный процесс, при определении забиндиного
     * типа бина.
     *
     * @param type      - тип бина, который ожидается для этого процесса.
     * @param onBinding - процесс, вызываемый после биндинга.
     */
    @SuppressWarnings("unchecked")
    public <T> void subscribeOn(Class<T> type, Consumer<T> onBinding) {
        Optional<T> instanceOptional = getInstance(type);
        if (instanceOptional.isPresent()) {
            onBinding.accept(instanceOptional.get());
            return;
        }

        Consumer<Object> consumer = beansOnBindingsMap.get(type);
        Consumer<Object> cast = (Consumer<Object>) onBinding;

        if (consumer == null) {
            consumer = cast;
        } else {
            consumer = consumer.andThen(cast);
        }

        beansOnBindingsMap.put(type, consumer);
    }

    /**
     * Потребляет указанный процесс, при определении забиндиного
     * типа процессора аннотаций.
     *
     * @param type      - тип процессора аннотаций, который ожидается для этого процесса.
     * @param onBinding - процесс, вызываемый после биндинга.
     */
    public void subscribeAnnotated(Class<? extends Annotation> type, Runnable onBinding) {
        Runnable runnable = processorsOnBindingsMap.get(type);

        if (runnable == null) {
            runnable = onBinding;
        } else {
            Runnable finalRunnable = runnable;
            runnable = () -> {
                finalRunnable.run();
                onBinding.run();
            };
        }

        processorsOnBindingsMap.put(type, runnable);
    }

    /**
     * Воспроизвести обычную запись бина в кеш
     * без дополнительных инжекций и процессов.
     *
     * @param bean - бин.
     */
    public synchronized void justStore(Bean bean) {
        if (!store.isStored(bean)) {
            store.store(bean);
        }
        injector.touchQueue();
    }

    /**
     * Воспроизвести обычную запись бина в кеш
     * без дополнительных инжекций и процессов.
     *
     * @param bean - бин.
     */
    public synchronized void justStore(Object bean) {
        justStore(scanner.createBean(bean.getClass(), bean));
    }

    /**
     * Воспроизвести обычную запись бина в кеш,
     * дополнительно воспроизведя только процессы
     * конструкции объекта.
     *
     * @param bean - бин.
     */
    public synchronized void justStoreAndConstruct(Bean bean) {
        justStore(bean);
        callPostConstructs(bean);
    }

    /**
     * Воспроизвести обычную запись бина в кеш,
     * дополнительно воспроизведя только процессы
     * конструкции объекта.
     *
     * @param bean - бин.
     */
    public synchronized void justStoreAndConstruct(Object bean) {
        justStoreAndConstruct(scanner.createBean(bean.getClass(), bean));
    }

    /**
     * Класс AnnotationProcessorHandler обрабатывает аннотации и соответствующие
     * им бины, используя предоставленный процессор.
     *
     * @param <V> тип аннотации, с которой работает этот обработчик.
     */
    @RequiredArgsConstructor
    class AnnotationProcessorHandler<V extends Annotation> {
        private final TypeAnnotationProcessor<V> processor;

        /**
         * Выполняет основную обработку аннотаций и связанных с ними бинов.
         * Конфигурирует процессор, обрабатывает найденные бины и вызывает постобработку.
         */
        public void handle() {
            AnnotationProcessorConfig<V> config = processor.configure();
            Class<V> annotationType = config.getAnnotationType();

            if (annotationType == null) {
                return;
            }

            handleFoundedBeans(config);
            callPostBindings(annotationType);
        }

        /**
         * Обрабатывает отдельный бин в соответствии с конфигурацией процессора.
         *
         * @param config конфигурация процессора аннотаций.
         * @param bean бин, который нужно обработать.
         */
        private void processBean(AnnotationProcessorConfig<V> config, Bean bean) {
            AnnotationVerificationContext<V> verification = new AnnotationVerificationContext<>(
                    config.getAnnotationType(), bean, config);

            AnnotationVerificationResult result = processor.verify(verification);

            if (result.isSuccess()) {
                bean.getProperties().setProperty(TypeAnnotationProcessor.BEAN_ANNOTATION_TYPE_PROPERTY, config.getAnnotationType().getName());

                if (bean.getType().isAuto()) {
                    bind(bean);
                } else {
                    processor.processBean(BeansService.this, bean);
                }
            }
        }

        /**
         * Обрабатывает все найденные бины, аннотированные указанной аннотацией.
         *
         * @param config конфигурация процессора аннотаций.
         */
        private void handleFoundedBeans(AnnotationProcessorConfig<V> config) {
            Class<V> annotationType = config.getAnnotationType();
            List<Bean> beans = scanner.scanBeans(config);

            log.debug("Founded {} beans annotated by §7@{}§r: {}", beans.size(), annotationType.getSimpleName(), beans);

            initializedAnnotationsSet.add(annotationType);

            beans.forEach(bean -> processBean(config, bean));

            annotationsAwaits.flushQueue(annotationType);
            injector.touchQueue();
        }

        /**
         * Вызывает постобработку для аннотаций.
         *
         * @param annotationType тип аннотации, для которой вызывается постобработка.
         */
        private void callPostBindings(Class<?> annotationType) {
            Runnable onBinding = processorsOnBindingsMap.remove(annotationType);
            if (onBinding != null) {
                onBinding.run();
            }
        }
    }
}
