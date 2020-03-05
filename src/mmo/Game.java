package mmo;

import lombok.Data;
import mmo.annotations.IsTurn;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {
    private final Locator locator;
    private final PlayerHandler playerHandler;
    private final DeckHandler deckHandler;

    private Player currentPlayer;
    private GamePhase gamePhase;
    private TurnPhase turnPhase;

    public Game(
            final Locator locator,
            final PlayerHandler playerHandler,
            final DeckHandler deckHandler) {
        this.locator = locator;
        this.playerHandler = playerHandler;
        this.deckHandler = deckHandler;

        locator.provide(new LevelMutator(locator, this));
        currentPlayer = playerHandler.getPlayers().get(0);
        gamePhase = GamePhase.NOT_STARTED;
        if (locator.getConfig().isListenAtDoorEnabled()) {
            turnPhase = TurnPhase.LISTEN_AT_DOOR;
        } else {
            turnPhase = TurnPhase.KICK_DOWN_DOOR;
        }
    }

    public void start() {
        gamePhase = GamePhase.STARTED;

        deckHandler.shuffle();
        deckHandler.deal(playerHandler.getMunchkins(), locator.getConfig().getNumberOfDoorsToDeal(),
                locator.getConfig().getNumberOfTreasuresToDeal());

        gamePhase = GamePhase.IN_PROGRESS;
    }

    @IsTurn
    public void listenAtTheDoor(final String playerId) {
        if (locator.getConfig().isListenAtDoorEnabled() &&
            verifyTurnPhase(TurnPhase.LISTEN_AT_DOOR)) {

            final ICard card = deckHandler.getDoors().draw();
            currentPlayer.getMunchkin().getHand().add(card);

            turnPhase = TurnPhase.KICK_DOWN_DOOR;
            System.out.println(String.format("%s listened at the door: %s", currentPlayer.getUsername(),
                    card.getName()));
        }
    }

    public void kickDownTheDoor(final String playerId) {
        if (currentPlayer.getId().equals(playerId) &&
                verifyTurnPhase(TurnPhase.KICK_DOWN_DOOR)) {

            final ICard card = deckHandler.getDoors().draw();

            System.out.println(String.format("%s kicked down the door: %s", currentPlayer.getUsername(),
                    card.getName()));
        }
    }

    public void lookForTrouble(final String playerId) {

    }

    public void lootTheRoom() {

    }

    public void endTurn(final String playerId) {

    }

    private boolean verifyTurnPhase(final TurnPhase turnPhase) {
        if (this.turnPhase != turnPhase) {
            System.out.println(String.format("You can't %s. You need to %s",
                    turnPhase.name(), this.turnPhase.name()));
            return false;
        }
        return true;
    }

    public ICard getCard(final String id) {
        final List<ICard> cards = new ArrayList<>();
        final List<IMunchkin> munchkins = playerHandler.getMunchkins();
        for (final IMunchkin munchkin : munchkins) {
            cards.addAll(munchkin.getHand());
            cards.addAll(munchkin.getInPlay());
        }
        cards.addAll(deckHandler.getDoors().getDraw().getCards());
        cards.addAll(deckHandler.getDoors().getDraw().getCards());
        cards.addAll(deckHandler.getTreasures().getDiscard().getCards());
        cards.addAll(deckHandler.getTreasures().getDiscard().getCards());
        return cards.stream()
                .filter(card -> card.getId().equals(id))
                .findFirst().get();
    }
}
