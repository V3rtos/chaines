package me.moonways.bridgenet.api.inject.decorator.config;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@XmlRootElement(name = "input")
public class XMLInputDescriptor {

    @Setter(onMethod_ = @XmlValue)
    private String value;
}
