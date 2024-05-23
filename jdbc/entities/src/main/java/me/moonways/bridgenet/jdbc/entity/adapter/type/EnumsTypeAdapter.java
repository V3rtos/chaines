package me.moonways.bridgenet.jdbc.entity.adapter.type;

import me.moonways.bridgenet.jdbc.entity.adapter.ParameterTypeAdapter;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;

public class EnumsTypeAdapter implements ParameterTypeAdapter {

    private boolean isEnumType(EntityParametersDescriptor.ParameterUnit unit) {
        return Enum.class.isAssignableFrom(unit.getType());
    }

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return isEnumType(unit);
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return isEnumType(unit);
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        return ((Enum<?>) unit.getValue()).ordinal();
    }

    @Override
    public Object deserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return unit.getType().getEnumConstants()[Integer.parseInt(unit.getValue().toString())];
    }

    @Override
    public Class<?> getOutputSerializationType() {
        return Integer.class;
    }

    @Override
    public Class<?> getOutputDeserializationType() {
        return Enum.class;
    }
}
