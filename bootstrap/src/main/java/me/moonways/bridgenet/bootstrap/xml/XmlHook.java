package me.moonways.bridgenet.bootstrap.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@ToString
@XmlRootElement(name = "hook")
@XmlType(propOrder = {"displayName", "priorityID", "priority", "executorPath"})
public class XmlHook {

    @Setter(onMethod_ = @XmlElement)
    private String displayName;

    @Setter(onMethod_ = @XmlElement)
    private String priorityID;

    @Setter(onMethod_ = @XmlElement)
    private String priority;

    @Setter(onMethod_ = @XmlElement)
    private String executorPath;
}
