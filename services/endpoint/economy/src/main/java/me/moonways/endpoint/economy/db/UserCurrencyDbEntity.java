package me.moonways.endpoint.economy.db;

import lombok.*;
import me.moonways.bridgenet.jdbc.dao.entity.EntityAccessible;
import me.moonways.bridgenet.jdbc.dao.entity.EntityElement;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@EntityAccessible(name = "UserEconomy")
public class UserCurrencyDbEntity {

    @EntityElement(id = "player_id") // todo - needs primary
    private final UUID playerId;

    @EntityElement(id = "currency") // todo - needs primary
    private final String currencyName;

    @EntityElement
    private final int value;
}
