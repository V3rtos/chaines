package me.moonways.bridgenet.model.service.language;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    public static Message keyed(String key) {
        return new Message(key);
    }

    public static Message empty() {
        return new Message("");
    }

    private final String key;
}
