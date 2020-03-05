import mmo.Pile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeckTest {
    @Test
    public void testDrawEmptyDeck() {
        final Pile deck = new Pile();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
           deck.take();
        });
    }

    @Test
    public void testIsEmpty() {

    }
}
