package mmo;

import lombok.Value;

import java.util.List;

@Value
public class DeckHandler {
    private final Deck doors;
    private final Deck treasures;

    public void deal(
            final List<IMunchkin> munchkins,
            final int numberOfDoorsToDeal,
            final int numberOfTreasuresToDeal) {
        deal(munchkins, numberOfDoorsToDeal, doors);
        deal(munchkins, numberOfTreasuresToDeal, treasures);
    }

    private void deal(
            final List<IMunchkin> munchkins,
            final int numberToDeal,
            final Deck deck) {
        for (int i = 0; i < munchkins.size() * numberToDeal; i++) {
            final ICard card = deck.draw();
            munchkins.get(i % munchkins.size()).getHand().add(card);
        }
    }

    public void shuffle() {
        doors.getDraw().shuffle();
        treasures.getDraw().shuffle();
    }
}
