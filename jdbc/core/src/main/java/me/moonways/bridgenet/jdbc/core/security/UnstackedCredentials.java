package me.moonways.bridgenet.jdbc.core.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class UnstackedCredentials implements Credentials {

    private String uri;
    private final String username;
    private final char[] password;

    @Override
    public Credentials setUri(String uri) {
        this.uri = uri;
        return this;
    }
}
