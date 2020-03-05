package mmo;

import mmo.card.CardMetadata;

public interface ICard {
    String getId();
    String getName();
    CardMetadata getMetadata();
}
