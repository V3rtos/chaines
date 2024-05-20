package me.moonways.bridgenet.test.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;
import me.moonways.bridgenet.jdbc.entity.modern.persistence.*;

import java.util.UUID;

@Getter
@Builder
@ToString
@NamedEntity("employees")
public class EmployeeEntity {

    @Key
    @Signature(ParameterSignature.AUTO_GENERATED)
    private final long id;

    @ExternalEntity
    @Signature(ParameterSignature.NOTNULL)
    private final ExampleEmployeeInfo description;

    @Getter
    @Builder
    @ToString
    @NamedEntity("employees_info")
    public static class ExampleEmployeeInfo {

        @Key
        @Signature(ParameterSignature.NOTNULL)
        private final UUID id;

        @Named("name")
        @Signature(ParameterSignature.NOTNULL)
        private final String employeeName;
    }
}
