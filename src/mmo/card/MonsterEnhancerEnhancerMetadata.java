package mmo.card;

import lombok.Value;

@Value
public class MonsterEnhancerEnhancerMetadata {
    private final CardMetadata metadata;

    private final int bonusModifier;
    private final int treasureModifier;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }
}
