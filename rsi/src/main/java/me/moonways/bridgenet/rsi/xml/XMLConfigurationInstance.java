package me.moonways.bridgenet.rsi.xml;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rsi.xml.element.XMLRootElement;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class XMLConfigurationInstance {

    @Getter
    private final XMLRootElement rootElement;
}
