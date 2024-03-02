package me.moonways.bridgenet.jdbc.core.security;

public interface Credentials {

    String getUri();

    String getUsername();

    char[] getPassword();

    Credentials setUri(String uri);
}
