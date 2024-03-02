package me.moonways.bridgenet.jdbc.core.transaction.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.jdbc.core.transaction.FailedTransactionPreprocessConsumer;

import java.util.function.Supplier;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TransactionData {

    private FailedTransactionPreprocessConsumer onFailedPreprocessor;
    private TransactionIsolation isolation;

    private final Supplier<TransactionRequestContext> contextSupplier;

    public TransactionRequestContext createRequestContext() {
        return contextSupplier.get();
    }
}
