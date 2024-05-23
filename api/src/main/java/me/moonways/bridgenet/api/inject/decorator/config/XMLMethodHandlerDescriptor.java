package me.moonways.bridgenet.api.inject.decorator.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "methodHandler")
public class XMLMethodHandlerDescriptor {

    @Setter(onMethod_ = @XmlElement(name = "name"))
    private String name;

    @Setter(onMethod_ = @XmlElement(name = "annotation"))
    private String annotationClassname;

    @Setter(onMethod_ = @XmlElement(name = "path"))
    private String handlerClassname;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "conflicts"),
            @XmlElement(name = "input")
    })
    private List<XMLInputDescriptor> conflicts;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "inherits"),
            @XmlElement(name = "input")
    })
    private List<XMLInputDescriptor> inherits;
}
