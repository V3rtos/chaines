package me.moonways.bridgenet.jdbc.entity.adapter.type;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.SneakyThrows;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;
import me.moonways.bridgenet.jdbc.entity.adapter.ParameterTypeAdapter;

import java.util.Objects;

public class JsonTypeAdapter implements ParameterTypeAdapter {

    private static final String JSON_BEGIN = "{";
    private static final String JSON_END = "}";

    private static final Gson GSON = new Gson();

    @Builder
    private static class WrappedObject {

        private final Object source;
        private final String classpath;

        public Object cast() throws ClassNotFoundException {
            return Class.forName(classpath).cast(source);
        }
    }

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return true;
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        String string = unit.getValue().toString();
        return string.startsWith(JSON_BEGIN) && string.endsWith(JSON_END);
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        Object value = !unit.getType().equals(Object.class) ? unit.getValue() :
                WrappedObject.builder()
                        .source(unit.getValue())
                        .classpath(unit.getValue().getClass().getName())
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
