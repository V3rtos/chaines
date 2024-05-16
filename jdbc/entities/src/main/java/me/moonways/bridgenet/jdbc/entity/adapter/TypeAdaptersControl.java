package me.moonways.bridgenet.jdbc.entity.adapter;

import me.moonways.bridgenet.jdbc.core.compose.ParameterType;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityDescriptor;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.util.search.SearchElement;

import java.util.Optional;

public final class TypeAdaptersControl {

    public void tryDeserializeValues(EntityDescriptor entityDescriptor) {
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

    public void trySerializeValues(EntityDescriptor entityDescriptor) {
        for (EntityParametersDescriptor.ParameterUnit parameterUnit : entityDescriptor.getParameters().getParameterUnits()) {
            if (ParameterType.fromJavaType(parameterUnit.getType()) != null) {
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

    public <E> SearchElement<?> trySerializeSearchElement(String id, EntityDescriptor entityDescriptor, SearchElement<E> searchElement) {
        Optional<EntityParametersDescriptor.ParameterUnit> parameterUnitOptional
                = entityDescriptor.getParameters().find(id);

        if (!parameterUnitOptional.isPresent()) {
            return searchElement;
        }

        Class<?> type = parameterUnitOptional.get().getType();

        if (ParameterType.fromJavaType(type) != null) {
            return searchElement;
        }

        EntityParametersDescriptor.ParameterUnit parameterUnit =
                EntityParametersDescriptor.ParameterUnit.builder()
                        .id(id)
                        .value(searchElement.getExpectation())
                        .type(type)
                        .build();

        serializeUnit(parameterUnit);
        return SearchElement.builder()
                .expectation(parameterUnit.getValue())
                .binder(searchElement.getBinder())
                .matcher(searchElement.getMatcher())
                .build();
    }
}
