package me.moonways.bridgenet.rsi.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "module")
public class XMLServiceModuleDescriptor {

    @Setter(onMethod_ = @XmlAttribute)
    private String name;

    @Setter(onMethod_ = @XmlAttribute)
    private String targetClass;

    @Setter(onMethod_ = @XmlAttribute)
    private String configClass;

    @Setter(onMethod_ = @XmlElement(name = "property"))
    private List<XmlServiceModulePropertyDescriptor> properties;
}
