package me.moonways.bridgenet.api.modern_command.message;

import me.moonways.bridgenet.api.util.minecraft.ChatColor;

public class MessageBuilder {

    private final StringBuilder stringBuilder = new StringBuilder();

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    public MessageBuilder color(ChatColor color) {
        stringBuilder.append(color.toString());
        return this;
    }

    public MessageBuilder text(String text) {
        stringBuilder.append(text);
        return this;
    }

    public String build() {
        return stringBuilder.toString();
    }
}
