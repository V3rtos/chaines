package me.moonways.bridgenet.api.inject.processor.verification;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.processor.AnnotationProcessorConfig;

import java.lang.annotation.Annotation;

@Log4j2
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class AnnotationVerificationContext<V extends Annotation> {

    private final Class<V> annotationType;

    private final Bean bean;
    private final AnnotationProcessorConfig<V> config;

    public ResultCompiler toResult() {
        return new ResultCompiler();
    }

    public static class ResultCompiler {
        public AnnotationVerificationResult asSuccessful() {
            return AnnotationVerificationResult.builder()
                    .code(AnnotationVerificationResult.Code.SUCCESS)
                    .build();
        }

        public AnnotationVerificationResult asFailureExceptionally(Exception exception) {
            log.error(exception);
            return asFailure(exception.getMessage());
        }

        public AnnotationVerificationResult asFailure(String errorMessage) {
            return AnnotationVerificationResult.builder()
                    .code(AnnotationVerificationResult.Code.FAILURE)
                    .errorMessage(errorMessage)
                    .build();
        }

        public AnnotationVerificationResult asNotEnoughData(String name) {
            String errorMessage = String.format("parameter '%s' is not initialized", name);
            return asFailure(errorMessage);
        }
    }
}
