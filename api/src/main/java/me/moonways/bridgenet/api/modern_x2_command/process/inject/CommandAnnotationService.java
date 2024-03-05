package me.moonways.bridgenet.api.modern_x2_command.process.inject;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorAdapter;
import me.moonways.bridgenet.api.modern_x2_command.process.inject.validate.CommandAnnotationValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.process.inject.validate.CommandAnnotationValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.process.inject.validate.CommandAnnotationValidateResult;

import java.lang.annotation.Annotation;
import java.util.*;

@Autobind
public class CommandAnnotationService {

    private final Map<Class<? extends Annotation>, CommandAnnotationHandler<?>> handlers = new HashMap<>();

    @Inject
    private CommandAnnotationValidateManagement verifyManagement;

    @Inject
    private BeansScanningService scanningService;

    @PostConstruct
    public void inject() {
        Set<Bean> beans = scanningService.scanBeansBySuperclass(CommandAnnotationHandler.class);

        for (Bean bean : beans) {
            Class<? extends Annotation> annCls = TypeAnnotationProcessorAdapter.getGenericType(0, bean.getType().getRoot());

            handlers.put(annCls, (CommandAnnotationHandler<?>) bean.getRoot());
        }
    }

    public void removeAll() {
        handlers.clear();
    }

    public List<CommandAnnotationValidateResult> validateAll(CommandWrapAnnotationContext context) {
        List<CommandAnnotationValidateResult> results = new ArrayList<>();

        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!context.getCommand().getBean().getType().isAnnotated(cls))
                continue;

            results.add(validate(context, cls));
        }

        return results;
    }

    private <T extends Annotation> CommandAnnotationValidateResult validate(CommandWrapAnnotationContext context, Class<T> cls) {
        T annotation = context.getCommand().getBeanMethod().getAnnotation(cls).orElse(null);

        return getHandler(cls).validate(
                CommandAnnotationValidateRequest.create(annotation, context.getCommand().getInfo(), context.toExecutionContext()));
    }

    public void prepare(CommandReflectAnnotationContext nativeCommandContext) {
        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!nativeCommandContext.getItem().isAnnotationPresent(cls))
                continue;

            doPrepare(nativeCommandContext, cls);
        }
    }

    private <T extends Annotation> void doPrepare(CommandReflectAnnotationContext nativeCommandContext, Class<T> cls) {
        T annotation = nativeCommandContext.getItem().getDeclaredAnnotation(cls);

        getHandler(cls).prepare(
                CommandBaseAnnotationContext.create(annotation, nativeCommandContext.getInfo()));
    }

    private <T extends Annotation> CommandAnnotationHandler<T> getHandler(Class<T> cls) {
        //noinspection unchecked
        return (CommandAnnotationHandler<T>) handlers.get(cls);
    }
}
