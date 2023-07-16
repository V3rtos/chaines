package me.moonways.bridgenet.rsi.xml.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "property")
public class XMLModuleProperty {

    @Setter(onMethod_ = @XmlAttribute)
    private String name;

    @Setter(onMethod_ = @XmlAttribute)
    private String value;
}
