package mmo.card;

import lombok.Builder;
import lombok.Value;
import mmo.ICard;

@Value
@Builder
public class Monster implements ICard {
    private final CardMetadata metadata;

    private final int bonus;
    private final int levels;
    private final int treasures;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }
}
