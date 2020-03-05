package mmo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Locator {
    private IDispatcher dispatcher;
    private GameState gameState;
    private GameConfiguration config;
    private LevelMutator levelMutator;

    public void provide(final IDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    public void provide(final GameState gameState) {
        this.gameState = gameState;
    }
    public void provide(final GameConfiguration config) {
        this.config = config;
    }
    public void provide(final LevelMutator levelMutator) {
        this.levelMutator = levelMutator;
    }
}
