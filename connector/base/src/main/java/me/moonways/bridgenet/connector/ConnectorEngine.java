package me.moonways.bridgenet.connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.jdbc.provider.BridgenetJdbcProvider;
import me.moonways.bridgenet.mtp.MTPConnectionFactory;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import me.moonways.bridgenet.mtp.client.MTPClientChannelHandler;
import me.moonways.bridgenet.mtp.client.MTPClientConnectionFactory;
import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XMLServiceModuleDescriptor;
import me.moonways.bridgenet.rsi.xml.XMLServiceModulePropertyDescriptor;
import me.moonways.bridgenet.rsi.xml.XMLServicesConfigDescriptor;
import me.moonways.bridgenet.rsi.xml.XmlServiceInfoDescriptor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

@Log4j2
public final class ConnectorEngine {

    private BeansService beansService;

    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private Gson gson;

    public void setProperties() {
        // jdbc settings.
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    public BeansService bindAll() {
        beansService = new BeansService();

        beansService.bind(MTPConnectionFactory.createConnectionFactory());
        beansService.start(BeansService.generateDefaultProperties());

        beansService.bind(new Properties());
        beansService.bind(new GsonBuilder().setLenient().create());

        beansService.inject(this);

        bindJdbcConnection();

        return beansService;
    }

    private void bindJdbcConnection() {
        BridgenetJdbcProvider.JdbcSettingsConfig jdbcSettingsConfig = readSettings();
        BridgenetJdbcProvider bridgenetJdbcProvider = new BridgenetJdbcProvider();

        bridgenetJdbcProvider.initConnection(jdbcSettingsConfig);

        beansService.bind(bridgenetJdbcProvider.getDatabaseProvider());
        beansService.bind(bridgenetJdbcProvider.getDatabaseProvider().getComposer());
        beansService.bind(bridgenetJdbcProvider.getDatabaseConnection());
    }

    private BridgenetJdbcProvider.JdbcSettingsConfig readSettings() {
        return gson.fromJson(
                assembly.readResourceFullContent(
                        ResourcesTypes.JDBC_SETTINGS_JSON,
                        StandardCharsets.UTF_8),
                BridgenetJdbcProvider.JdbcSettingsConfig.class);
    }

    public void connectToEndpoints(RemoteServiceRegistry registry) {
        registry.initializeXmlConfiguration();

        XMLServicesConfigDescriptor xmlConfiguration = registry.getXmlConfiguration();
        List<XmlServiceInfoDescriptor> servicesList = xmlConfiguration.getServicesList();

        XMLServiceModulePropertyDescriptor hostPropertyDescriptor = xmlConfiguration.getModulesList()
                .stream()
                .filter(descriptor -> descriptor.getConfigClass().toLowerCase().contains("access"))
                .findFirst()
                .map(XMLServiceModuleDescriptor::getProperties)
                .map(List::stream)
                .flatMap(stream -> stream.filter(propertyDescriptor -> propertyDescriptor.getName().toLowerCase().contains("host"))
                        .findFirst())
                .orElse(null);

        for (XmlServiceInfoDescriptor descriptor : servicesList) {
            connectToEndpoint(descriptor, hostPropertyDescriptor);
        }
    }

    public void disconnectToEndpoints(RemoteServiceRegistry registry) {
        XMLServicesConfigDescriptor xmlConfiguration = registry.getXmlConfiguration();

        if (xmlConfiguration != null) {
            List<XmlServiceInfoDescriptor> servicesList = xmlConfiguration.getServicesList();

            for (XmlServiceInfoDescriptor descriptor : servicesList) {
                disconnectToEndpoint(descriptor);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows({ClassNotFoundException.class})
    private <T extends RemoteService> void connectToEndpoint(XmlServiceInfoDescriptor descriptor, XMLServiceModulePropertyDescriptor hostPropertyDescriptor) {
        Class<T> modelClass = (Class<T>) getClass().getClassLoader()
                .loadClass(descriptor.getModelPath())
                .asSubclass(RemoteService.class);

        ServiceInfo serviceInfo = new ServiceInfo(descriptor.getName(),
                Integer.parseInt(descriptor.getBindPort()), modelClass);

        AccessRemoteModule accessModule = new AccessRemoteModule();
        accessModule.init(serviceInfo, new AccessConfig(hostPropertyDescriptor.getValue()));

        T service = accessModule.lookupStub();

        beansService.bind(modelClass, service);
        log.info("Success connected to rmi service - §2" + descriptor);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows({ClassNotFoundException.class})
    private <T extends RemoteService> void disconnectToEndpoint(XmlServiceInfoDescriptor descriptor) {
        Class<T> modelClass = (Class<T>) getClass().getClassLoader()
                .loadClass(descriptor.getModelPath())
                .asSubclass(RemoteService.class);

        beansService.unbind(modelClass);
        log.info("§4Disconnected from rmi service - " + descriptor);
    }

    public MTPMessageSender connectBridgenetServer(MTPDriver mtpDriver, MTPClientConnectionFactory connectionFactory,
                                                   MTPClientChannelHandler channelHandler) {
        mtpDriver.bindMessages();
        mtpDriver.bindHandlers();

        return connectionFactory.newUncastedClient(channelHandler);
    }
}
