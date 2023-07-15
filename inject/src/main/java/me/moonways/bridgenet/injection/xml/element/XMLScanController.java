package me.moonways.bridgenet.injection.xml.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@ToString
@XmlRootElement(name = "scanner")
@XmlType(propOrder = {"annotationClass", "targetClass"})
public class XMLScanController {

    @Setter(onMethod_ = @XmlElement)
    private String annotationClass;

    @Setter(onMethod_ = @XmlElement)
    private String targetClass;
}
