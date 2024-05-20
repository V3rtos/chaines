package me.moonways.bridgenet.jdbc.entity.adapter.type;

import lombok.SneakyThrows;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.adapter.ParameterTypeAdapter;

import java.util.Objects;

public class ClassTypeAdapter implements ParameterTypeAdapter {

    private boolean isClassType(EntityParametersDescriptor.ParameterUnit unit) {
        return Objects.equals(Class.class, unit.getType());
    }

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return isClassType(unit);
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return isClassType(unit);
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        return ((Class<?>)unit.getValue()).getName();
    }

    @SneakyThrows
    @Override
    public Object deserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return Class.forName(unit.getValue().toString());
    }

    @Override
    public Class<?> getOutputSerializationType() {
        return String.class;
    }

    @Override
    public Class<?> getOutputDeserializationType() {
        return Class.class;
    }
}
