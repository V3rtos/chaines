package me.moonways.bridgenet.jdbc.core.security;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class BasicCredentials implements Credentials {

    private String uri;

    private final String username;
    private final String password;

    @Override
    public char[] getPassword() {
        return password.toCharArray();
    }

    @Override
    public Credentials setUri(String uri) {
        this.uri = uri;
        return this;
    }
}
