package me.moonways.bridgenet.api.inject.processor;

import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationContext;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationResult;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * Внутренний процессор аннотаций относительно бинов,
 * которые помогают их обнаружить автоматизированным
 * процессом среди всех ресурсов и пакейджей одного проекта.
 *
 * @param <V> - Тип аннотации, процессинг которого реализуется здесь.
 */
public interface TypeAnnotationProcessor<V extends Annotation> {
    String BEAN_ANNOTATION_TYPE_PROPERTY = "beans.processor.target";

    /**
     * Конфигурация процесса сканирование ресурсов
     * и пакейджей на поиск нужной аннотации.
     *
     * @return - подготовленная конфигурация сканнера.
     */
    AnnotationProcessorConfig<V> configure();

    /**
     * Процесс дополнительной верификации входящих
     * бинов, которые смог обнаружить сканнер по параметрам
     * из конфигурации сканнера.
     *
     * @param verification - контекст, необходимый для верификации.
     * @return - результат верификации.
     */
    AnnotationVerificationResult verify(AnnotationVerificationContext<V> verification);

    /**
     * Процесс применения обнаруженного бина в системе.
     *
     * @param service - сервис управления бинами.
     * @param bean - обнаруженный бин.
     */
    void processBean(BeansService service, Bean bean);
}
