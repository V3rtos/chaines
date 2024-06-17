package me.moonways.endpoint.mojang.http.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Profile {

    private final String id;
    private final String name;
    private final TexturesProperties[] properties;
}
