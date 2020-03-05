package mmo;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Pile implements IPile {

    private final List<ICard> pile;

    public Pile() {
        pile = new ArrayList<>();
    }

    @Override
    public ICard take() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("mmo.Deck is empty.");
        }
        return pile.remove(0);
    }

    @Override
    public ImmutableList<ICard> getCards() {
        return ImmutableList.copyOf(pile);
    }

    @Override
    public void add(final ICard card) {
        if (pile.contains(card)) {
            throw new UnsupportedOperationException("mmo.Deck already has card: " + card);
        }
        pile.add(0, card);
    }

    @Override
    public void addAll(final List<ICard> cards) {
        for (final ICard card : cards) {
            add(card);
        }
    }

    @Override
    public Optional<ICard> take(final String id) {
        for (final Iterator<ICard> iterator = pile.iterator(); iterator.hasNext();) {
            final ICard card = iterator.next();
            if (card.getId().equals(id)) {
                iterator.remove();
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ICard> take(final List<String> ids) {
        final List<ICard> cards = new ArrayList<>();
        for (final Iterator<ICard> iterator = pile.iterator(); iterator.hasNext();) {
            final ICard card = iterator.next();
            if (ids.contains(card.getId())) {
                cards.add(card);
                iterator.remove();
            }
        }
        return cards;
    }

    @Override
    public List<ICard> takeAll() {
        final List<ICard> clone = new ArrayList<>(pile);
        pile.clear();
        return clone;
    }

    @Override
    public boolean contains(final String id) {
        return pile.stream().anyMatch(card -> card.getId().equals(id));
    }

    @Override
    public boolean isEmpty() {
        return pile.size() == 0;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(pile);
    }
}
