package me.moonways.bridgenet.mtp.test.client;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import me.moonways.bridgenet.api.injection.DependencyInjection;
import me.moonways.bridgenet.api.injection.Inject;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.message.DefaultMessage;
import me.moonways.bridgenet.mtp.pipeline.NettyPipeline;

import java.util.concurrent.TimeUnit;

public class ClientTest {

    @Inject
    private MTPDriver driver;

    @Inject
    private DependencyInjection dependencyInjection;

    public static void main(String[] args) {
        DependencyInjection dependencyInjection = new DependencyInjection();
        dependencyInjection.findComponentsIntoBasePackage();

        dependencyInjection.bind(new Gson());

        ClientTest clientTest = new ClientTest();

        MTPConnectionFactory connectionFactory = MTPConnectionFactory.createConnectionFactory(dependencyInjection);
        dependencyInjection.bind(connectionFactory);

        dependencyInjection.injectFields(clientTest);

        clientTest.startClientConnection(connectionFactory);
    }

    public void startClientConnection(MTPConnectionFactory connectionFactory) {
        driver.register(DefaultMessage.class);

        ChannelFactory<? extends Channel> clientChannelFactory = NettyFactory.createClientChannelFactory();

        NettyPipeline channelInitializer = NettyPipeline.create(driver, connectionFactory.getConfiguration());
        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(2);

        MTPClient client = MTPConnectionFactory.newClientBuilder(connectionFactory)
                .setGroup(parentWorker)
                .setChannelFactory(clientChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        MTPChannel channel = client.connectSync();
        dependencyInjection.bind(channel);

        parentWorker.schedule(() -> {

            DefaultMessage message = DefaultMessage.empty();
            message.setProperty("value", "itzstonlex govnokoder!!!");

            channel.sendMessage(message);

            System.out.println("messages is sent");

        }, 3, TimeUnit.SECONDS);
    }
}
