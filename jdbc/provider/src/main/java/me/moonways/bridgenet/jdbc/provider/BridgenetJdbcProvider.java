package me.moonways.bridgenet.jdbc.provider;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.security.Credentials;
import me.moonways.bridgenet.jdbc.core.security.DbCredentials;

@Getter
@RequiredArgsConstructor
public class BridgenetJdbcProvider {

    @Getter
    @Builder
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class JdbcSettingsConfig {

        private final String url;
        private final String username;
        private final String password;
    }

    private final DatabaseProvider databaseProvider;
    private DatabaseConnection databaseConnection;

    public void initConnection(JdbcSettingsConfig jdbcSettingsConfig) {
        Credentials credentials = DbCredentials.basicCredentials(
                jdbcSettingsConfig.url, jdbcSettingsConfig.username, jdbcSettingsConfig.password);

        databaseConnection = databaseProvider.openConnection(credentials);
    }
}
