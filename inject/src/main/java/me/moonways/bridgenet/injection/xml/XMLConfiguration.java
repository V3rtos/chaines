package me.moonways.bridgenet.injection.xml;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.injection.xml.element.XMLRootElement;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class XMLConfiguration {

    @Getter
    private final XMLRootElement rootElement;
}
