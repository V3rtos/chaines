package me.moonways.bridgenet.connector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import me.moonways.bridgenet.mtp.client.MTPClientConnectionFactory;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;

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
    private RemoteServiceRegistry remoteServiceRegistry;
    @Inject
    private MTPDriver mtpDriver;

    private final BaseBridgenetConnectorChannelHandler channelHandler = new BaseBridgenetConnectorChannelHandler();
    private final ConnectorEngine engine = new ConnectorEngine();

    @Getter
    private final BridgenetServerSync bridgenetServerSync = new BridgenetServerSync();

    protected abstract ConnectedDeviceInfo createDeviceInfo();

    /**
     * Исполнить процесс базового подключения к
     * серверу системы Bridgenet и инициализации всех
     * компонентов и ресурсов проекта для поддержания
     * стабильного соединения со всеми сервисами системы Bridgenet.
     */
    public void doConnectBasically() {
        log.info("******************************** BEGIN BRIDGENET-CONNECTOR INITIALIZATION ********************************");

        engine.setProperties();

        BeansService beansService = engine.bindAll();
        beansService.inject(this);

        engine.connectToEndpoints(remoteServiceRegistry);

        tryConnectToBridgenetServer();

        beansStore.store(beansScanner.createBean(BridgenetConnector.class, this));

        log.info("******************************** END BRIDGENET-CONNECTOR INITIALIZATION ********************************");
    }

    /**
     * Попытаться подключиться к единому серверу
     * системы Bridgenet.
     */
    private void tryConnectToBridgenetServer() {
        MTPMessageSender channel = engine.connectBridgenetServer(mtpDriver, clientConnectionFactory, channelHandler);

        bridgenetServerSync.setChannel(channel);

        onConnected(channel);
        exportDeviceHandshake();
    }

    /**
     * Отправить запрос с текущего устройства
     * на рукопожатие.
     */
    private void exportDeviceHandshake() {
        BridgenetServerSync bridgenet = getBridgenetServerSync();
        ConnectedDeviceInfo deviceInfo = createDeviceInfo();

        Handshake.Result result = bridgenet.exchangeHandshake(
                deviceInfo.getName(),
                deviceInfo.getHost(),
                deviceInfo.getPort());

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
     * Получить уникальный идентификатор текущего сервера,
     * который вернул нам Bridgenet при рукопожатии.
     */
    public final UUID getServerUuid() {
        return bridgenetServerSync.getServerUuid();
    }

    /**
     * Полностью обрубить соединение с единым сервером
     * Bridgenet и деинициализировать сервисы.
     */
    public final void shutdownConnection() {
        MTPMessageSender channel = getChannel();

        if (channel != null) {
            channel.close();
        }

        bridgenetServerSync.setChannel(null);

        clientConnectionFactory = null;
        beansStore = null;
        beansScanner = null;
        remoteServiceRegistry = null;
        mtpDriver = null;
    }
}
