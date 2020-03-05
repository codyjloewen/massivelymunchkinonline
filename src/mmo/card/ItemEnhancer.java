package mmo.card;

import lombok.Value;
import mmo.ICard;

@Value
public class ItemEnhancer implements ICard {
    private final CardMetadata metadata;

    private final int bonus;
    private final ItemEnhancerCondition condition;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }
}
