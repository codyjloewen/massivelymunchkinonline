package mmo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameConfiguration {
    private final int numberOfTreasuresToDeal;
    private final int numberOfDoorsToDeal;
    private final int goldValueForLevel;
    private final int winningLevel;
    private final int startingLevel;
    private final int minimumLevel;

    private final boolean isListenAtDoorEnabled;
}
