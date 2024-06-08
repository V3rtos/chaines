package me.moonways.bridgenet.mtp.channel;

import io.netty.channel.Channel;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;

@Autobind
public class NetworkChannelFactory {

    @Inject
    private BeansService beansService;

    public BridgenetNetworkChannel create(ChannelDirection direction, Channel channel) {
        BridgenetNetworkChannel wrapper = new NetworkRemoteChannel(direction, channel);
        beansService.inject(wrapper);
        return wrapper;
    }
}
