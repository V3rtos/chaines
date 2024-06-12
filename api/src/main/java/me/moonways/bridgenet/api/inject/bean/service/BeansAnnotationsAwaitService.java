package me.moonways.bridgenet.api.inject.bean.service;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanComponent;
import me.moonways.bridgenet.api.inject.bean.BeanException;
import me.moonways.bridgenet.api.inject.bean.BeanType;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorAdapter;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorResult;
import me.moonways.bridgenet.api.inject.processor.persistence.GetTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.persistence.WaitTypeAnnotationProcessor;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class BeansAnnotationsAwaitService {

    private final Map<Bean, Class<? extends Annotation>[]> annotationsQueueMap
            = Collections.synchronizedMap(new HashMap<>());

    private final BeansService service;
    private final BeansStore store;

    /**
     * Получить класс ожидаемой аннотации бина для
     * инициализации процессора аннотаций. Если аннотации нет -
     * значение будет null.
     *
     * @param bean - бин, в рамках которого ищем аннотацию.
     */
    public Class<? extends Annotation>[] getAwaitsAnnotationsTypes(Bean bean) {
        return bean.getType().getAnnotation(WaitTypeAnnotationProcessor.class)
                .map(WaitTypeAnnotationProcessor::value)
                .orElse(null);
    }

    /**
     * Проверить на необходимость вставания указанного
     * бина в очередь ожидания инициализации какого-либо
     * процессора аннотаций.
     *
     * @param bean - бин, который необходимо проверить.
     */
    public boolean needsAwaits(Bean bean) {
        Class<? extends Annotation>[] awaitsAnnotationType = getAwaitsAnnotationsTypes(bean);
        if (awaitsAnnotationType == null || awaitsAnnotationType.length == 0) {
            return false;
        }

        boolean isStillNotInitialized = !service.isAnnotationsInitialized(awaitsAnnotationType);
        if (!isStillNotInitialized) {
            initAnnotationProcessorResult(bean);
        }
        return isStillNotInitialized;
    }

    /**
     * Сохранить бин в очередь ожидания инициализации
     * процессора аннотации, которая ему необходима для
     * дальнейших взаимодействий.
     *
     * @param bean - бин, который встает в ожидание процессора аннотаций.
     */
    public void offer(Bean bean) {
        if (annotationsQueueMap.containsKey(bean)) {
            throw new BeanException("Bean " + bean.getFullClassName() + " has already await anyone annotation-processor");
        }
        annotationsQueueMap.put(bean, getAwaitsAnnotationsTypes(bean));
    }

    /**
     * Очистка очереди вызывается только тогда, когда
     * инициализировался один из процессоров аннотаций,
     * и если указанный тип аннотации совпадает с одним
     * из бинов в очереди - воспроизводится кеширование
     * этого бина как забиндиного.
     *
     * @param annotationType - класс аннотации, который был только что
     *                       проинициализирован одним из процессоров аннотаций.
     */
    public void flushQueue(Class<? extends Annotation> annotationType) {
        Set<Bean> awaitBeans = new HashSet<>(annotationsQueueMap.keySet());

        for (Bean bean : awaitBeans) {
            List<Class<?>> annotationsTypes = Arrays.asList(annotationsQueueMap.getOrDefault(bean, new Class[0]));

            if (annotationsTypes.contains(annotationType)) {
                annotationsQueueMap.remove(bean);
                service.bind(bean);
            }
        }
    }

    /**
     * Проинициализировать результаты процессинга
     * ожидаемой аннотации.
     *
     * @param bean - бин, в котором инициализируем результат.
     */
    private void initAnnotationProcessorResult(Bean bean) {
        Class<? extends Annotation>[] awaitsAnnotationType = getAwaitsAnnotationsTypes(bean);
        BeanType beanType = bean.getType();

        List<BeanComponent> resultComponents = beanType.getAllComponents()
                .stream()
                .filter(component -> component.isAnnotated(GetTypeAnnotationProcessor.class))
                .collect(Collectors.toList());

        if (!resultComponents.isEmpty()) {
            List<Bean> proceedBeans =
                    Arrays.stream(awaitsAnnotationType)
                            .flatMap(store::findByAnnotation)
                            .collect(Collectors.toList());

            for (BeanComponent component : resultComponents) {
                Class<?> type = component.getType();

                if (!type.equals(TypeAnnotationProcessorResult.class)) {
                    throw new BeanException("Component " + component.getRoot() + " must be return type of TypeAnnotationProcessorResult");
                }

                Class<?> genericType = TypeAnnotationProcessorAdapter.getGenericType(0, type);
                TypeAnnotationProcessorResult<?> value = new TypeAnnotationProcessorResult<>(awaitsAnnotationType, genericType, proceedBeans);

                component.setValue(value);
            }
        }
    }
}
