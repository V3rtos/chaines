package me.moonways.bridgenet.rest.api.test.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RandomCatFact {

    private final String fact;
    private final int length;
}
