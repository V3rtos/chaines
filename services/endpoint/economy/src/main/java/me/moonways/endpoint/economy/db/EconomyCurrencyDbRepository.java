package me.moonways.endpoint.economy.db;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.dao.EntityAccessCondition;
import me.moonways.bridgenet.jdbc.dao.EntityDao;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.model.economy.Currency;

import java.util.UUID;

public class EconomyCurrencyDbRepository {

    private EntityDao<UserCurrencyDbEntity> dao;

    @Inject
    private DatabaseProvider databaseProvider;
    @Inject
    private DatabaseConnection databaseConnection;

    @PostConstruct
    private void initDao() {
        dao = databaseProvider.createDao(UserCurrencyDbEntity.class, databaseConnection);
    }

    public void updateEntity(UUID playerId, Currency currency, int value) {
        dao.insertMono(
                UserCurrencyDbEntity.builder()
                        .playerId(playerId)
                        .currencyName(currency.name())
                        .value(value)
                        .build());
    }

    public int getValue(UUID playerId, Currency currency) {
        return dao.findMono(EntityAccessCondition.create()
                .withMono("player_id", playerId)
                .withMono("currency", currency.name())).map(UserCurrencyDbEntity::getValue).orElse(-1);
    }
}
