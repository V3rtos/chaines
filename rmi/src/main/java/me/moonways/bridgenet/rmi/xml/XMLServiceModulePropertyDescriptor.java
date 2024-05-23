package me.moonways.bridgenet.rmi.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "property")
public class XMLServiceModulePropertyDescriptor {

    @Setter(onMethod_ = @XmlAttribute)
    private String name;

    @Setter(onMethod_ = @XmlAttribute)
    private String value;
}
