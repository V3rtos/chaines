package me.moonways.bridgenet.jdbc.entity.adapter;

import me.moonways.bridgenet.jdbc.core.compose.ParameterType;
import me.moonways.bridgenet.jdbc.entity.criteria.SearchElement;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityDescriptor;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class TypeAdaptersControl {

    public void tryDeserializeValues(@NotNull EntityDescriptor entityDescriptor) {
        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entityDescriptor.getParameters().getParameterUnits()) {
            for (ParameterTypeAdapter adapter : TypeAdapters.TYPES) {

                if (adapter.canDeserialize(parameterUnit)) {

                    if (parameterUnit.getValue() != null) {
                        Object object = adapter.deserialize(parameterUnit);
                        parameterUnit.setValue(object);
                    }

                    parameterUnit.setType(adapter.getOutputDeserializationType());
                    break;
                }
            }
        }
    }

    public void trySerializeValues(@NotNull EntityDescriptor entityDescriptor) {
        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entityDescriptor.getParameters().getParameterUnits()) {
            if (parameterUnit.isExternal() || ParameterType.fromJavaType(parameterUnit.getType()) != null) {
                continue;
            }

            for (ParameterTypeAdapter adapter : TypeAdapters.TYPES) {
                if (adapter.canSerialize(parameterUnit)) {

                    if (parameterUnit.getValue() != null) {
                        Object object = adapter.serialize(parameterUnit);
                        parameterUnit.setValue(object);
                    }

                    parameterUnit.setType(adapter.getOutputSerializationType());
                    break;
                }
            }
        }
    }

    private void serializeUnit(EntityParametersDescriptor.ParameterUnit parameterUnit) {
        for (ParameterTypeAdapter adapter : TypeAdapters.TYPES) {
            if (adapter.canSerialize(parameterUnit)) {

                if (parameterUnit.getValue() != null) {
                    Object object = adapter.serialize(parameterUnit);
                    parameterUnit.setValue(object);
                }

                parameterUnit.setType(adapter.getOutputSerializationType());
                break;
            }
        }
    }

    public <E> SearchElement<?> trySerializeSearchElement(String id, @NotNull EntityDescriptor entityDescriptor, SearchElement<E> searchElement) {
        Optional<EntityParametersDescriptor.ParameterUnit> parameterUnitOptional
                = entityDescriptor.getParameters().find(id);

        if (!parameterUnitOptional.isPresent()) {
            return searchElement;
        }

        EntityParametersDescriptor.ParameterUnit parameterUnit = parameterUnitOptional.get();
        Class<?> type = parameterUnit.getType();

        if (ParameterType.fromJavaType(type) != null) {
            return searchElement;
        }

        EntityParametersDescriptor.ParameterUnit wrapped =
                EntityParametersDescriptor.ParameterUnit.builder()
                        .id(id)
                        .value(searchElement.getExpectation())
                        .type(parameterUnit.isExternal() ? type : Object.class)
                        .build();

        serializeUnit(wrapped);
        return SearchElement.builder()
                .expectation(wrapped.getValue())
                .binder(searchElement.getBinder())
                .matcher(searchElement.getMatcher())
                .build();
    }
}
