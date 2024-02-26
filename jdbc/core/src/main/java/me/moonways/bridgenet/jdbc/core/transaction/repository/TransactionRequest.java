package me.moonways.bridgenet.jdbc.core.transaction.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.jdbc.core.compose.template.completed.CompletedQuery;

@Getter
@Builder
@ToString
public class TransactionRequest {

    private TransactionIsolation intermediateIsolation;
    private TransactionResponseHandler responseHandler;

    private CompletedQuery target;
}
