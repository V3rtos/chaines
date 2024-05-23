package me.moonways.endpoint.players;

import me.moonways.bridgenet.model.service.players.component.PlayerLeveling;

public final class PlayerLevelingStub implements PlayerLeveling {

    private static final double BASE = 10_000;
    private static final double GROWTH = 2_500;

    private static final double HALF_GROWTH = 0.5 * GROWTH;

    private static final double REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH;
    private static final double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
    private static final double GROWTH_DIVIDES_2 = 2 / GROWTH;

    public static int getLevel(int totalExperience) {
        return totalExperience < 0 ? 1 : (int) Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * totalExperience));
    }

    public static int getTotalExperience(int level) {
        return (int) ((HALF_GROWTH * (level - 2) + BASE) * (level - 1));
    }

    public static int getExperienceToNextLevel(int level) {
        return (int) (level < 1 ? BASE : GROWTH * (level - 1) + BASE);
    }

    public static int getExperiencePercentToNextLevel(int totalExperience) {
        int level = getLevel(totalExperience),
                totalExperienceStart = getTotalExperience(level);
        return ((totalExperience - totalExperienceStart) / (getTotalExperience(level + 1) - totalExperienceStart)) * 100;
    }

    public static int subtotalExperience(int level, int experience) {
        return getTotalExperience(level) + experience;
    }

    @Override
    public int toLevel(int totalExperience) {
        return getLevel(totalExperience);
    }

    @Override
    public int totalExperience(int level) {
        return getTotalExperience(level);
    }

    @Override
    public int experienceToNextLevel(int level) {
        return getExperienceToNextLevel(level);
    }

    @Override
    public int experiencePercentToNextLevel(int totalExperience) {
        return getExperiencePercentToNextLevel(totalExperience);
    }

    @Override
    public int subtotal(int level, int experience) {
        return subtotalExperience(level, experience);
    }
}
