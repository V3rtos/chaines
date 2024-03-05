package me.moonways.bridgenet.endpoint.socials.api.type.telegram;

import me.moonways.bridgenet.endpoint.socials.api.SocialNetworkLinkageApi;
import me.moonways.bridgenet.endpoint.socials.api.SocialNetworkLinkageResult;

import java.util.concurrent.CompletableFuture;

public class TelegramLinkageApi implements SocialNetworkLinkageApi {

    private static final String NAME_TAG_PREFIX = "@";
    private static final String NAME_TAG_URL_PREFIX = "t.me/";

    @Override
    public boolean verify(String input) {
        return input.startsWith(NAME_TAG_PREFIX) || input.contains(NAME_TAG_URL_PREFIX);
    }

    @Override
    public CompletableFuture<SocialNetworkLinkageResult> link(String input) {
        return null;
    }

    @Override
    public CompletableFuture<SocialNetworkLinkageResult> unlink(String input) {
        return null;
    }
}
