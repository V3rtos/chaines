package me.moonways.rest.server.jaxb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.moonways.bridgenet.api.jaxb.XmlRootObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlRootElement(name = "serverContext")
public class JaxbServerContext implements XmlRootObject {

    @Setter(onMethod_ = @XmlElement)
    private Boolean printExceptions;

    @Setter(onMethod_ = @XmlElement)
    private JaxbServerConnection connection;

    @Setter(onMethod_ = {
            @XmlElementWrapper(name = "controllers"),
            @XmlElement(name = "controller"),
    })
    private List<JaxbHttpController> controllersList;
}
