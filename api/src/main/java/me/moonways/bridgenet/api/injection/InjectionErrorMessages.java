package me.moonways.bridgenet.api.injection;

public final class InjectionErrorMessages {

    public static final String DUPLICATED_IMPLEMENTS = "Found duplicated instances for interface {}: ({})";
    public static final String ALREADY_BIND = "Component {} is already bind";
    public static final String COMPONENT_NOT_FOUND = "Component {} not found in {}";
    public static final String SCANNER_NOT_FOUND = "Scanner class {} is not found";
    public static final String CANNOT_CREATE_OBJECT_INSTANCE = "Cannot create object instance from {}: {}";
    public static final String CANNOT_INJECT_FIELD_TYPE = "Cannot inject target field type as {}: {}";

}
