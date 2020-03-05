package mmo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevelMutator {
    private final Locator locator;
    private final Game game;

    public boolean canLevel(final IMunchkin munchkin, final int levels) {
        return munchkin.getLevel() + levels < locator.getConfig().getWinningLevel();
    }

    public void level(final IMunchkin munchkin, final int levels) {
        level(munchkin, levels, false);
    }

    public void level(final IMunchkin munchkin, final int levels, final boolean canBeWinningLevel) {
        if (!canBeWinningLevel && !canLevel(munchkin, levels)) {
            throw new RuntimeException("Winning level");
        }
    }
}
