package me.moonways.bridgenet.client.api;

import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.jdbc.provider.BridgenetJdbcProvider;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.connection.client.BridgenetNetworkClientHandler;
import me.moonways.bridgenet.mtp.connection.client.NetworkClientConnectionFactory;
import me.moonways.bridgenet.rmi.module.access.AccessConfig;
import me.moonways.bridgenet.rmi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rmi.service.RemoteService;
import me.moonways.bridgenet.rmi.service.RemoteServicesManagement;
import me.moonways.bridgenet.rmi.service.ServiceInfo;
import me.moonways.bridgenet.rmi.xml.XMLServiceModuleDescriptor;
import me.moonways.bridgenet.rmi.xml.XMLServiceModulePropertyDescriptor;
import me.moonways.bridgenet.rmi.xml.XMLServicesConfigDescriptor;
import me.moonways.bridgenet.rmi.xml.XmlServiceInfoDescriptor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

@Log4j2
public final class ClientEngine {

    private BeansService beansService;

    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private DatabaseProvider databaseProvider;

    public void setProperties() {
        // jdbc settings.
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    public BeansService bindAll() {
        beansService = new BeansService();

        beansService.start();

        beansService.bind(new Properties());
        beansService.bind(new GsonBuilder().setLenient().create());

        beansService.inject(this);

        bindJdbcConnection();

        return beansService;
    }

    private void bindJdbcConnection() {
        BridgenetJdbcProvider.JdbcSettingsConfig jdbcSettingsConfig = readSettings();
        BridgenetJdbcProvider bridgenetJdbcProvider = new BridgenetJdbcProvider(databaseProvider);

        bridgenetJdbcProvider.initConnection(jdbcSettingsConfig);

        DatabaseProvider databaseProvider = bridgenetJdbcProvider.getDatabaseProvider();
        DatabaseConnection databaseConnection = bridgenetJdbcProvider.getDatabaseConnection();
        DatabaseComposer composer = databaseProvider.getComposer();

        beansService.bind(composer);
        beansService.bind(databaseConnection);
        beansService.bind(new EntityRepositoryFactory(composer, databaseConnection));
    }

    private BridgenetJdbcProvider.JdbcSettingsConfig readSettings() {
        return assembly.readJsonAtEntity(ResourcesTypes.JDBC_JSON,
                StandardCharsets.UTF_8,
                BridgenetJdbcProvider.JdbcSettingsConfig.class);
    }

    public void connectToEndpoints(RemoteServicesManagement registry) {
        registry.initConfig();

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

    public void disconnectToEndpoints(RemoteServicesManagement registry) {
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

    public BridgenetNetworkChannel newBridgenetChannel(BridgenetNetworkController networkDriver, NetworkClientConnectionFactory connectionFactory,
                                                       BridgenetNetworkClientHandler channelHandler) {
        networkDriver.bindMessages();
        networkDriver.bindMessageListeners();

        return connectionFactory.newReferenceClient(channelHandler);
    }
}
