package me.moonways.bridgenet.api.inject.decorator.config;

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
@XmlRootElement(name = "interceptor")
public class XMLInterceptorDescriptor implements XmlRootObject {

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "methodHandlers"),
            @XmlElement(name = "methodHandler")
    })
    private List<XMLMethodHandlerDescriptor> methodHandlers;
}
