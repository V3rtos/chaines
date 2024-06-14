package me.moonways.endpoint.mojang.http.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class NameAndUuid {

    private final String id;
    private final String name;
}
