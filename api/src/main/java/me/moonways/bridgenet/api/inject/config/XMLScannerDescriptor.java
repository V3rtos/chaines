package me.moonways.bridgenet.api.inject.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@ToString
@XmlRootElement(name = "scanner")
@XmlType(propOrder = {"annotationClass", "targetClass"})
public class XMLScannerDescriptor {

    @Setter(onMethod_ = @XmlElement)
    private String annotationClass;

    @Setter(onMethod_ = @XmlElement)
    private String targetClass;
}
