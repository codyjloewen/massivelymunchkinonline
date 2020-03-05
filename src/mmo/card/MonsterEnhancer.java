package mmo.card;

import lombok.Builder;
import lombok.Value;
import mmo.ICard;

import java.util.List;

@Value
@Builder
public class MonsterEnhancer implements ICard {
    private final CardMetadata metadata;

    private final int bonusModifier;
    private final int treasureModifier;
    private final List<MonsterEnhancerEnhancer> enhancerEnhancers;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }
}
