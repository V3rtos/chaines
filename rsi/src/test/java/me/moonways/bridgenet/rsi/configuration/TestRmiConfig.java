package me.moonways.bridgenet.rsi.configuration;

import me.moonways.bridgenet.rsi.xml.XMLConfigurationInstance;
import me.moonways.bridgenet.rsi.xml.XMLConfigurationParser;
import me.moonways.bridgenet.rsi.xml.element.XMLRootElement;
import me.moonways.bridgenet.rsi.xml.element.XMLService;

public class TestRmiConfig {

    public static void main(String[] args) {
        XMLConfigurationParser parser = new XMLConfigurationParser();
        XMLConfigurationInstance config = parser.parseNewInstance();

        System.out.println(config);

        XMLRootElement rootElement = config.getRootElement();
        for (XMLService service : rootElement.getServicesList()) {
            System.out.println(service.getName() + "(port: " + service.getBindPort() + ", class: " + service.getTargetType() + ")");
        }
    }
}
