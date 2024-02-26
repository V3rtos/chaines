package me.moonways.bridgenet.jdbc.core.compose.template.completed;

import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.ResponseStream;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.jdbc.core.util.result.Result;

public interface CompletedQuery {

    Result<ResponseStream> callTransactional(DatabaseConnection connection);

    Result<ResponseStream> callTransactional(TransactionIsolation isolation, DatabaseConnection connection);

    Result<ResponseStream> call(DatabaseConnection connection);

    Result<String> toNativeQuery();
}
