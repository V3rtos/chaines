package me.moonways.bridgenet.endpoint.socials.api.type.whatsapp;

import com.google.gson.Gson;
import com.whatsapp.api.WhatsappApiFactory;
import com.whatsapp.api.WhatsappApiServiceGenerator;
import com.whatsapp.api.domain.messages.Message;
import com.whatsapp.api.domain.messages.TextMessage;
import com.whatsapp.api.domain.messages.response.MessageResponse;
import com.whatsapp.api.impl.WhatsappBusinessCloudApi;
import com.whatsapp.api.impl.WhatsappBusinessManagementApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.util.StreamToStringUtils;
import me.moonways.bridgenet.endpoint.socials.api.SocialNetworkLinkageApi;
import me.moonways.bridgenet.endpoint.socials.api.SocialNetworkLinkageResult;
import me.moonways.bridgenet.model.socials.result.SocialBindingResult;

import java.util.concurrent.CompletableFuture;

public class WhatsappLinkageApi implements SocialNetworkLinkageApi {

    public static void main(String[] args) {
        WhatsappLinkageApi whatsappLinkageApi = new WhatsappLinkageApi();
        whatsappLinkageApi.gson = new Gson();
        whatsappLinkageApi.assembly = new ResourcesAssembly();
        whatsappLinkageApi.init();

        whatsappLinkageApi.link("+79127523730");
    }

    private static final String PHONE_NUMBER_PATTERN = "\\+?([0-9]{3,16}+)^";
    private static final String PHONE_NUMBER_SENDER = "+79127523730";

    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class WhatsappCredentials {

        private final String profileID;
        private final String accessToken;

        private final int httpProxyPort;
    }

    private WhatsappBusinessCloudApi whatsappBusinessCloudApi;
    private WhatsappBusinessManagementApi whatsappBusinessManagementApi;

    @Inject
    private Gson gson;
    @Inject
    private ResourcesAssembly assembly;

    @PostConstruct
    private void init() {
        String credentialsJson = StreamToStringUtils.toStringFull(WhatsappLinkageApi.class.getResourceAsStream("/credentials/whatsapp.json"));
        WhatsappCredentials whatsappCredentials = gson.fromJson(credentialsJson, WhatsappCredentials.class);

        WhatsappApiFactory factory = WhatsappApiFactory.newInstance(whatsappCredentials.getAccessToken());

        WhatsappApiServiceGenerator.setHttpProxy("localhost",
                whatsappCredentials.getHttpProxyPort(), null,null);

        whatsappBusinessCloudApi = factory.newBusinessCloudApi();
        whatsappBusinessManagementApi = factory.newBusinessManagementApi();
    }

    @Override
    public boolean verify(String input) {
        return input.matches(PHONE_NUMBER_PATTERN);
    }

    @Override
    public CompletableFuture<SocialNetworkLinkageResult> link(String input) {
        MessageResponse response = whatsappBusinessCloudApi.sendMessage(PHONE_NUMBER_SENDER,
                Message.MessageBuilder.builder()
                        .setTo(input)
                        .buildTextMessage(new TextMessage()
                                .setBody("привет чебурашка, пишу тебе от имени BridgeNet, это тестовое сообщение написанное при помощи Whatsapp-API")));
        return CompletableFuture.completedFuture(
                new SocialNetworkLinkageResult(null, SocialBindingResult.SUCCESS)
        );
    }

    @Override
    public CompletableFuture<SocialNetworkLinkageResult> unlink(String input) {
        return null;
    }
}
