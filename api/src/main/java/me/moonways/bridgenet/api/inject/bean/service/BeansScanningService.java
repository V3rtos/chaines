package me.moonways.bridgenet.api.inject.bean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.IgnoredRegistry;
import me.moonways.bridgenet.api.inject.bean.*;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProvider;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.processor.AnnotationProcessorConfig;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.def.JustBindTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.persistence.UseTypeAnnotationProcessor;
import me.moonways.bridgenet.api.util.pair.Pair;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class BeansScanningService {
    private final Set<Class<?>> allResourcesSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * Сканировать все существующие ресурсы проекта
     * для дальнейшей оптимизации поиска конкретных ресурсов.
     */
    private synchronized void scanAllResourcesAsClasses() {
        if (!allResourcesSet.isEmpty()) {
            return;
        }

        ConfigurationBuilder configuration = ConfigurationBuilder.build(new SubTypesScanner(false));
        configuration.setParallel(true);
        configuration.setExpandSuperTypes(false);

        Reflections reflections = new Reflections(configuration);

        Set<Class<?>> classSet = reflections.getSubTypesOf(Object.class);

        log.debug("Scanning result is §e{} §rresources", classSet.size());
        allResourcesSet.addAll(classSet);
    }

    /**
     * Получить список ресурсов из общего, которые наследуют
     * указанный тип класса как superclass.
     *
     * @param superclass - класс наследник.
     */
    private synchronized Stream<Class<?>> getResourcesBySuperclass(Class<?> superclass) {
        scanAllResourcesAsClasses();
        return allResourcesSet.stream().parallel().filter(superclass::isAssignableFrom);
    }

    /**
     * Получить список ресурсов из общего, которые наследуют
     * указанный тип класса как superclass.
     *
     * @param superclass - класс наследник.
     */
    private synchronized <T> Stream<Class<T>> getResourcesBySuperclassCast(Class<T> superclass) {
        return getResourcesBySuperclass(superclass).map(aClass -> (Class<T>) aClass);
    }

    /**
     * Получить список ресурсов из общего, которые аннотируют
     * указанный тип аннотации
     *
     * @param annotationType - класс аннотации.
     */
    private synchronized Stream<Class<?>> getResourcesAnnotatedWith(Class<? extends Annotation> annotationType) {
        scanAllResourcesAsClasses();
        return allResourcesSet.stream().parallel().filter(resourceClass -> resourceClass.isAnnotationPresent(annotationType));
    }

    /**
     * Получить список ресурсов из общего, которые находятся
     * в одном из указанных пакейджей.
     *
     * @param packageNamesArray - имена пакейджей
     */
    private synchronized Stream<Class<?>> getResourcesFromPackage(String... packageNamesArray) {
        if (packageNamesArray.length == 0) {
            return Stream.of();
        }

        scanAllResourcesAsClasses();
        return allResourcesSet.stream()
                .parallel()
                .filter(resourceClass -> {
                    String resourcePackageName = resourceClass.getPackage().getName();

                    for (String packageName : packageNamesArray) {
                        if (resourcePackageName.startsWith(packageName)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    /**
     * Воспроизвести сканирование проекта для поиска
     * и дальнейшей инициализации и применения объектов,
     * которые наследуют указанный тип класса.
     *
     * @param superclass - класс наследник, по которому собираемся искать объекты.
     */
    public Set<Bean> scanBeansBySuperclass(@NotNull Class<?> superclass) {
        return getResourcesBySuperclass(superclass).map(this::createBean).collect(Collectors.toSet());
    }

    /**
     * Воспроизвести сканирование проекта для поиска
     * и дальнейшей инициализации и применения объектов,
     * которые наследуют указанный тип класса.
     *
     * @param superclass - класс наследник, по которому собираемся искать объекты.
     */
    public Set<Class<?>> scanBySuperclass(@NotNull Class<?> superclass) {
        return getResourcesBySuperclass(superclass).collect(Collectors.toSet());
    }

    /**
     * Воспроизвести сканирование проекта для поиска
     * и дальнейшей инициализации и применения процессоров
     * аннотаций классов, относящихся к технологии Dependency Injection.
     *
     * @param packageNames - имя пакейджа, в котором искать бины.
     */
    public List<Bean> scanByPackage(String... packageNames) {
        return getResourcesFromPackage(packageNames)
                .filter(resourceClass -> resourceClass.isAnnotationPresent(Autobind.class))
                .filter(probablyClass -> Arrays.stream(probablyClass.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(UseTypeAnnotationProcessor.class)))
                .map(this::createBean)
                .collect(Collectors.toList());
    }

    /**
     * Воспроизвести сканирование проекта для поиска
     * и дальнейшей инициализации и применения процессоров
     * аннотаций классов, относящихся к технологии Dependency Injection.
     */
    public List<TypeAnnotationProcessor<?>> scanAnnotationProcessors() {
        Set<Class<? extends TypeAnnotationProcessor>> result = getResourcesBySuperclassCast(TypeAnnotationProcessor.class).collect(Collectors.toSet());
        Set<Class<?>> usageAnnotationsTypes = getResourcesAnnotatedWith(UseTypeAnnotationProcessor.class).collect(Collectors.toSet());

        List<TypeAnnotationProcessor<?>> typeAnnotationProcessors = toTypeAnnotationsProcessors(result);

        for (Class<?> annotationType : usageAnnotationsTypes) {
            if (Annotation.class.isAssignableFrom(annotationType)) {
                typeAnnotationProcessors.add(new JustBindTypeAnnotationProcessor((Class<? extends Annotation>) annotationType));
            }
        }

        return typeAnnotationProcessors;
    }

    /**
     * Преобразовать результат сканирования в список
     * процессоров для аннотаций.
     *
     * @param scannerResultSet - результат сканирования проекта.
     */
    private List<TypeAnnotationProcessor<?>> toTypeAnnotationsProcessors(Set<Class<? extends TypeAnnotationProcessor>> scannerResultSet) {
        List<TypeAnnotationProcessor<?>> result = new ArrayList<>();

        BeanFactoryProvider factoryProvider = BeanFactoryProviders.UNSAFE.getImpl();
        BeanFactory beanFactory = factoryProvider.get();

        for (Class<? extends TypeAnnotationProcessor> type : scannerResultSet) {
            if (type.isInterface() || (type.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT) {
                continue;
            }

            TypeAnnotationProcessor typeAnnotationProcessor = beanFactory.create(type);
            result.add(typeAnnotationProcessor);
        }

        return result;
    }

    /**
     * Воспроизвести сканирование проекта.
     * Внутри процесса создается конфигурация фильтра вложений
     * результата и конфигурация самого сканирования, после которой
     * все аннотированные (помеченные аннотацией) ресурсы помещаются
     * в общий результат, и идут на преобразование в бины.
     *
     * @param config - конфигурация процессора аннотаций
     */
    public List<Bean> scanBeans(AnnotationProcessorConfig<?> config) {
        Set<Class<?>> result = getResourcesFromPackage(config.getPackages().toArray(new String[0]))
                .filter(resourceClass -> resourceClass.isAnnotationPresent(config.getAnnotationType()))
                .collect(Collectors.toSet());

        return toBeansList(result);
    }

    /**
     * Преобразования результата сканирования ресурсов
     * пакейджа в готовые бины, содержащие необходимую
     * информацию для дальнейших процессов системы.
     *
     * @param scannerResultSet - результат сканирования в виде java.util.Set
     */
    private List<Bean> toBeansList(Set<Class<?>> scannerResultSet) {
        List<Bean> result = new ArrayList<>();

        for (Class<?> resourceType : scannerResultSet) {
            result.add(createBean(resourceType));
        }
        return sort(result);
    }

    /**
     * Создать бин по подобию образа полученного
     * класса ресурса из процесса сканирования.
     *
     * @param resourceType - полученный класс ресурса.
     */
    public Bean createBean(Class<?> resourceType) {
        return createBean(resourceType, this::createRoot);
    }

    /**
     * Создать бин по подобию образа полученного
     * класса ресурса из процесса сканирования.
     *
     * @param resourceType - полученный класс ресурса.
     * @param root         - инстанс бина.
     */
    public Bean createBean(Class<?> resourceType, Object root) {
        return createBean(resourceType, ((type) -> root));
    }

    /**
     * Создать бин по подобию образа полученного
     * класса ресурса из процесса сканирования.
     *
     * @param resourceType   - полученный класс ресурса.
     * @param functionOfRoot - функция получения инстанса бина.
     */
    private Bean createBean(Class<?> resourceType, Function<BeanType, Object> functionOfRoot) {
        AtomicReference<Bean> beanRef = new AtomicReference<>();

        BeanType beanType = new BeanType(beanRef, resourceType, getInterfaces(resourceType));
        Bean bean = new Bean(new Properties(), UUID.randomUUID(), beanType, functionOfRoot.apply(beanType));

        beanRef.set(bean);

        processPreConstructs(bean);
        return bean;
    }

    /**
     * Исполнить предварительные функции бина
     * перед его конструированием.
     *
     * @param bean - бин.
     */
    private void processPreConstructs(Bean bean) {
        bean.getType().getPreConstructFunctions()
                .forEach(BeanMethod::invoke);
    }

    /**
     * Создание основного инстанса бина
     *
     * @param beanType - тип бина.
     */
    public Object createRoot(BeanType beanType) {
        BeanFactoryProvider factoryProvider = BeanFactoryProviders.DEFAULT.getImpl();
        Class<?> rootType = beanType.getRoot();

        if (beanType.isAuto()) {
            factoryProvider = beanType.getAutoFactoryProvider();
        }

        return factoryProvider.get().create(rootType);
    }

    /**
     * Получение интерфейсов исходя из основного типа ресурса.
     * Здесь происходит отбрасывание общеиспользованных и не неужных
     * для кеширования интерфейсов.
     *
     * @param resourceType - основной тип ресурса.
     */
    public Class<?>[] getInterfaces(Class<?> resourceType) {
        return Stream.of(resourceType.getInterfaces()).filter(this::canInterfaceInclude)
                .toArray(Class<?>[]::new);
    }

    /**
     * Проверяем, может ли данный интерфейс войти в результат
     * типов бина, которые будут кешированы.
     *
     * @param interfaceType - проверяемый тип интерфейса.
     */
    public boolean canInterfaceInclude(Class<?> interfaceType) {
        if (interfaceType.isAnnotationPresent(IgnoredRegistry.class)) {
            return false;
        }

        String packageName = interfaceType.getPackage().getName();
        return !packageName.startsWith("java.") && !packageName.startsWith("com.sun.");
    }

    /**
     * Сортировка бинов для дальнейшей инжекции.
     *
     * @param beans - список всех готовых бинов.
     */
    private List<Bean> sort(List<Bean> beans) {
        Pair<Class<?>, Class<?>> recursionPair = searchRecursiveInjections(beans);
        if (recursionPair != null) {
            throw new BeanException("Recursive beans injection in " + recursionPair);
        }

        List<Class<?>> resources = toResources(beans);

        int sortedBeans = 0;
        for (Bean bean : new ArrayList<>(beans) /* fix CME */) {
            BeanType beanType = bean.getType();

            for (BeanComponent component : beanType.getInjectComponents()) {

                int rootIndex = resources.indexOf(beanType.getRoot());
                int componentIndex = resources.indexOf(component.getType());

                if (rootIndex < componentIndex) {

                    // rewrite bean to end of list.
                    beans.remove(bean);
                    beans.add(bean);

                    sortedBeans++;
                }
            }
        }
        return sortedBeans > 0 ? sort(beans) : beans;
    }

    /**
     * Данная функция выполняет поиск конфликтов под
     * теговым названием "рекурсивная инжекция", и возвращает
     * первый найденный конфликт между двумя классами,
     * которые относятся к "рекурсивной инжекции".
     *
     * @param beans - список бинов, в которых производится поиск.
     */
    private Pair<Class<?>, Class<?>> searchRecursiveInjections(List<Bean> beans) {
        for (Bean bean : beans) {
            BeanType beanType = bean.getType();

            for (BeanComponent component : beanType.getInjectComponents()) {
                Bean componentBean = beans.stream().filter(other -> other.getType().isSimilar(component.getType()))
                        .findFirst()
                        .orElse(null);

                // skip self-injection
                if (componentBean != null && !componentBean.isSimilar(bean)) {

                    for (BeanComponent componentInternalComponent : componentBean.getType().getInjectComponents()) {

                        if (beanType.isSimilar(componentInternalComponent.getType())) {
                            return Pair.immutable(beanType.getRoot(), component.getType());
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Преобразовать список подготовленных бинов
     * в список ресурсов, относящихся к бинам.
     *
     * @param beans - список подготовленных бинов.
     */
    private List<Class<?>> toResources(List<Bean> beans) {
        List<Class<?>> resources = new ArrayList<>();

        for (Bean bean : beans) {
            BeanType beanType = bean.getType();

            resources.add(beanType.getRoot());
            resources.addAll(Arrays.asList(beanType.getInterfaces()));
        }

        return resources;
    }
}
