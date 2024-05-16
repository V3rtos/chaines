package me.moonways.bridgenet.jdbc.entity.descriptor.adapter;

import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;

import java.util.Objects;
import java.util.UUID;

public class UuidTypeAdapter implements ParameterTypeAdapter {

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return Objects.equals(UUID.class, unit.getType());
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return Objects.equals(UUID.class, unit.getType());
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        return unit.getValue().toString();
    }

    @Override
    public Object deserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return UUID.fromString(unit.getValue().toString());
    }

    @Override
    public Class<?> getOutputSerializationType() {
        return String.class;
    }

    @Override
    public Class<?> getOutputDeserializationType() {
        return UUID.class;
    }
}
