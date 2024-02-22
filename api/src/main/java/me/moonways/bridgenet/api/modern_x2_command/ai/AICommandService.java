package me.moonways.bridgenet.api.modern_x2_command.ai;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AICommandService {

    private final Map<Class<? extends Annotation>, AICommandHandler<?>> handlers = new HashMap<>();

    @Inject
    private AICommandValidateManagement verifyManagement;

    @PostConstruct
    public void inject() {
    }

    public List<AICommandValidateResult> validateAll(AINativeExecutionContext context) {
        List<AICommandValidateResult> results = new ArrayList<>();

        for (Class<? extends Annotation> cls : handlers.keySet()) {
            if (!context.getCommand().getItem().isAnnotationPresent(cls))
                continue;

            results.add(validate(context, cls));
        }

        return results;
    }

    private <T extends Annotation> AICommandValidateResult validate(AINativeExecutionContext context, Class<T> cls) {
        T annotation = context.getCommand().getHandle().getDeclaredAnnotation(cls);

        return getHandler(cls).validate(
                AICommandValidateRequest.create(annotation, context.getCommand().getInfo(), context.toCompleted()));
    }

    public void doAll(AINativeCommandContext nativeCommandContext) {
        for (Class<? extends Annotation> cls : handlers.keySet()) {
            doSingle(nativeCommandContext, cls);
        }
    }

    private <T extends Annotation> void doSingle(AINativeCommandContext nativeCommandContext, Class<T> cls) {
        T annotation = nativeCommandContext.getItem().getDeclaredAnnotation(cls);

        getHandler(cls).load(
                AICommandContext.create(annotation, nativeCommandContext.getInfo()));
    }

    private <T extends Annotation> AICommandHandler<T> getHandler(Class<T> cls) {
        //noinspection unchecked
        return (AICommandHandler<T>) handlers.get(cls);
    }
}
