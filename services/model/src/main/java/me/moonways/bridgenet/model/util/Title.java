package me.moonways.bridgenet.model.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class Title<T> {

    private final T title;
    private final T subtitle;

    private final TitleFade fade;
}
