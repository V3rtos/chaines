package me.moonways.bridgenet.model.economy.deposit;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.economy.currency.bank.BankTransaction;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DepositOperation {

    public enum Result {

        SUCCESS_OPERATION,
        CANCELLED,
        NOT_ENOUGH_FUNDS,
    }

    private final ActiveDeposit deposit;
    private final BankTransaction transaction;

    private final Result result;
}
