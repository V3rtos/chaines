package me.moonways.bridgenet.jdbc.core.transaction.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;

@Getter
@ToString
@RequiredArgsConstructor
public class TransactionRequestContext {

    private final DatabaseComposer composer;
    private final DatabaseConnection activeConnection;
}
