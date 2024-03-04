package me.moonways.bridgenet.api.modern_x2_command.annotation;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorAdapter;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateResult;

import java.lang.annotation.Annotation;
import java.util.*;

@Autobind
public class AnnotationCommandService {

    private final Map<Class<? extends Annotation>, AnnotationCommandHandler<?>> handlers = new HashMap<>();

    @Inject
    private AnnotationCommandValidateManagement verifyManagement;

    @Inject
    private BeansScanningService scanningService;

    @PostConstruct
    public void inject() {
        Set<Bean> beans = scanningService.scanBeansBySuperclass(AnnotationCommandHandler.class);

        for (Bean bean : beans) {
            Class<? extends Annotation> annCls = TypeAnnotationProcessorAdapter.getGenericType(0, bean.getType().getRoot());

            handlers.put(annCls, (AnnotationCommandHandler<?>) bean.getRoot());
        }
    }

    public void removeAll() {
        handlers.clear();
    }

    public List<AnnotationCommandValidateResult> validateAll(AnnotationNativeExecutionContext context) {
        List<AnnotationCommandValidateResult> results = new ArrayList<>();

        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!context.getCommand().getBean().getType().isAnnotated(cls))
                continue;

            results.add(validate(context, cls));
        }

        return results;
    }

    private <T extends Annotation> AnnotationCommandValidateResult validate(AnnotationNativeExecutionContext context, Class<T> cls) {
        T annotation = context.getCommand().getBeanMethod().getAnnotation(cls).orElse(null);

        return getHandler(cls).validate(
                AnnotationCommandValidateRequest.create(annotation, context.getCommand().getInfo(), context.toCompleted()));
    }

    public void prepare(AnnotationNativeCommandContext nativeCommandContext) {
        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!nativeCommandContext.getItem().isAnnotationPresent(cls))
                continue;

            doPrepare(nativeCommandContext, cls);
        }
    }

    private <T extends Annotation> void doPrepare(AnnotationNativeCommandContext nativeCommandContext, Class<T> cls) {
        T annotation = nativeCommandContext.getItem().getDeclaredAnnotation(cls);

        getHandler(cls).prepare(
                AnnotationCommandContext.create(annotation, nativeCommandContext.getInfo()));
    }

    private <T extends Annotation> AnnotationCommandHandler<T> getHandler(Class<T> cls) {
        //noinspection unchecked
        return (AnnotationCommandHandler<T>) handlers.get(cls);
    }
}
