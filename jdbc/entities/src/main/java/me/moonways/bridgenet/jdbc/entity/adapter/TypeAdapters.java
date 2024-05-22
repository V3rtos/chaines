package me.moonways.bridgenet.jdbc.entity.adapter;

import me.moonways.bridgenet.jdbc.entity.adapter.type.*;

/**
 * Список всех доступных адаптеров типов
 * параметров сущностей.
 */
public interface TypeAdapters {

    ParameterTypeAdapter SERIALIZATION = new SerializableTypeAdapter();
    ParameterTypeAdapter BOOLEAN = new BooleanTypeAdapter();
    ParameterTypeAdapter ENUMS = new EnumsTypeAdapter();
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
            // ---------------- //
                    BOOLEAN,
                    ENUMS,
                    UUID,
                    CLASS,
            // ---------------- //
                    SERIALIZATION, // <--- Обязательно должен быть предпоследним.
                    JSON, // <--- Обязательно должен быть последним.
            };
}
