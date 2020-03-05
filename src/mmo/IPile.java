package mmo;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

public interface IPile {
    ICard take();
    ImmutableList<ICard> getCards();
    void add(ICard card);
    void addAll(List<ICard> cards);
    Optional<ICard> take(String id);
    List<ICard> take(List<String> ids);
    List<ICard> takeAll();
    boolean contains(String id);
    boolean isEmpty();
    void shuffle();
}
