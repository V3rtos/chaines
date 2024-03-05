package me.moonways.bridgenet.endpoint.socials.api;

import java.util.concurrent.CompletableFuture;

public interface SocialNetworkLinkageApi {

    boolean verify(String input);

    CompletableFuture<SocialNetworkLinkageResult> link(String input);

    CompletableFuture<SocialNetworkLinkageResult> unlink(String input);
}
