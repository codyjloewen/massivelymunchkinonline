package mmo.card;

import com.google.gson.annotations.JsonAdapter;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import mmo.ICard;
import mmo.card.deserializer.MonsterEnhancerEnhancerDeserializer;

@Value
@Builder
@JsonAdapter(MonsterEnhancerEnhancerDeserializer.class)
public class MonsterEnhancerEnhancer implements ICard {
    @NonNull private final CardMetadata metadata;

    @NonNull private final int bonusModifier;
    @NonNull private final int treasureModifier;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }
}
