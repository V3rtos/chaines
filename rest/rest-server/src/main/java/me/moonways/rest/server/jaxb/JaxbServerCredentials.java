package me.moonways.rest.server.jaxb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@XmlRootElement(name = "authentication")
public class JaxbServerCredentials {

    @Setter(onMethod_ = @XmlElement)
    private String username;

    @Setter(onMethod_ = @XmlElement)
    private String password;
}
