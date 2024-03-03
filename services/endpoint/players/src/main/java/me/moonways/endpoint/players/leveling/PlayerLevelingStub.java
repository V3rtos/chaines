package me.moonways.endpoint.players.leveling;

import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.bridgenet.model.players.leveling.PlayerLeveling;

import java.rmi.RemoteException;

public class PlayerLevelingStub extends EndpointRemoteObject implements PlayerLeveling {

    private static final long serialVersionUID = 3304541429104254807L;

    private static final double BASE = 10_000;
    private static final double GROWTH = 2_500;

    private static final double HALF_GROWTH = 0.5 * GROWTH;

    private static final double REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH;
    private static final double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
    private static final double GROWTH_DIVIDES_2 = 2 / GROWTH;

    public PlayerLevelingStub() throws RemoteException {
        super();
    }

    @Override
    public int calculateLevel(int totalExperience) {
        return totalExperience < 0 ? 1 : (int) Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * totalExperience));
    }

    @Override
    public int calculateTotalExperience(int level) {
        return (int) ((HALF_GROWTH * (level - 2) + BASE) * (level - 1));
    }

    @Override
    public int calculateExperienceToNextLevel(int level) {
        return (int) (level < 1 ? BASE : GROWTH * (level - 1) + BASE);
    }

    @Override
    public int calculateExperiencePercentToNextLevel(int totalExperience) {
        int level = calculateLevel(totalExperience),
                totalExperienceStart = calculateTotalExperience(level);
        return ((totalExperience - totalExperienceStart) / (calculateTotalExperience(level + 1) - totalExperienceStart)) * 100;
    }

    @Override
    public int subtotal(int level, int experience) {
        return calculateTotalExperience(level) + experience;
    }
}
