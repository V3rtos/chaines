package me.moonways.bridgenet.jdbc.entity.descriptor.adapter;

import me.moonways.bridgenet.jdbc.entity.DatabaseEntityException;
import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;

import java.io.*;
import java.util.Objects;

public class SerializableTypeAdapter implements ParameterTypeAdapter {

    @Override
    public boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit) {
        return Serializable.class.isAssignableFrom(unit.getType());
    }

    @Override
    public boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit) {
        return Objects.equals(byte[].class, unit.getType());
    }

    @Override
    public Object serialize(EntityParametersDescriptor.ParameterUnit unit) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(unit.getValue());

            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException exception) {
            throw new DatabaseEntityException(exception);
        }
    }

    @Override
    public Object deserialize(EntityParametersDescriptor.ParameterUnit unit) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((byte[]) unit.getValue());
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        }
        catch (ClassNotFoundException | IOException exception) {
            throw new DatabaseEntityException(exception);
        }
    }

    @Override
    public Class<?> getOutputSerializationType() {
        return byte[].class;
    }

    @Override
    public Class<?> getOutputDeserializationType() {
        return Object.class;
    }
}
