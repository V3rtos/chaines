package me.moonways.bridgenet.rsi.xml;

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
@XmlRootElement(name = "configuration")
public class XMLServicesConfigDescriptor implements XmlRootObject {

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "modules"),
            @XmlElement(name = "module")
    })
    private List<XMLServiceModuleDescriptor> modulesList;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "services"),
            @XmlElement(name = "service")
    })
    private List<XmlServiceInfoDescriptor> servicesList;
}
