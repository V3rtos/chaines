package me.moonways.bridgenet.injection.proxy.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "methodHandler")
public class XmlProxiedMethodHandler {

    @Setter(onMethod_ = @XmlElement(name = "annotation"))
    private String annotationClassname;

    @Setter(onMethod_ = @XmlElement(name = "path"))
    private String handlerClassname;
}
