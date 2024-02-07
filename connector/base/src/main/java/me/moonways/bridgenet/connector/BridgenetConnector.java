package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPConnectionFactory;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.MTPMessageSender;
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
import net.conveno.jdbc.ConvenoRouter;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
public abstract class BridgenetConnector {

    @Inject
    private MTPClientConnectionFactory clientConnectionFactory;
    @Inject
    private BeansService beansService;
    @Inject
    private BeansStore beansStore;
    @Inject
    private RemoteServiceRegistry remoteServiceRegistry;
    @Inject
    private MTPDriver mtpDriver;

    @Getter
    protected UUID serverUuid;

    private final BaseBridgenetConnectorChannelHandler channelHandler = new BaseBridgenetConnectorChannelHandler();

    @PostConstruct
    protected void initBase() {
        log.info("******************************** BEGIN BRIDGENET-CONNECTOR INITIALIZATION ********************************");

        setProperties();

        bindAll();

        connectToEndpoints();
        connectBridgenetServer();

        log.info("******************************** END BRIDGENET-CONNECTOR INITIALIZATION ********************************");
    }

    private void setProperties() {
        // jdbc settings.
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    private void bindAll() {
        beansService = new BeansService();

        beansService.bind(MTPConnectionFactory.createConnectionFactory());
        beansService.start(BeansService.generateDefaultProperties());

        beansService.bind(ConvenoRouter.create());
        beansService.bind(new Properties());

        beansService.inject(this);
    }

    private void connectToEndpoints() {
        remoteServiceRegistry.initializeXmlConfiguration();

        XMLServicesConfigDescriptor xmlConfiguration = remoteServiceRegistry.getXmlConfiguration();
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
    }

    private void connectBridgenetServer() {
        mtpDriver.bindMessages();
        mtpDriver.bindHandlers();

        MTPMessageSender channel = clientConnectionFactory.newUncastedClient(channelHandler);

        onConnected(channel);
    }

    public Handshake.Result sendServerHandshake(String serverName, String serverHost, int serverPort) {
        Properties properties = new Properties();

        properties.setProperty("server.name", serverName);
        properties.setProperty("server.address.host", serverHost);
        properties.setProperty("server.address.port", Integer.toString(serverPort));

        CompletableFuture<Handshake.Result> completableFuture = getChannel().sendMessageWithResponse(Handshake.Result.class,
                new Handshake(Handshake.Type.SERVER, properties));

        Handshake.Result result = completableFuture.join();

        serverUuid = result.getKey();
        return result;
    }

    public void onConnected(MTPMessageSender channel) {
        // override me.
    }

    public MTPMessageSender getChannel() {
        return channelHandler.getChannel();
    }
}
