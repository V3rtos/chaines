package me.moonways.bridgenet.jdbc.core.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class NoPasswordCredentials implements Credentials {

    private static final char[] PASSWORD_CONST = new char[0];

    private String uri;
    private final String username;

    @Override
    public char[] getPassword() {
        return PASSWORD_CONST;
    }

    @Override
    public Credentials setUri(String uri) {
        this.uri = uri;
        return this;
    }
}
