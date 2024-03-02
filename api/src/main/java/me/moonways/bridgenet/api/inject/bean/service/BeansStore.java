package me.moonways.bridgenet.api.inject.bean.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanException;
import me.moonways.bridgenet.api.inject.bean.BeanType;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessor;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Контейнер бинов, выполняющий работу кеширования,
 * что помечает бины как забинденные, и дает возможность
 * технологии Dependency Injection осуществить
 * дальнейшие инжекции инстансов, типы которых
 * взаимосвязаны с бинами, что закешированы здесь.
 */
@RequiredArgsConstructor
public final class BeansStore {

    private final Map<UUID, Bean> store = Collections.synchronizedMap(new LinkedHashMap<>());

    private final BeansScanningService scanner;

    /**
     * Сохранить подготовленный бин для его дальнейших иженкций.
     * @param bean - бин.
     */
    public void store(Bean bean) {
        if (isStored(bean)) {
            throw new BeanException("Bean has already bind");
        }
        DuplicatedBean[] duplicates = findDuplicatesBy(bean);

        if (duplicates.length != 0) {
            if (!deleteDuplicates(bean, duplicates)) {
                //throw new BeanException("Bean has conflicts with duplicates on bind");
                return;
            }
        }

        store.put(bean.getId(), bean);
    }

    /**
     * Найти дубликаты ресурсов относительно целевого бина
     * во всех кешированных ранее бинах.
     *
     * @param target - целевой бин.
     */
    private DuplicatedBean[] findDuplicatesBy(Bean target) {
        Collection<Bean> totalBeans = store.values();
        List<DuplicatedBean> duplicatedBeanList = new ArrayList<>();

        for (Bean bean : totalBeans) {
            if (bean.isSimilar(target)) {

                List<Class<?>> targetResources = Arrays.asList(target.getType().getTotalResources());
                List<Class<?>> beanResources = Arrays.asList(bean.getType().getTotalResources());

                Class<?>[] duplicatedResources = targetResources.stream().filter(beanResources::contains)
                        .toArray(Class[]::new);

                if (duplicatedResources.length != 0) {
                    duplicatedBeanList.add(new DuplicatedBean(bean, duplicatedResources));
                }
            }
        }

        return duplicatedBeanList.toArray(new DuplicatedBean[0]);
    }

    /**
     * Удалить найденные дублирования относительно целевого бина.
     * @param target - целевой бин.
     * @param duplicates - найденные дубликаты.
     */
    private boolean deleteDuplicates(Bean target, DuplicatedBean[] duplicates) {
        for (DuplicatedBean duplicate : duplicates) {
            if (duplicate.getBean().isSimilarExact(target)) {
                return false;
            }

            restore(duplicate.getDuplicates(), duplicate.getBean());
        }
        return true;
    }

    /**
     * Перезаписываем дублированный бин без выявленных дубликатов.
     *
     * @param duplicates - выявленный список дубликатов.
     * @param bean - бин, который необходимо перезаписать.
     */
    private void restore(Class<?>[] duplicates, Bean bean) {
        BeanType beanType = bean.getType();

        Class<?>[] includeInterfaces = Stream.of(scanner.getInterfaces(beanType.getRoot()))
                .filter(interfaceType -> scanner.canInterfaceInclude(interfaceType) && !Arrays.asList(duplicates).contains(interfaceType))
                .toArray(Class<?>[]::new);

        AtomicReference<Bean> beanRef = new AtomicReference<>(bean);
        BeanType newType = new BeanType(beanRef, beanType.getRoot(), includeInterfaces);

        delete(bean);
        store(new Bean(bean.getProperties(), bean.getId(), newType, bean.getRoot()));
    }

    /**
     * Удалить подготовленный бин из кеша
     * и прекратить его использование в дальнейших инжекциях.
     *
     * @param bean - бин.
     */
    public void delete(Bean bean) {
        store.remove(bean.getId());
    }

    /**
     * Проверить на наличие сохраненного бина в кеше.
     * @param bean - бин.
     */
    public boolean isStored(Bean bean) {
        return store.containsKey(bean.getId()) || findExact(bean.getType().getRoot()).isPresent();
    }

    /**
     * Проверить на наличие сохраненного бина в кеше.
     * @param resourceType - тип ресурса бина.
     */
    public boolean isStored(Class<?> resourceType) {
        return store.values().stream()
                .anyMatch(bean -> bean.getType().isSimilar(resourceType));
    }

    /**
     * Поиск подготовленного бина по всевозможным его
     * типам, которые хоть как-то могут быть похожи на него.
     *
     * @param resourceType - тип ресурса бина.
     */
    public Optional<Bean> find(Class<?> resourceType) {
        return store.values().stream()
                .filter(bean -> bean.getType().isSimilar(resourceType))
                .findFirst();
    }

    /**
     * Поиск подготовленного бина с указанием
     * точного типа бина, который принадлежит бину
     * как основной.
     *
     * @param resourceType - тип ресурса бина.
     */
    public Optional<Bean> findExact(Class<?> resourceType) {
        return store.values().stream()
                .filter(bean -> bean.getType().isSimilarExact(resourceType))
                .findFirst();
    }

    /**
     * Поиск подготовленного бина с указанием
     * проперти секции, хранящаяся в локальных
     * проперти бина.
     *
     * @param propertyKey - ключ к секции локального проперти.
     * @param valuePredicate - проверка значения секции проперти.
     */
    public Stream<Bean> findByProperty(String propertyKey, Predicate<String> valuePredicate) {
        return store.values().stream()
                .filter(bean -> bean.getProperties().containsKey(propertyKey))
                .filter(bean -> valuePredicate.test(bean.getProperties().getProperty(propertyKey)));
    }

    /**
     * Поиск подготовленного бина с указанием
     * проперти секции, хранящаяся в локальных
     * проперти бина.
     *
     * @param propertyKey - ключ к секции локального проперти.
     */
    public Stream<Bean> findByPropertyContains(String propertyKey) {
        return findByProperty(propertyKey, (v) -> true);
    }

    /**
     * Поиск подготовленного бина с указанием
     * аннотации, по которой был обнаружен бин через
     * процессор аннотаций.
     *
     * @param annotationType - класс аннотации, по которой ищем
     */
    public Stream<Bean> findByAnnotation(Class<? extends Annotation> annotationType) {
        String annotationTypeName = annotationType.getName();
        return findByProperty(TypeAnnotationProcessor.BEAN_ANNOTATION_TYPE_PROPERTY, (v) -> v.equals(annotationTypeName));
    }

    @Getter
    @RequiredArgsConstructor
    private static class DuplicatedBean {

        private final Bean bean;
        private final Class<?>[] duplicates;
    }
}
