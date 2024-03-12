package me.moonways.bridgenet.model.economy.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.economy.Currency;
import me.moonways.bridgenet.model.economy.bank.BankTransaction;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class PlayerEconomyCurrencyValueUpdateEvent implements Event {

    private final UUID playerId;

    private final Currency currency;

    private final BankTransaction transaction;
}
