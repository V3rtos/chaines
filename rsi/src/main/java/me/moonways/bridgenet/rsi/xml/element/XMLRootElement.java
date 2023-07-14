package me.moonways.bridgenet.rsi.xml.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "configuration")
public class XMLRootElement {

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "modules"),
            @XmlElement(name = "module")
    })
    private List<XMLModule> modulesList;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "services"),
            @XmlElement(name = "service")
    })
    private List<XMLService> servicesList;
}
