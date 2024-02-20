package me.moonways.bridgenet.mtp.client;

import me.moonways.bridgenet.mtp.MTPMessageSender;

public interface MTPClientChannelHandler {

    void onConnected(MTPMessageSender channel);

    void onDisconnected(MTPMessageSender channel);

    void onReconnect(MTPMessageSender channel);
}
