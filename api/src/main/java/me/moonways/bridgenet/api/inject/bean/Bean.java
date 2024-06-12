package me.moonways.bridgenet.api.inject.bean;

import lombok.*;

import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Bean {

    private final Properties properties;

    @EqualsAndHashCode.Include
    private final UUID id;

    @ToString.Include
    private final BeanType type;

    @Setter
    private Object root;

    /**
     * Проверить на совместимсть и схожесть бинов
     * относительно текущего бина.
     *
     * @param bean - бин, который проверяем.
     */
    public boolean isSimilar(Bean bean) {
        return bean.getType().isSimilarExact(type.getRoot()) || Stream.of(bean.getType().getInterfaces())
                .anyMatch(type::isSimilar);
    }

    /**
     * Проверить на совместимсть и схожесть бинов
     * относительно текущего бина.
     *
     * @param bean - бин, который проверяем.
     */
    public boolean isSimilarExact(Bean bean) {
        return bean.getType().isSimilarExact(type.getRoot());
    }
}
