package me.moonways.bridgenet.api.modern_x2_command.ai;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorAdapter;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;

import java.lang.annotation.Annotation;
import java.util.*;

@Autobind
public class AICommandService {

    private final Map<Class<? extends Annotation>, AICommandHandler<?>> handlers = new HashMap<>();

    @Inject
    private AICommandValidateManagement verifyManagement;

    @Inject
    private BeansScanningService scanningService;

    @PostConstruct
    public void inject() {
        Set<Bean> beans = scanningService.scanBeansBySuperclass(AICommandHandler.class);

        for (Bean bean : beans) {
            Class<? extends Annotation> annCls = TypeAnnotationProcessorAdapter.getGenericType(0, bean.getType().getRoot());

            handlers.put(annCls, (AICommandHandler<?>) bean.getRoot());
        }
    }

    public void removeAll() {
        handlers.clear();
    }

    public List<AICommandValidateResult> validateAll(AINativeExecutionContext context) {
        List<AICommandValidateResult> results = new ArrayList<>();

        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!context.getCommand().getBean().getType().isAnnotated(cls))
                continue;

            results.add(validate(context, cls));
        }

        return results;
    }

    private <T extends Annotation> AICommandValidateResult validate(AINativeExecutionContext context, Class<T> cls) {
        T annotation = context.getCommand().getBeanMethod().getAnnotation(cls).orElse(null);

        return getHandler(cls).validate(
                AICommandValidateRequest.create(annotation, context.getCommand().getInfo(), context.toCompleted()));
    }

    public void prepare(AINativeCommandContext nativeCommandContext) {
        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!nativeCommandContext.getItem().isAnnotationPresent(cls))
                continue;

            doPrepare(nativeCommandContext, cls);
        }
    }

    private <T extends Annotation> void doPrepare(AINativeCommandContext nativeCommandContext, Class<T> cls) {
        T annotation = nativeCommandContext.getItem().getDeclaredAnnotation(cls);

        getHandler(cls).prepare(
                AICommandContext.create(annotation, nativeCommandContext.getInfo()));
    }

    private <T extends Annotation> AICommandHandler<T> getHandler(Class<T> cls) {
        //noinspection unchecked
        return (AICommandHandler<T>) handlers.get(cls);
    }
}
