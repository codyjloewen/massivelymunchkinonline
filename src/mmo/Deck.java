package mmo;

import lombok.Value;

@Value
public class Deck {
    private final Pile draw;
    private final Pile discard;

    public ICard draw() {
        if (draw.isEmpty()) {
            draw.addAll(discard.takeAll());
            draw.shuffle();
        }
        return draw.take();
    }
}
