package mmo.card;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import mmo.Game;
import mmo.ICard;
import mmo.ItemSize;
import mmo.Munchkin;

@SuperBuilder
@Getter
public abstract class Item implements ICard {
    private final CardMetadata metadata;

    private final int value;
    private final ItemSize size;

    public String getId() {
        return metadata.getId();
    }

    public String getName() {
        return metadata.getName();
    }

    public void carry(final Munchkin munchkin) {

    }
}
