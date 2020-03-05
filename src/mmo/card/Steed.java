package mmo.card;

import lombok.Value;
import mmo.ICard;

@Value
public class Steed implements ICard {
    private final CardMetadata metadata;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }
}
