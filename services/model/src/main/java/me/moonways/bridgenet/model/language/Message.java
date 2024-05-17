package me.moonways.bridgenet.model.language;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    public static final String PLACEHOLDER_MSG_KEY = "${msg.key}";

    public static Message keyed(String key) {
        return new Message(key);
    }

    private final String key;
}
