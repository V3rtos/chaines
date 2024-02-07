package me.moonways.bridgenet.rest.server.jaxb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "controller")
public class JaxbHttpController {

    @Setter(onMethod_ = @XmlElement)
    private String method;

    @Setter(onMethod_ = @XmlElement)
    private String pattern;

    @Setter(onMethod_ = @XmlElement(name = "handler"))
    private String handlerClasspath;
}
