package me.moonways.bridgenet.mtp.message;

import lombok.*;
import me.moonways.bridgenet.mtp.message.response.ResponsibleMessageService;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class ExportedMessage {

    @Setter
    private long callbackID;

    private final WrappedNetworkMessage wrapper;

    @EqualsAndHashCode.Include
    private final Object message;

    public void marksResponsible(ResponsibleMessageService service) {
        callbackID = service.generateCallbackID();
    }
}
