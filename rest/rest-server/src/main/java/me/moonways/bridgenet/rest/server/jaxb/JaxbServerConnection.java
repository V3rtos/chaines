package me.moonways.bridgenet.rest.server.jaxb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "connection")
public class JaxbServerConnection {

    @Setter(onMethod_ = @XmlElement)
    private String host;

    @Setter(onMethod_ = @XmlElement)
    private String port;

    @Setter(onMethod_ = @XmlElement(name = "authentication"))
    private JaxbServerCredentials credentials;
}
