package me.moonways.bridgenet.injection.xml.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "containers")
public class XMLRootElement {

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "scanners"),
            @XmlElement(name = "scanner")
    })
    private List<XMLScanController> scannersList;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "factories"),
            @XmlElement(name = "factory")
    })
    private List<XMLObjectFactory> factoriesList;
}
