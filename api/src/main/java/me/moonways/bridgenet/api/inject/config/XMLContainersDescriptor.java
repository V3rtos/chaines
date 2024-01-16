package me.moonways.bridgenet.api.inject.config;

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
@XmlRootElement(name = "containers")
public class XMLContainersDescriptor implements XmlRootObject {

    @Setter(onMethod_ = @XmlElement)
    private String searchPackage;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "scanners"),
            @XmlElement(name = "scanner")
    })
    private List<XMLScannerDescriptor> scannersList;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "factories"),
            @XmlElement(name = "factory")
    })
    private List<XMLObjectFactoryDescriptor> factoriesList;
}
