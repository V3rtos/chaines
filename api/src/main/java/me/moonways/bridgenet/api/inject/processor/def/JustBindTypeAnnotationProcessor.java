package me.moonways.bridgenet.api.inject.processor.def;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorAdapter;

import java.lang.annotation.Annotation;

@Log4j2
@RequiredArgsConstructor
public class JustBindTypeAnnotationProcessor extends TypeAnnotationProcessorAdapter<Annotation> {
    private final Class<? extends Annotation> annotationType;

    @Inject
    private BeansService beans;

    @SuppressWarnings("unchecked")
    @Override
    protected Class<Annotation> getAnnotationType() {
        return (Class<Annotation>) annotationType;
    }

    @Override
    public void processBean(BeansService service, Bean bean) {
        beans.bind(bean);
    }
}
