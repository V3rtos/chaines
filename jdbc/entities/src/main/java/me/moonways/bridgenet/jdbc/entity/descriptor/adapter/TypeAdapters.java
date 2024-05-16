package me.moonways.bridgenet.jdbc.entity.descriptor.adapter;

/**
 * Список всех доступных адаптеров типов
 * параметров сущностей.
 */
public interface TypeAdapters {

    ParameterTypeAdapter SERIALIZATION = new SerializableTypeAdapter();
    ParameterTypeAdapter BOOLEAN = new BooleanTypeAdapter();
    ParameterTypeAdapter UUID = new UuidTypeAdapter();
    ParameterTypeAdapter CLASS = new ClassTypeAdapter();
    ParameterTypeAdapter JSON = new JsonTypeAdapter();

    /**
     * В списке типов соблюдена обязательная
     * последовательность адаптеров. В этом же
     * порядке они будут воспроизводиться в цикле
     * при попытке обработки юнита.
     */
    ParameterTypeAdapter[] TYPES =
            {
                    JSON, // <--- Обязательно должен быть первым.
                    BOOLEAN,
                    UUID,
                    CLASS,
                    SERIALIZATION, // <--- Обязательно должен быть последним.
            };
}
