package me.moonways.bridgenet.jdbc.core.transaction;

import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedQuery;
import org.jetbrains.annotations.NotNull;

public interface TransactionQuery {

    TransactionQuery marksSavepoint();

    TransactionQuery intermediateIsolation(@NotNull TransactionIsolation isolation);

    TransactionQuery writeSql(@NotNull String sql);

    TransactionQuery write(@NotNull CompletedQuery query);
}
