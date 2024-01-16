package me.moonways.bridgenet.bootstrap.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.moonways.bridgenet.api.util.jaxb.XmlRootObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "bootstrap")
public class XMLBootstrapConfigDescriptor implements XmlRootObject {

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "hooks"),
            @XmlElement(name = "hook")
    })
    private List<XMLHookDescriptor> hooks;
}
