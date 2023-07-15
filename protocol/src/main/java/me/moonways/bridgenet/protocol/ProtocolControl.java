package me.moonways.bridgenet.protocol;

import lombok.Getter;
import lombok.experimental.Delegate;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.message.MessageResponseContainer;
import me.moonways.bridgenet.protocol.message.MessageTriggerHandler;
import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.Inject;

@Component
@Getter
public class ProtocolControl {

    @Inject
    @Delegate
    private MessageRegistrationService registrationService;

    @Inject
    @Delegate
    private MessageResponseContainer responseContainer;

    @Inject
    @Delegate
    private MessageTriggerHandler triggerHandler;
}
