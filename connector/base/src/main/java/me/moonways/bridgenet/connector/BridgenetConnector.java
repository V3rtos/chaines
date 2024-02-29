package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;
import me.moonways.bridgenet.connector.description.DeviceDescription;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import me.moonways.bridgenet.mtp.client.MTPClientConnectionFactory;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public abstract class BridgenetConnector {

    @Inject
    private MTPClientConnectionFactory clientConnectionFactory;
    @Inject
    private BeansStore beansStore;
    @Inject
    private BeansScanningService beansScanner;
    @Inject
    private MTPDriver mtpDriver;

    @Getter
    private final ConnectorEngine engine = new ConnectorEngine();
    @Getter
    private final BridgenetServerSync bridgenetServerSync = new BridgenetServerSync();
    @Getter
    private final BridgenetGamesSync bridgenetGamesSync = new BridgenetGamesSync(bridgenetServerSync);

    private final BaseBridgenetConnectorChannelHandler channelHandler = new BaseBridgenetConnectorChannelHandler(this);

    protected abstract DeviceDescription createDescription();

    /**
     * Исполнить процесс базового подключения к
     * серверу системы Bridgenet и инициализации всех
     * компонентов и ресурсов проекта для поддержания
     * стабильного соединения со всеми сервисами системы Bridgenet.
     */
    public void start() {
        log.info("***************************** BEGIN BRIDGENET-CONNECTOR INITIALIZATION *****************************");
        engine.setProperties();

        BeansService beansService = engine.bindAll();

        beansService.bind(bridgenetServerSync);
        beansService.bind(bridgenetGamesSync);

        beansService.inject(channelHandler);

        beansService.bind(this);

        log.info("****************************** END BRIDGENET-CONNECTOR INITIALIZATION ******************************");

        doConnect();
    }

    /**
     * Исполнить процесс базового подключения к
     * серверу системы Bridgenet и инициализации всех
     * компонентов и ресурсов проекта для поддержания
     * стабильного соединения со всеми сервисами системы Bridgenet.
     */
    private void doConnect() {
        tryConnectToBridgenetServer();
        beansStore.store(beansScanner.createBean(BridgenetConnector.class, this));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> channelHandler.onDisconnected(getChannel())));
    }

    /**
     * Попытаться подключиться к единому серверу
     * системы Bridgenet.
     */
    private void tryConnectToBridgenetServer() {
        MTPMessageSender channel = engine.connectBridgenetServer(mtpDriver, clientConnectionFactory, channelHandler);
        handleConnection(channel);
    }

    /**
     * Обработать подключенное клиентское соединения для
     * последующей инициализации и корректного запуска процессов.
     *
     * @param channel - клиентский канал.
     */
    protected void handleConnection(MTPMessageSender channel) {
        bridgenetServerSync.setChannel(channel);
        exportDeviceHandshake();
    }

    /**
     * Отправить запрос с текущего устройства
     * на рукопожатие.
     */
    private void exportDeviceHandshake() {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        DeviceDescription description = createDescription();

        Handshake.Result result = bridgenet.exportDeviceHandshake(description);

        onHandshake(result);
    }

    /**
     * Переопределяющаяся функция, вызываемая при успешном
     * подключении к единому серверу Bridgenet.
     *
     * @param channel - клиентский канал, которому удалось подключиться.
     */
    public void onConnected(MTPMessageSender channel) {
        // override me.
    }

    /**
     * Переопределяющаяся функция, вызываемая при обмене
     * рукопожатием с единым сервером Bridgenet.
     *
     * @param result - результат рукопожатия.
     */
    public void onHandshake(Handshake.Result result) {
        // override me.
    }

    /**
     * Получить клиентский канал к единому серверу Bridgenet.
     */
    public final MTPMessageSender getChannel() {
        MTPMessageSender channel = channelHandler.getChannel();
        bridgenetServerSync.setChannel(channel);

        return channel;
    }

    /**
     * Получить уникальный идентификатор текущего устройства,
     * который вернул нам Bridgenet при рукопожатии.
     */
    public final UUID getCurrentDeviceId() {
        return bridgenetServerSync.getCurrentDeviceId();
    }

    /**
     * Полностью обрубить соединение с единым сервером
     * Bridgenet и деинициализировать сервисы.
     */
    public final void shutdownConnection() {
        MTPMessageSender channel = getChannel();

        if (channel != null) {
            channelHandler.onDisconnected(channel);

            channel.close();
        }

        bridgenetServerSync.setChannel(null);

        clientConnectionFactory = null;
        beansStore = null;
        beansScanner = null;
        mtpDriver = null;
    }
}
