package me.moonways.bridgenet.api.inject.decorator.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlElementWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "methodHandler")
public class XmlDecoratorHandler {

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
    private List<XmlDecoratorInput> conflicts;

    @Setter(onMethod_ = {
        @XmlElementWrapper(name = "inherits"),
        @XmlElement(name = "input")
    })
    private List<XmlDecoratorInput> inherits;
}
