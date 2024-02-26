package me.moonways.bridgenet.jdbc.core.transaction;

import me.moonways.bridgenet.jdbc.core.transaction.impl.DatabaseTransaction;
import me.moonways.bridgenet.jdbc.core.transaction.repository.TransactionRepository;

public final class TransactionFactory {

    public Transaction createTransaction() {
        return new DatabaseTransaction();
    }

    public PreparedTransaction createPreparedTransaction(TransactionRepository repository) {
        return new DatabaseTransaction();
    }
}
