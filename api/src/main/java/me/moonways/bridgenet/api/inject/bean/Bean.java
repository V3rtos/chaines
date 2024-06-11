package me.moonways.bridgenet.api.inject.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Bean {

    private final Properties properties;

    @EqualsAndHashCode.Include
    private final UUID id;

    @ToString.Include
    private final BeanType type;
    private final Object root;

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
