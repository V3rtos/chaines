package me.moonways.bridgenet.jdbc.core.transaction;

import me.moonways.bridgenet.jdbc.core.Combinable;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import org.jetbrains.annotations.NotNull;

public interface Transaction extends Combinable<PreparedTransaction> {

    Transaction isolation(@NotNull TransactionIsolation isolation);

    Transaction onFailed(@NotNull FailedTransactionPreprocessConsumer failed);

    Transaction query(@NotNull TransactionQuery query);
}
