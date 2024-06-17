package me.moonways.endpoint.mojang.http.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TexturesProperties {

    private final String value;
    private final String signature;
}
