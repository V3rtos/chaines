package me.moonways.bridgenet.rsi.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@ToString
@XmlType(propOrder = {"bindPort", "name", "modelPath"})
@XmlRootElement(name = "service")
public class XmlServiceInfoDescriptor {

    @Setter(onMethod_ = @XmlElement)
    private String bindPort;

    @Setter(onMethod_ = @XmlElement)
    private String name;

    @Setter(onMethod_ = @XmlElement)
    private String modelPath;
}
