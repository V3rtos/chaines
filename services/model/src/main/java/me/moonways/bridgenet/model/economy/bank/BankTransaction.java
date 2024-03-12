package me.moonways.bridgenet.model.economy.bank;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.economy.Currency;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class BankTransaction {

    private final UUID transactionId;

    private final BankTransactionResult result;
    private final PostTransferState state;

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class PostTransferState {

        private final Currency currency;

        private final int current;
        private final int spent;
        private final int received;
    }
}
