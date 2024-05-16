package me.moonways.bridgenet.jdbc.entity.descriptor.adapter;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.SneakyThrows;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;

import java.util.Objects;

public class JsonTypeAdapter implements ParameterTypeAdapter {

    private static final Gson GSON = new Gson();

    @Builder
    private static class WrappedObject {

        private final Object source;
        private final String classname;

        public Object cast() throws ClassNotFoundException {
            return Class.forName(classname).cast(source);
        }
    }

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return Objects.equals(unit.getType(), Object.class);
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        String string = unit.getValue().toString();
        return Objects.equals(String.class, unit.getType())
                && string.startsWith("{")
                && string.endsWith("}");
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        Object value = !unit.getType().equals(Object.class) ? unit.getValue() :
                WrappedObject.builder()
                        .source(unit.getValue())
                        .classname(unit.getValue().getClass().getName())
                        .build();

        return GSON.toJson(value);
    }

    @SneakyThrows
    @Override
    public Object deserialize(EntityParametersDescriptor.ParameterUnit unit) {
        String json = unit.getValue().toString();

        if (unit.getType().equals(Object.class)) {
            WrappedObject wrappedObject = GSON.fromJson(json, WrappedObject.class);

            return wrappedObject.cast();
        }

        try {
            return GSON.fromJson(json, unit.getType());
        } catch (Throwable exception) {
            unit.setType(Object.class);

            return deserialize(unit);
        }
    }

    @Override
    public Class<?> getOutputSerializationType() {
        return String.class;
    }

    @Override
    public Class<?> getOutputDeserializationType() {
        return Object.class;
    }
}
