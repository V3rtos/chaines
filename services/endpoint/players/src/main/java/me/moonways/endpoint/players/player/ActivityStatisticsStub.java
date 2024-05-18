package me.moonways.endpoint.players.player;

import me.moonways.bridgenet.model.players.service.statistic.ActivityStatistics;
import me.moonways.bridgenet.model.players.service.statistic.Statistic;

import java.rmi.RemoteException;
import java.util.EnumMap;

public final class ActivityStatisticsStub implements ActivityStatistics {
    private final EnumMap<Statistic, Number> statisticsValuesMap
            = new EnumMap<>(Statistic.class);

    @Override
    public void reset() throws RemoteException {
        statisticsValuesMap.clear();
    }

    @Override
    public void setInt(Statistic statistic, int value) throws RemoteException {
        statisticsValuesMap.put(statistic, value);
    }

    @Override
    public void setLong(Statistic statistic, long value) throws RemoteException {
        statisticsValuesMap.put(statistic, value);
    }

    @Override
    public int getInt(Statistic statistic) throws RemoteException {
        return (int) statisticsValuesMap.get(statistic);
    }

    @Override
    public long getLong(Statistic statistic) throws RemoteException {
        return (long) statisticsValuesMap.get(statistic);
    }
}
