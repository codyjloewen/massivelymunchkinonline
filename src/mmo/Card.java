package mmo;

import mmo.card.CardMetadata;

import java.util.Random;

public class Card implements ICard {

    private final String id;

    public Card() {
        this.id = String.valueOf(new Random().nextInt(100));
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public CardMetadata getMetadata() {
        return CardMetadata.builder().build();}
    @Override
    public String getName() {
        return null;
    }
}
