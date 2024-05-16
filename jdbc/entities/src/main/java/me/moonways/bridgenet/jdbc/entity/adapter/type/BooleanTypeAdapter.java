package me.moonways.bridgenet.jdbc.entity.adapter.type;

import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.adapter.ParameterTypeAdapter;

import java.util.Objects;

public class BooleanTypeAdapter implements ParameterTypeAdapter {

    private static final short TRUE = 1;
    private static final short FALSE = 0;

    private boolean isBooleanType(EntityParametersDescriptor.ParameterUnit unit) {
        Class<?> type = unit.getType();
        return Objects.equals(Boolean.class, type) || Objects.equals(boolean.class, type);
    }

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return isBooleanType(unit);
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return isBooleanType(unit);
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        System.out.println("serialize " + unit);
        return Boolean.parseBoolean(unit.getValue().toString()) ? TRUE : FALSE;
    }

    @Override
    public Object deserialize(EntityParametersDescriptor.ParameterUnit unit) {
        System.out.println("deserialize " + unit);

        short shortValue = (short) unit.getValue();
        return shortValue == TRUE || shortValue == FALSE ? shortValue == TRUE : unit.getValue();
    }

    @Override
    public Class<?> getOutputSerializationType() {
        return Short.class;
    }

    @Override
    public Class<?> getOutputDeserializationType() {
        return Boolean.class;
    }
}
